package com.yy.superaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 宠物咨询向量数据库配置（初始化基于内存的向量数据库 Bean）
 *
 * @author wyy
 */
@Configuration
public class PetAppVectorStoreConfig {

    @Resource
    private PetAppDocumentLoader petAppDocumentLoader;

    @Bean
    VectorStore petAppVectorStore(EmbeddingModel dashScopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashScopeEmbeddingModel).build();
        List<Document> documentList = petAppDocumentLoader.loadMarkdowns();
        simpleVectorStore.add(documentList);
        return simpleVectorStore;
    }
}
