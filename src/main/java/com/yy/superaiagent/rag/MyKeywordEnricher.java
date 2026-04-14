package com.yy.superaiagent.rag;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 基于 AI 的文档元信息增强器（为文档补充元信息）
 *
 * @author wyy
 */
@Component
public class MyKeywordEnricher {

    private final ChatModel dashScopeChatModel;

    public MyKeywordEnricher(ChatModel dashScopeChatModel) {
        this.dashScopeChatModel = dashScopeChatModel;
    }

    public List<Document> enrichDocuments(List<Document> documents){
        KeywordMetadataEnricher keywordMetadataEnricher = KeywordMetadataEnricher.builder(dashScopeChatModel)
                // 设置关键词数量
                .keywordCount(5)
                .build();
        return keywordMetadataEnricher.apply(documents);
    }
}
