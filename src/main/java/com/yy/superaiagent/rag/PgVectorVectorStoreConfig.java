package com.yy.superaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

/**
 * 使用 PgVector 存储知识库
 *
 * @author wyy
 */
// 为方便开发调试和部署，临时注释，如果需要使用 PgVector 存储知识库，取消注释即可
//@Configuration
public class PgVectorVectorStoreConfig {

    @Resource
    private PetAppDocumentLoader petAppDocumentLoader;

    @Bean
    public VectorStore pgVectorVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel dashscopeEmbeddingModel) {
        PgVectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, dashscopeEmbeddingModel)
                // Optional: defaults to model dimensions or 1536
                .dimensions(1536)
                // Optional: defaults to COSINE_DISTANCE
                .distanceType(COSINE_DISTANCE)
                // Optional: defaults to HNSW
                .indexType(HNSW)
                // Optional: defaults to false
                .initializeSchema(true)
                // Optional: defaults to "public"
                .schemaName("public")
                // Optional: defaults to "vector_store"
                .vectorTableName("vector_store")
                // Optional: defaults to 10000PgVectorVectorStoreConfigTest
                .maxDocumentBatchSize(10000)
                .build();

        // 加载文档
        List<Document> documentList = petAppDocumentLoader.loadMarkdowns();
        vectorStore.add(documentList);
        return vectorStore;
    }
}
