package com.yy.superaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "宠物咨询.pdf";
        String content = "宠物：猫、狗等";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}