package com.yy.superaiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.yy.superaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 文件操作工具类（提供文件读写功能）
 *
 * @author wyy
 */
public class FileOperationTool {

    private static final String FILE_DIR  = FileConstant.FILE_SAVE_DIR + "/file";

    /**
     * 写文件
     *
     * @param fileName 文件名
     * @param content  内容
     * @return
     */
    @Tool(description = "Write content to a file")
    public String writeFile(
            @ToolParam(description = "Name of the file to write") String fileName,
            @ToolParam(description = "Content to write to the file") String content
    ) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            //创建文件夹
            FileUtil.mkdir(FILE_DIR);
            //写文件
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully to:" + filePath;
        } catch (Exception e) {
            return "Error writing to file:" + e.getMessage();
        }
    }

    /**
     * 读文件
     *
     * @param fileName 文件名
     * @return
     */
    @Tool(description = "Read content from a file")
    public String readFile(
            @ToolParam(description = "Name of the file to read") String fileName
    ) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading to file:" + e.getMessage();
        }
    }

}
