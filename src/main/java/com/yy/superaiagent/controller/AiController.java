package com.yy.superaiagent.controller;

import com.yy.superaiagent.agent.Manus;
import com.yy.superaiagent.app.PetApp;
import com.yy.superaiagent.common.BaseResponse;
import com.yy.superaiagent.common.ResultUtils;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

/**
 * AI 宠物咨询应用 与 AI 超级智能体 控制器
 *
 * @author wyy
 */
@RestController
@RequestMapping("/ai")
public class AiController {
    @Resource
    private PetApp petApp;
    @Resource
    private ToolCallback[] allTools;
    @Resource
    private ChatModel dashScopeChatModel;

    /**
     * 同步调用 AI 宠物咨询应用
     * @param message 用户输入
     * @param chatId 会话ID
     * @return 聊天结果
     */
    @GetMapping("/love_app/chat/sync")
    public BaseResponse<String> doChatWithPetAppSync(String message, String chatId) {
        String result = petApp.doChatWithAll(message, chatId);
        return ResultUtils.success(result);
    }

    /**
     * SSE 流式调用 AI 宠物咨询应用
     *
     * @param message 用户输入
     * @param chatId 会话ID
     * @return 聊天结果
     */
    @GetMapping(value = "/love_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public BaseResponse<Flux<String>> doChatWithPetAppSse(String message, String chatId) {
        Flux<String> result = petApp.doChatWithAllByStream(message, chatId);
        return ResultUtils.success(result);
    }

    /**
     * SSE 流式调用 AI 宠物咨询应用
     *
     * @param message 用户输入
     * @param chatId 会话ID
     * @return 聊天结果
     */
    @GetMapping(value = "/love_app/chat/server_sent_event")
    public BaseResponse<Flux<ServerSentEvent<String>>> doChatWithPetAppServerSentEvent(String message, String chatId) {
        Flux<ServerSentEvent<String>> result = petApp.doChatWithAllByStream(message, chatId)
                .map(chunk -> ServerSentEvent.<String>builder().data(chunk).build());
        return ResultUtils.success(result);
    }

    /**
     * SSE 流式调用 AI 宠物咨询应用
     *
     * @param message 用户输入
     * @param chatId 会话ID
     * @return 聊天结果
     */
    @GetMapping(value = "/love_app/chat/sse_emitter")
    public BaseResponse<SseEmitter> doChatWithLoveAppServerSseEmitter(String message, String chatId) {
        //创建一个超长时间较长的 SseEmitter
        SseEmitter sseEmitter = new SseEmitter(180000L);
        // 获取 Flux 响应式数据流并且直接通过订阅推送给 SseEmitter
        petApp.doChatWithAllByStream(message, chatId)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(chunk);
                    } catch (Exception e) {
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);
        //返回
        return ResultUtils.success(sseEmitter);
    }

    /**
     * 流式调用 Manus 超级智能体
     *
     * @param message 用户输入
     * @return 聊天结果
     */
    @GetMapping("/manus/chat")
    public BaseResponse<SseEmitter> doChatWithManus(String message) {
        Manus manus = new Manus(allTools, dashScopeChatModel);
        SseEmitter result = manus.runStream(message);
        return ResultUtils.success(result);
    }
}
