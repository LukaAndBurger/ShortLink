package com.shortlink.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 短链接工具类测试
 */
@SpringBootTest
class ShortLinkUtilTest {

    @Test
    void testGenerateShortLinkByMD5() {
        String url = "https://www.example.com/path/to/resource?param=value";
        
        String shortLink = ShortLinkUtil.generateShortLinkByMD5(url);
        
        assertNotNull(shortLink);
        assertEquals(6, shortLink.length());
        assertTrue(ShortLinkUtil.isValidShortLink(shortLink));
        
        // 相同URL应该生成相同的短链接
        String shortLink2 = ShortLinkUtil.generateShortLinkByMD5(url);
        assertEquals(shortLink, shortLink2);
        
        // 不同URL应该生成不同的短链接
        String differentUrl = "https://www.example.com/different/path";
        String differentShortLink = ShortLinkUtil.generateShortLinkByMD5(differentUrl);
        assertNotEquals(shortLink, differentShortLink);
    }

    @Test
    void testGenerateShortLinkByHash() {
        String url = "https://www.example.com/path/to/resource";
        
        String shortLink = ShortLinkUtil.generateShortLinkByHash(url);
        
        assertNotNull(shortLink);
        assertEquals(6, shortLink.length());
        assertTrue(ShortLinkUtil.isValidShortLink(shortLink));
        
        // 相同URL应该生成相同的短链接
        String shortLink2 = ShortLinkUtil.generateShortLinkByHash(url);
        assertEquals(shortLink, shortLink2);
    }

    @Test
    void testGenerateRandomShortLink() {
        String shortLink = ShortLinkUtil.generateRandomShortLink();
        
        assertNotNull(shortLink);
        assertEquals(6, shortLink.length());
        assertTrue(ShortLinkUtil.isValidShortLink(shortLink));
        
        // 两次随机生成应该不同（极低概率相同）
        String shortLink2 = ShortLinkUtil.generateRandomShortLink();
        // 注意：理论上有可能相同，但概率极低
        // assertNotEquals(shortLink, shortLink2);
    }

    @Test
    void testIsValidShortLink() {
        // 有效短链接
        assertTrue(ShortLinkUtil.isValidShortLink("abc123"));
        assertTrue(ShortLinkUtil.isValidShortLink("ABCdef"));
        assertTrue(ShortLinkUtil.isValidShortLink("123456"));
        assertTrue(ShortLinkUtil.isValidShortLink("aBcDeF"));
        
        // 无效短链接
        assertFalse(ShortLinkUtil.isValidShortLink(null));
        assertFalse(ShortLinkUtil.isValidShortLink(""));
        assertFalse(ShortLinkUtil.isValidShortLink("abc12")); // 长度不足
        assertFalse(ShortLinkUtil.isValidShortLink("abc1234")); // 长度过长
        assertFalse(ShortLinkUtil.isValidShortLink("abc-12")); // 包含非法字符
        assertFalse(ShortLinkUtil.isValidShortLink("abc 12")); // 包含空格
        assertFalse(ShortLinkUtil.isValidShortLink("abc@12")); // 包含特殊字符
    }

    @Test
    void testGenerateShortLinkWithTimestamp() {
        String url = "https://www.example.com/same/url";
        
        String shortLink1 = ShortLinkUtil.generateShortLinkWithTimestamp(url);
        
        // 等待一小段时间，确保时间戳不同
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String shortLink2 = ShortLinkUtil.generateShortLinkWithTimestamp(url);
        
        assertNotNull(shortLink1);
        assertNotNull(shortLink2);
        assertEquals(6, shortLink1.length());
        assertEquals(6, shortLink2.length());
        
        // 由于时间戳不同，生成的短链接应该不同
        assertNotEquals(shortLink1, shortLink2);
    }

    @Test
    void testGenerateCustomLengthShortLink() {
        String url = "https://www.example.com/custom/length";
        
        // 测试不同长度
        String shortLink6 = ShortLinkUtil.generateCustomLengthShortLink(url, 6);
        String shortLink8 = ShortLinkUtil.generateCustomLengthShortLink(url, 8);
        String shortLink10 = ShortLinkUtil.generateCustomLengthShortLink(url, 10);
        
        assertNotNull(shortLink6);
        assertNotNull(shortLink8);
        assertNotNull(shortLink10);
        
        assertEquals(6, shortLink6.length());
        assertEquals(8, shortLink8.length());
        assertEquals(10, shortLink10.length());
        
        // 测试无效长度（应该使用默认长度）
        String defaultLength = ShortLinkUtil.generateCustomLengthShortLink(url, 0);
        assertEquals(6, defaultLength.length());
        
        String negativeLength = ShortLinkUtil.generateCustomLengthShortLink(url, -5);
        assertEquals(6, negativeLength.length());
    }

    @Test
    void testEdgeCases() {
        // 空字符串
        String emptyUrl = "";
        String shortLinkEmpty = ShortLinkUtil.generateShortLinkByMD5(emptyUrl);
        assertNotNull(shortLinkEmpty);
        assertEquals(6, shortLinkEmpty.length());
        
        // 非常长的URL
        StringBuilder longUrlBuilder = new StringBuilder("https://www.example.com/");
        for (int i = 0; i < 1000; i++) {
            longUrlBuilder.append("very/long/path/");
        }
        String longUrl = longUrlBuilder.toString();
        String shortLinkLong = ShortLinkUtil.generateShortLinkByMD5(longUrl);
        assertNotNull(shortLinkLong);
        assertEquals(6, shortLinkLong.length());
        
        // 特殊字符URL
        String specialUrl = "https://www.example.com/path?param=值&特殊=字符";
        String shortLinkSpecial = ShortLinkUtil.generateShortLinkByMD5(specialUrl);
        assertNotNull(shortLinkSpecial);
        assertEquals(6, shortLinkSpecial.length());
    }

    @Test
    void testPerformance() {
        int iterations = 1000;
        String url = "https://www.example.com/performance/test";
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < iterations; i++) {
            ShortLinkUtil.generateShortLinkByMD5(url + i);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // 1000次生成应该在合理时间内完成
        assertTrue(duration < 5000, "生成1000个短链接耗时过长: " + duration + "ms");
        
        System.out.println("生成 " + iterations + " 个短链接耗时: " + duration + "ms");
        System.out.println("平均每个短链接耗时: " + (duration / (double) iterations) + "ms");
    }

    @Test
    void testCollisionRate() {
        // 测试碰撞率（简单测试）
        int total = 1000;
        String baseUrl = "https://www.example.com/test/";
        
        java.util.Set<String> generatedLinks = new java.util.HashSet<>();
        
        for (int i = 0; i < total; i++) {
            String url = baseUrl + i;
            String shortLink = ShortLinkUtil.generateShortLinkByMD5(url);
            generatedLinks.add(shortLink);
        }
        
        // 期望碰撞率很低
        double collisionRate = 1.0 - (generatedLinks.size() / (double) total);
        assertTrue(collisionRate < 0.01, "碰撞率过高: " + collisionRate);
        
        System.out.println("生成 " + total + " 个短链接，唯一数量: " + generatedLinks.size());
        System.out.println("碰撞率: " + (collisionRate * 100) + "%");
    }
}