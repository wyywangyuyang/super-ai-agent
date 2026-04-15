package com.yy.superaiagent.tools;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class WebScrapingToolTest {

    @Test
    void webScrapingTool() {

        WebScrapingTool webScrapingTool = new WebScrapingTool();
        String result = webScrapingTool.webScrapingTool("https://movie.douban.com/top250");
        assertNotNull(result);
        log.info("result: {}", result);
    }

}