package com.shortlink.controller;

import com.shortlink.util.ShortLinkUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 短链接生成控制器
 * 提供短链接生成和验证的API接口
 */
@RestController
@RequestMapping("/api/shortlink")
public class ShortLinkController {

    /**
     * 生成短链接
     * @param request 包含原始URL的请求体
     * @return 生成的短链接信息
     */
    @PostMapping("/generate")
    public Map<String, Object> generateShortLink(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("url");
        
        if (originalUrl == null || originalUrl.trim().isEmpty()) {
            return createErrorResponse("URL不能为空");
        }
        
        // 使用MD5算法生成短链接
        String shortLink = ShortLinkUtil.generateShortLinkByMD5(originalUrl);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("originalUrl", originalUrl);
        response.put("shortLink", shortLink);
        response.put("algorithm", "MD5");
        response.put("timestamp", System.currentTimeMillis());
        
        return response;
    }

    /**
     * 批量生成短链接
     * @param request 包含URL列表的请求体
     * @return 批量生成的短链接信息
     */
    @PostMapping("/batch-generate")
    public Map<String, Object> batchGenerateShortLinks(@RequestBody Map<String, Object> request) {
        java.util.List<String> urls = (java.util.List<String>) request.get("urls");
        
        if (urls == null || urls.isEmpty()) {
            return createErrorResponse("URL列表不能为空");
        }
        
        java.util.List<Map<String, String>> results = new java.util.ArrayList<>();
        
        for (String url : urls) {
            String shortLink = ShortLinkUtil.generateShortLinkByMD5(url);
            
            Map<String, String> result = new HashMap<>();
            result.put("originalUrl", url);
            result.put("shortLink", shortLink);
            results.add(result);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", results.size());
        response.put("results", results);
        response.put("timestamp", System.currentTimeMillis());
        
        return response;
    }

    /**
     * 使用不同算法生成短链接
     * @param request 包含原始URL和算法类型的请求体
     * @return 使用不同算法生成的短链接
     */
    @PostMapping("/generate-with-algorithm")
    public Map<String, Object> generateWithAlgorithm(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("url");
        String algorithm = request.get("algorithm");
        
        if (originalUrl == null || originalUrl.trim().isEmpty()) {
            return createErrorResponse("URL不能为空");
        }
        
        String shortLink;
        String usedAlgorithm;
        
        if ("hash".equalsIgnoreCase(algorithm)) {
            shortLink = ShortLinkUtil.generateShortLinkByHash(originalUrl);
            usedAlgorithm = "Hash";
        } else if ("random".equalsIgnoreCase(algorithm)) {
            shortLink = ShortLinkUtil.generateRandomShortLink();
            usedAlgorithm = "Random";
        } else if ("timestamp".equalsIgnoreCase(algorithm)) {
            shortLink = ShortLinkUtil.generateShortLinkWithTimestamp(originalUrl);
            usedAlgorithm = "Timestamp";
        } else {
            // 默认使用MD5
            shortLink = ShortLinkUtil.generateShortLinkByMD5(originalUrl);
            usedAlgorithm = "MD5";
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("originalUrl", originalUrl);
        response.put("shortLink", shortLink);
        response.put("algorithm", usedAlgorithm);
        response.put("timestamp", System.currentTimeMillis());
        
        return response;
    }

    /**
     * 验证短链接格式
     * @param shortLink 短链接字符串
     * @return 验证结果
     */
    @GetMapping("/validate/{shortLink}")
    public Map<String, Object> validateShortLink(@PathVariable String shortLink) {
        boolean isValid = ShortLinkUtil.isValidShortLink(shortLink);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("shortLink", shortLink);
        response.put("isValid", isValid);
        
        if (isValid) {
            response.put("message", "短链接格式有效");
            response.put("length", shortLink.length());
        } else {
            response.put("message", "短链接格式无效");
        }
        
        return response;
    }

    /**
     * 生成自定义长度的短链接
     * @param request 包含原始URL和长度的请求体
     * @return 自定义长度的短链接
     */
    @PostMapping("/generate-custom-length")
    public Map<String, Object> generateCustomLength(@RequestBody Map<String, Object> request) {
        String originalUrl = (String) request.get("url");
        Integer length = (Integer) request.get("length");
        
        if (originalUrl == null || originalUrl.trim().isEmpty()) {
            return createErrorResponse("URL不能为空");
        }
        
        if (length == null || length <= 0) {
            length = 6; // 默认长度
        }
        
        String shortLink = ShortLinkUtil.generateCustomLengthShortLink(originalUrl, length);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("originalUrl", originalUrl);
        response.put("shortLink", shortLink);
        response.put("length", length);
        response.put("timestamp", System.currentTimeMillis());
        
        return response;
    }

    /**
     * 获取短链接生成统计信息
     * @return 统计信息
     */
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 这里可以添加实际的统计逻辑
        stats.put("totalGenerated", 0); // 实际项目中可以从数据库获取
        stats.put("supportedAlgorithms", java.util.Arrays.asList("MD5", "Hash", "Random", "Timestamp"));
        stats.put("defaultLength", 6);
        stats.put("characterSetSize", 62); // 26小写 + 26大写 + 10数字
        stats.put("possibleCombinations", Math.pow(62, 6)); // 62^6
        stats.put("timestamp", System.currentTimeMillis());
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("stats", stats);
        
        return response;
    }

    /**
     * 创建错误响应
     * @param message 错误信息
     * @return 错误响应
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}