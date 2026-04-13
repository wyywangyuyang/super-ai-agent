package com.yy.superaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.pgvector.autoconfigure.PgVectorStoreAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoConfiguration(exclude = {PgVectorStoreAutoConfiguration.class})
@SpringBootTest
class PetAppTest {

    @Resource
    private PetApp petApp;

    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        //第一轮
        String message = "你好，我是程序员菜狗";
        String answer = petApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        //第二轮
        message = "我想让咨询一下猫咪的行为问题";
        answer = petApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        //第三轮
        message = "我想咨询的动物是什么来着？刚跟你说过，帮我回忆一下";
        answer = petApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);

    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好，我是程序员菜狗,我想让我的猫咪更爱我，但我不知道该怎么做";
        PetApp.PetReport loveReport = petApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "刚接回家的幼宠（猫 / 狗）需要做哪些准备和注意事项？";
        PetApp.PetReport loveReport = petApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }
}