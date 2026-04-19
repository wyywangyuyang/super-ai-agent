package com.yy.superaiagent.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

/**
 * 菜狗的 AI 超级智能体（拥有自主规划能力，可以直接使用）
 * @author wyy
 */
@Component
public class Manus extends ToolCallAgent {

    private static final String NAME = "Manus";

    private static final String SYSTEM_PROMPT = """
                You are Manus, an all-capable AI assistant, aimed at solving any task presented by the user.
                You have various tools at your disposal that you can call upon to efficiently complete complex requests.
                """;

    private static final String NEXT_STEP_PROMPT = """
                Based on user needs, proactively select the most appropriate tool or combination of tools.
                For complex tasks, you can break down the problem and use different tools step by step to solve it.
                After using each tool, clearly explain the execution results and suggest the next steps.
                If you want to stop the interaction at any point, use the `terminate` tool/function call.
                """;

    public Manus(ToolCallback[] allTools, ChatModel dashScopeChatModel) {
        super(allTools);
        // 设置名称
        this.setName(NAME);
        // 设置系统提示
        this.setSystemPrompt(SYSTEM_PROMPT);
        // 设置下一步提示
        this.setNextStepPrompt(NEXT_STEP_PROMPT);
        // 设置最大步骤数
        this.setMaxSteps(20);
        // 初始化对话客户端
        ChatClient chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        this.setChatClient(chatClient);
    }
}
