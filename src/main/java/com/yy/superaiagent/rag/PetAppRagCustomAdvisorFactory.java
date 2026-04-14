package com.yy.superaiagent.rag;

import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/**
 * 创建自定义的 RAG 检索增强顾问工厂
 *
 * @author wyy
 */
public class PetAppRagCustomAdvisorFactory {

    /**
     * 创建自定义的 RAG 检索增强顾问
     *
     * @param vectorStore  向量存储
     * @param status 状态
     * @return RAG 检索增强顾问
     */
    public static Advisor createPetAppRagCustomAdvisor(VectorStore vectorStore, String status){
        // 过滤特定状态的文档
        Filter.Expression expression = new FilterExpressionBuilder()
                .eq("status", status)
                .build();

        // 创建文档检索增强顾问
        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                // 设置过滤条件
                .filterExpression(expression)
                // 设置相似度阈值
                .similarityThreshold(0.5)
                // 设置返回文档数量
                .topK(3)
                .build();

        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                // AI 检索不到文档时，回复特定内容
                .queryAugmenter(PetAppContextualQueryAugmenterFactory.createInstance())
                .build();
    }
}
