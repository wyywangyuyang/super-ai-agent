package com.yy.superaiagent.agent;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ManusTest {

    @Resource
    private Manus manus;

    @Test
    public void run() {
        String userPrompt = """
                请为我制定一个详细的约会计划：
                1. 我的另一半居住在苏州相城区，请查找该区域5公里范围内的约会场所
                2. 包括但不限于餐厅、咖啡馆、公园、电影院、博物馆等适合情侣的场所
                3. 为每个推荐的场所提供相关的网络图片参考，并将图片下载下来
                4. 制定一份完整的约会行程安排，包括时间、地点、活动内容
                5. 最后将完整的约会计划用中文以PDF格式输出，文件名为"yuehui.pdf"
                """;
        String answer = manus.run(userPrompt);
        assertNotNull(answer);
        log.info(answer);
    }


}