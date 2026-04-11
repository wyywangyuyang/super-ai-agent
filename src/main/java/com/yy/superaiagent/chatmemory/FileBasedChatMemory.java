package com.yy.superaiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于文件持久化的对话记忆
 *
 * @author wyy
 */
public class FileBasedChatMemory implements ChatMemory {

    //文件存储路径
    private final String BASE_DIR;

    //初始化Kryo对象
    private static final Kryo kryo = new Kryo();

    //动态注册序列化的类
    static {
        //关闭手动注册
        kryo.setRegistrationRequired(false);
        //设置实例化策略
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    /**
     * 构造对象时，指定文件保存目录
     * @param dir 文件保存目录
     */
    public FileBasedChatMemory(String dir) {
        this.BASE_DIR = dir;
        File baseDir = new File(dir);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }


    /**
     * 添加消息
     *
     * @param conversationId 会话ID
     * @param message 消息
     */
    @Override
    public void add(String conversationId, Message message) {
        this.saveConversation(conversationId, List.of(message));
    }

    /**
     * 添加消息列表
     *
     * @param conversationId 会话ID
     * @param messages 消息列表
     */
    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> messageList = this.getOrCreateConversation(conversationId);
        messageList.addAll(messages);
        this.saveConversation(conversationId, messageList);
    }

    /**
     * 获取会话消息列表
     *
     * @param conversationId 会话ID
     * @return 会话消息列表
     */
    @Override
    public List<Message> get(String conversationId) {
        return this.getOrCreateConversation(conversationId);
    }

    /**
     * 清空会话消息列表
     *
     * @param conversationId 会话ID
     */
    @Override
    public void clear(String conversationId) {
        File file = this.getConversationFile(conversationId);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取或创建会话消息的列表
     *
     * @param conversationId 会话ID
     * @return 会话消息列表
     */
    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        List<Message> messages = new ArrayList<>();
        if (file.exists()) {
            try (Input input = new Input(new FileInputStream(file))) {
                messages = kryo.readObject(input, ArrayList.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }

    /**
     * 保存会话消息列表
     *
     * @param conversationId 会话ID
     * @param messages 会话消息列表
     */
    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 每个会话文件单独保存
     *
     * @param conversationId 会话ID
     * @return 会话文件
     */
    private File getConversationFile(String conversationId) {
        return new File(BASE_DIR, conversationId + ".kryo");
    }
}
