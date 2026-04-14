package com.yy.superaiagent.rag;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * 创建上下文增强器的工厂
 *
 * @author wyy
 */
public class PetAppContextualQueryAugmenterFactory {

    public static ContextualQueryAugmenter createInstance() {
        PromptTemplate emptyContextPromptTemplate  = new PromptTemplate(
                """
                        你应该输出下面的内容：
                        抱歉，我只能回答宠物相关的问题，别的没办法帮到您哦，
                        有问题可以联系宠物咨询客服 https://docs.spring.io/spring-ai
                        """
        );

        return ContextualQueryAugmenter.builder()
                .emptyContextPromptTemplate(emptyContextPromptTemplate)
                .allowEmptyContext(false)
                .build();
            }

    }
