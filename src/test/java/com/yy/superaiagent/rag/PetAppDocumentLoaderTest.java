package com.yy.superaiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PetAppDocumentLoaderTest {

    @Resource
    private PetAppDocumentLoader petAppDocumentLoader;

    @Test
    void loadMarkDowns() {
        petAppDocumentLoader.loadMarkdowns();
    }
}