package com.yy.superaiagent.tools;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class FileOperationToolTest {

    @Test
    void writeFile() {
        String writtenFile = new FileOperationTool().writeFile("test.txt", "hello world");
        assertNotNull(writtenFile);
    }

    @Test
    void readFile() {
        String readFile = new FileOperationTool().readFile("test.txt");
        assertNotNull(readFile);
        log.info("readFile: {}", readFile);
    }
}