package com.yy.superaiagent.app;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.pgvector.autoconfigure.PgVectorStoreAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
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

    @Test
    void doChatWithTools() {
        // 测试联网搜索工具
        testMessage("周末想带我家金毛去公园玩，推荐几个适合狗狗活动的上海宠物友好场所？");
        // 测试网页抓取工具
        testMessage("最近我家猫咪总是乱抓沙发，看看其他铲屎官是怎么解决这个问题的？看看宠物网站（https://www.chongso.com/）能不能解决问题。");
        // 测试资源下载工具
        testMessage("直接下载一张适合做手机壁纸的可爱猫咪图片为文件");
        // 测试终端操作工具
        testMessage("执行 Python3 脚本来分析宠物的健康数据并生成报告");
        // 测试文件操作工具
        testMessage("保存我的宠物档案为文件，包含品种、年龄和疫苗接种记录");
        // 测试PDF生成工具
        testMessage("生成一份'新手养猫指南'PDF，包含日常护理、饮食建议和常见疾病预防");
    }

    private  void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = petApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        String message = "我的另一半居住在苏州相城区，请帮我找到 5 公里内合适的约会地点";
        String answer = petApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);

//        String message = "帮我搜索一些黄昏时刻的图片";
//        String answer =  petApp.doChatWithMcp(message, chatId);
//        Assertions.assertNotNull(answer);
//        log.info("answer：{}", answer);

    }
}