package com.yy.superaiagent.app;

import com.yy.superaiagent.advisor.MyLoggerAdvisor;
import com.yy.superaiagent.advisor.ReReadingAdvisor;
import com.yy.superaiagent.chatmemory.FileBasedChatMemory;
import com.yy.superaiagent.rag.PetAppRagCustomAdvisorFactory;
import com.yy.superaiagent.rag.QueryRewriter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 宠物咨询客户端
 *
 * @author wyy
 */
@Component
@Slf4j
public class PetApp {

    private final ChatClient chatClient;

    /**
     * 系统提示语
     */
    private static final String SYSTEM_PROMPT = "你是一名拥有10年以上宠物诊疗、行为训练经验的资深宠物专家，" +
            "擅长解答犬、猫等常见宠物的健康问题、行为异常问题，熟悉宠物生理特点、常见疾病症状、行为习惯引导方法，" +
            "具备耐心、细致、专业的沟通能力，全程模拟真实宠物咨询场景，以用户为中心，通过引导性提问逐步深入了解情况，" +
            "最终提供全面、可操作、针对性强的建议。";

    /**
     * 初始化ChatClient
     * @param dashScopeChatModel 阿里大模型
     */
    public PetApp(ChatModel dashScopeChatModel) {
        //初始化基于文件的对话记忆
//        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
//        FileBasedChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        // 初始化基于内存的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                // 设置最大存储消息数
                .maxMessages(20)
                .build();
        chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        // 自定义日志 Advisor，可按需开启
                        new MyLoggerAdvisor()
                        // 自定义推理增强 Advisor，可按需开启
//                        new ReReadingAdvisor()
                )
                .build();
    }

    /**
     * AI基础对话（支持多轮会话记忆）
     * @param message 用户输入
     * @param chatId 会话ID
     * @return 聊天结果
     */
    public String doChat(String message, String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content：{}", content);
        return content;
    }

    record PetReport(String title, List<String> suggestions) {
    }

    /**
     * AI宠物咨询报告功能（实战结构化输出）
     * @param message 用户输入
     * @param chatId 会话ID
     * @return 聊天结果
     */
    public PetReport doChatWithReport(String message, String chatId){
        PetReport petReport = chatClient.prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成宠物咨询报告，标题为{用户名}的宠物咨询报告，内容为建议列表")
                .user(message)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .entity(PetReport.class);
        log.info("petReport：{}", petReport);
        return petReport;
    }

    @Resource
    private VectorStore petAppVectorStore;
    @Resource
    private Advisor petAppRagCloudAdvisor;
    @Resource
    private VectorStore pgVectorVectorStore;
    @Resource
    private QueryRewriter queryRewriter;

    /**
     * 与 RAG 知识库进行对话
     * @param message 用户输入
     * @param chatId 会话ID
     * @return 聊天结果
     */
    public String doChatWithRag(String message, String chatId) {
        //查询重写
        String rewriterMessage = queryRewriter.doQueryRewriter(message);

        ChatResponse chatResponse = chatClient.prompt()
                // 使用改写后的查询
                .user(rewriterMessage)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatId))
                //开启日志便于观察
                .advisors(new SimpleLoggerAdvisor())
                // 应用 RAG 知识库问答
                .advisors(QuestionAnswerAdvisor.builder(petAppVectorStore).build())
                // 应用 RAG 检索增强服务（基于云知识库服务）
//                .advisors(petAppRagCloudAdvisor)
                // 应用 RAG 检索增强服务（基于 PgVector 向量存储）
//                .advisors(QuestionAnswerAdvisor.builder(pgVectorVectorStore).build())
                // 应用自定义的 RAG 检索增强服务（文档增强器 + 上下文增强器）
//                .advisors(PetAppRagCustomAdvisorFactory.createPetAppRagCustomAdvisor(petAppVectorStore, "猫"))
                .call()
                .chatResponse();

        String content = chatResponse.getResult().getOutput().getText();
        log.info("content：{}", content);
        return content;
    }


    @Resource
    private ToolCallback[] allTools;

    /**
     * AI宠物咨询报告功能（支持工具调用）
     * @param message 用户输入
     * @param chatId 会话ID
     * @return 聊天结果
     */
    public String doChatWithTools(String message, String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatId))
                //开启日志便于观察
                .advisors(new SimpleLoggerAdvisor())
                .tools(allTools)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content：{}", content);
        return content;
    }
}
