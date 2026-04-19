package com.yy.superaiagent.agent;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.yy.superaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理工具调用的基础代理类，具体实现了 think 和 act 方法，可以用作创建实例的父类
 *
 * @author wyy
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent {

    // 可用工具
    private final ToolCallback[] availableTools;

    // 保存工具调用信息的响应结果（要调用哪些工具）
    private ChatResponse toolCallResponse;

    // 工具管理者
    private final ToolCallingManager toolCallingManager;

    //禁用 Spring AI 内置的工具调用机制，自己维护选项和消息上下文
    private ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] availableTools) {
        super();
        this.availableTools = availableTools;
        this.toolCallingManager = ToolCallingManager.builder().build();
        // 禁用 Spring AI 内置的工具调用机制，自己维护选项和消息上下文
        this.chatOptions = ToolCallingChatOptions.builder()
                .internalToolExecutionEnabled(false)
                .build();
    }

    /**
     * 处理当前状态并决定下一步行动
     *
     * @return 是否需要执行行动，true表示需要执行，false表示不需要执行
     */
    @Override
    public Boolean think() {
        // 1、校验提示词，并拼接用户提示词
        if (StrUtil.isNotBlank(this.getNextStepPrompt())) {
            this.getMessageList().add(new UserMessage(this.getNextStepPrompt()));
        }

        // 2、调用 AI 大模型，获取工具调用结果
        List<Message> messageList = this.getMessageList();
        Prompt prompt = new Prompt(messageList, this.chatOptions);
        try {
            ChatResponse chatResponse = this.getChatClient().prompt(prompt)
                    .system(this.getSystemPrompt())
                    .toolCallbacks(this.availableTools)
                    .call()
                    .chatResponse();
            // 记录响应，等下用于 Act
            this.toolCallResponse = chatResponse;

            // 3、解析工具调用结果，获取需要调用的工具
            // 助手消息
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            // 获取需要调用的工具列表
            List<AssistantMessage.ToolCall> toolCallList = assistantMessage.getToolCalls();
            // 输出提示消息
            String result = assistantMessage.getText();
            log.info(getName() + "的思考：" + result);
            log.info(getName() + "选择了 " + toolCallList.size() + " 个工具来使用");
            String toCallInfo = toolCallList.stream()
                    .map(toolCall -> String.format("工具：%s,参数：%s", toolCall.name(), toolCall.arguments()))
                    .collect(Collectors.joining("\n"));
            log.info(toCallInfo);
            //如果不需要调用工具就返回false
            if (toolCallList.isEmpty()) {
                // 只有不调用工具时，才需要手动记录助手消息
                this.getMessageList().add(assistantMessage);
                return false;
            } else {
                // 需要调用工具时，无需记录助手消息，因为调用工具时会自动记录
                return true;
            }
        } catch (Exception e) {
            log.error(getName() + "的思考过程遇到了问题：" + e.getMessage());
            this.getMessageList().add(new AssistantMessage("处理时遇到了错误：" + e.getMessage()));
            return false;

        }
    }

    /**
     * 执行决定的行动
     *
     * @return 行动执行结果
     */
    @Override
    public String act() {
        // 1、基础校验
        if (!this.toolCallResponse.hasToolCalls()){
            return "没有工具需要调用";
        }
        // 调用工具
        Prompt prompt = new Prompt(this.getMessageList(), this.chatOptions);
        ToolExecutionResult toolExecutionResult = this.toolCallingManager.executeToolCalls(prompt, this.toolCallResponse);
        // 记录消息上下文，conversationHistory 已经包含了助手消息和工具调用返回的结果
        this.setMessageList(toolExecutionResult.conversationHistory());
        ToolResponseMessage toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());
        // 判断是否调用了终止工具
        boolean terminateToolCalled = toolResponseMessage.getResponses()
                .stream()
                .anyMatch(toolResponse -> "doTerminate".equals(toolResponse.name()));
        if (terminateToolCalled) {
            //任务结束，更改状态
            this.setState(AgentState.FINISHED);
        }
        String results = toolResponseMessage.getResponses()
                .stream()
                .map(toolResponse -> "工具" + toolResponse.name() + "返回的结果：" + toolResponse.responseData())
                .collect(Collectors.joining("\n"));
        log.info(results);

        return results;
    }
}
