package com.runtimerevolution.urlshortener.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class ShortenerServiceTest {

    private static final long VALID_ID = 1000L;
    private ShortenerService shortenerService;

    @BeforeEach
    void setUp() {
        shortenerService = new ShortenerService();
    }

    @Test
    public void generateShortKeyWithMaxLong() {
        final String shortKey = shortenerService.generateShortKeyFromId(Long.MAX_VALUE);

        Assertions.assertNotNull(shortKey);
    }

    @Test
    public void getShortenedUrlSuccessfully() {
        final String shortKey = shortenerService.generateShortKeyFromId(VALID_ID);
        final String url = shortenerService.getShortenedUrl(new MockHttpServletRequest(), shortKey);

        Assertions.assertNotNull(shortKey);
        Assertions.assertNotNull(url);
        Assertions.assertTrue(url.contains("http://"));
        Assertions.assertTrue(url.contains(shortKey));
        Assertions.assertFalse(url.contains(":80/"));
    }
}
