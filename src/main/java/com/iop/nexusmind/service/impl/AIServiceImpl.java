package com.iop.nexusmind.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iop.nexusmind.service.AIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    @Value("${ai.qwen.api-key}")
    private String apiKey;

    @Value("${ai.qwen.api-url}")
    private String apiUrl;

    @Value("${ai.qwen.model}")
    private String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public String generateSummary(String content) {
        try {
            String prompt = "请用 100 字以内总结以下内容：\n\n" + content;
            return callQwenAPI(prompt);
        } catch (Exception e) {
            log.error("生成摘要失败：{}", e.getMessage());
            return "摘要生成失败";
        }
    }

    @Override
    public String chat(String message) {
        return callQwenAPI(message);
    }

    private String callQwenAPI(String input) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);

            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", input);
            requestBody.put("input", Map.of("messages", List.of(message)));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            
            if (response.getBody() != null) {
                Map<String, Object> output = (Map<String, Object>) response.getBody().get("output");
                if (output != null && output.containsKey("text")) {
                    return (String) output.get("text");
                }
            }
            
            log.warn("AI API 返回空响应");
            return "AI 响应失败";
        } catch (Exception e) {
            log.error("调用 AI API 失败：{}", e.getMessage(), e);
            return "AI 服务调用失败：" + e.getMessage();
        }
    }
}
