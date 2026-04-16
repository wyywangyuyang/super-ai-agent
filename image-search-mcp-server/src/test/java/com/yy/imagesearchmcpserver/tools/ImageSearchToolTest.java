package com.yy.imagesearchmcpserver.tools;

import com.yy.imagesearchmcpserver.ImageSearchMcpServerApplication;
import com.yy.imagesearchmcpserver.tools.ImageSearchTool;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImageSearchToolTest {

    @Resource
    private ImageSearchTool imageSearchTool;

    @Test
    void searchImage() {

        String result = imageSearchTool.searchImage("cat");
        Assertions.assertNotNull(result);
    }

    @Test
    void searchMediumImages() {
    }
}