package com.yy.superaiagent.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 联网搜索工具类（根据关键词搜索网页列表）
 *
 * @author wyy
 */
public class WebSearchTool {

    // 搜索API的URL地址
    private static final String SEARCH_API_URL = "https://www.searchapi.io/api/v1/search";

    // API密钥，用于身份验证
    private final String apiKey;

    // 构造函数，初始化API密钥
    public WebSearchTool(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * 从百度搜索引擎搜索信息
     *
     * @param query 搜索查询关键词
     * @return 搜索结果的JSON字符串表示
     */
    @Tool(description = "Search for information from Baidu Search Engine")
    public String searchWeb(
            @ToolParam(description = "Search query keyword") String query) {
        // 创建参数映射表
        Map<String, Object> paramMap = new HashMap<>();
        // 设置搜索关键词
        paramMap.put("q", query);
        // 设置API密钥
        paramMap.put("api_key", apiKey);
        // 设置搜索引擎为百度
        paramMap.put("engine", "baidu");

        try {
            // 发送GET请求到搜索API并获取响应
            String response = HttpUtil.get(SEARCH_API_URL, paramMap);

            // 将响应解析为JSON对象
            JSONObject jsonObject = JSONUtil.parseObj(response);

            // 获取搜索结果中的有机结果部分
            JSONArray organicResults = jsonObject.getJSONArray("organic_results");
            // 取前5个搜索结果
            List<Object> objects = organicResults.subList(0, 5);

            // 将每个搜索结果转换为字符串并用逗号连接
            String result = objects.stream().map(obj -> {
                JSONObject tmpJSONObject = (JSONObject) obj;
                return tmpJSONObject.toString();
            }).collect(Collectors.joining(","));

            return result;
        } catch (Exception e) {
            // 如果发生异常，返回错误信息
            return "Error searching Baidu: " + e.getMessage();
        }
    }
}
