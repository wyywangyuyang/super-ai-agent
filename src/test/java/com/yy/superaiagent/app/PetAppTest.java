package com.yy.superaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
}