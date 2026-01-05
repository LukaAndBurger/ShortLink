package com.shortlink.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

/**
 * 短链接生成工具类
 * 提供多种短链接生成算法
 */
public class ShortLinkUtil {
    
    // 字符集：包含数字、小写字母、大写字母
    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SHORT_LINK_LENGTH = 6; // 短链接长度
    private static final Random RANDOM = new Random();
    
    /**
     * 使用MD5哈希算法生成短链接
     * @param originalUrl 原始URL
     * @return 短链接字符串
     */
    public static String generateShortLinkByMD5(String originalUrl) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(originalUrl.getBytes());
            
            // 将MD5哈希值转换为Base64编码，然后取前6位
            String base64 = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
            
            // 移除Base64中的特殊字符，只保留字母和数字
            String filtered = base64.replaceAll("[^a-zA-Z0-9]", "");
            
            // 如果过滤后的字符串长度不足，用随机字符补充
            if (filtered.length() < SHORT_LINK_LENGTH) {
                return filtered + generateRandomString(SHORT_LINK_LENGTH - filtered.length());
            }
            
            return filtered.substring(0, SHORT_LINK_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            // 如果MD5不可用，使用随机字符串
            return generateRandomShortLink();
        }
    }
    
    /**
     * 使用简单哈希算法生成短链接
     * @param originalUrl 原始URL
     * @return 短链接字符串
     */
    public static String generateShortLinkByHash(String originalUrl) {
        int hash = originalUrl.hashCode();
        
        // 确保哈希值为正数
        hash = hash & 0x7FFFFFFF;
        
        StringBuilder shortLink = new StringBuilder();
        
        // 将哈希值转换为62进制
        int temp = hash;
        for (int i = 0; i < SHORT_LINK_LENGTH; i++) {
            int index = temp % CHARACTERS.length();
            shortLink.append(CHARACTERS.charAt(index));
            temp = temp / CHARACTERS.length();
        }
        
        return shortLink.toString();
    }
    
    /**
     * 生成随机短链接
     * @return 随机短链接字符串
     */
    public static String generateRandomShortLink() {
        return generateRandomString(SHORT_LINK_LENGTH);
    }
    
    /**
     * 生成指定长度的随机字符串
     * @param length 字符串长度
     * @return 随机字符串
     */
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
    
    /**
     * 验证短链接格式是否有效
     * @param shortLink 短链接
     * @return 是否有效
     */
    public static boolean isValidShortLink(String shortLink) {
        if (shortLink == null || shortLink.length() != SHORT_LINK_LENGTH) {
            return false;
        }
        
        // 检查是否只包含允许的字符
        for (char c : shortLink.toCharArray()) {
            if (CHARACTERS.indexOf(c) == -1) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 生成带时间戳的短链接（避免冲突）
     * @param originalUrl 原始URL
     * @return 短链接字符串
     */
    public static String generateShortLinkWithTimestamp(String originalUrl) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String combined = originalUrl + timestamp;
        return generateShortLinkByMD5(combined);
    }
    
    /**
     * 生成自定义长度的短链接
     * @param originalUrl 原始URL
     * @param length 短链接长度
     * @return 短链接字符串
     */
    public static String generateCustomLengthShortLink(String originalUrl, int length) {
        if (length <= 0) {
            length = SHORT_LINK_LENGTH;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(originalUrl.getBytes());
            
            String base64 = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
            String filtered = base64.replaceAll("[^a-zA-Z0-9]", "");
            
            if (filtered.length() < length) {
                return filtered + generateRandomString(length - filtered.length());
            }
            
            return filtered.substring(0, length);
        } catch (NoSuchAlgorithmException e) {
            return generateRandomString(length);
        }
    }
}