package com.yy.superaiagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动类
 *
 * @author wyy
 */
@SpringBootApplication(exclude = {
        // 为了便于大家开发调试和部署，取消数据库自动配置，需要使用 PgVector 时把 DataSourceAutoConfiguration.class 删除
        DataSourceAutoConfiguration.class,
        // 排除 PgVector 自动配置，避免与 PetAppVectorStoreConfig 冲突
        org.springframework.ai.vectorstore.pgvector.autoconfigure.PgVectorStoreAutoConfiguration.class
})
public class SuperAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuperAiAgentApplication.class, args);
    }

}
