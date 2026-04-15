package com.yy.superaiagent.tools;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class WebSearchToolTest {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Test
    void search() {
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        String query = "帝皇铠甲";
        String search = webSearchTool.searchWeb(query);
        assertNotNull(search);
        log.info("search: {}", search);
    }

}