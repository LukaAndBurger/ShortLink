package com.shortlink.model;

import java.time.LocalDateTime;

/**
 * 短链接模型类
 */
public class ShortLink {
    
    private String id;
    private String originalUrl;
    private String shortCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private int clickCount;
    private String createdBy;
    private String algorithm;
    private boolean isActive;
    
    // 默认构造函数
    public ShortLink() {
        this.createdAt = LocalDateTime.now();
        this.clickCount = 0;
        this.isActive = true;
    }
    
    // 带参数的构造函数
    public ShortLink(String originalUrl, String shortCode) {
        this();
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
    }
    
    // 带算法参数的构造函数
    public ShortLink(String originalUrl, String shortCode, String algorithm) {
        this(originalUrl, shortCode);
        this.algorithm = algorithm;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getOriginalUrl() {
        return originalUrl;
    }
    
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
    
    public String getShortCode() {
        return shortCode;
    }
    
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public int getClickCount() {
        return clickCount;
    }
    
    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }
    
    public void incrementClickCount() {
        this.clickCount++;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getAlgorithm() {
        return algorithm;
    }
    
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    /**
     * 检查短链接是否已过期
     * @return 是否已过期
     */
    public boolean isExpired() {
        if (expiresAt == null) {
            return false; // 永不过期
        }
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    /**
     * 检查短链接是否有效（未过期且活跃）
     * @return 是否有效
     */
    public boolean isValid() {
        return isActive && !isExpired();
    }
    
    /**
     * 设置过期时间（从当前时间开始的天数）
     * @param days 天数
     */
    public void setExpiresInDays(int days) {
        if (days <= 0) {
            this.expiresAt = null; // 永不过期
        } else {
            this.expiresAt = LocalDateTime.now().plusDays(days);
        }
    }
    
    /**
     * 获取完整的短链接URL
     * @param baseUrl 基础URL（如：https://short.link/）
     * @return 完整的短链接URL
     */
    public String getFullShortUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            return shortCode;
        }
        
        // 确保baseUrl以斜杠结尾
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        
        return baseUrl + shortCode;
    }
    
    @Override
    public String toString() {
        return "ShortLink{" +
                "id='" + id + '\'' +
                ", originalUrl='" + originalUrl + '\'' +
                ", shortCode='" + shortCode + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", clickCount=" + clickCount +
                ", createdBy='" + createdBy + '\'' +
                ", algorithm='" + algorithm + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}