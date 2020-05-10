package com.runtimerevolution.urlshortener.service;

import com.runtimerevolution.urlshortener.dto.ShortenedUrlDTO;
import com.runtimerevolution.urlshortener.model.Url;
import com.runtimerevolution.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

    private static final String SHORT_KEY_NOT_FOUND = "INVALIDKEY";
    private static final String VALID_SHORT_KEY = "AbCd1234";
    private static final String SHORT_URL = "http://localhost/" + VALID_SHORT_KEY;
    private static final String VALID_URL = "http://www.google.com/";

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public UrlService urlService() {
            return new UrlService();
        }
    }

    private UrlService urlService;

    private MockHttpServletRequest mockHttpServletRequest;

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private ShortenerService shortenerService;

    @BeforeEach
    void setUp() {
        urlService = new UrlService(urlRepository, shortenerService);
        mockHttpServletRequest = new MockHttpServletRequest();
    }

    private static Url getValidUrl() {
        Url url = new Url(VALID_URL);
        url.setId(1L);
        url.setShortKey(VALID_SHORT_KEY);
        return url;
    }

    @Test
    public void getShortenedUrlByShortKeySuccessfully() {
        when(urlRepository.findOneByShortKey(VALID_SHORT_KEY))
                .thenReturn(Optional.of(getValidUrl()));
        when(shortenerService.getShortenedUrl(any(HttpServletRequest.class), anyString()))
                .thenReturn(SHORT_URL);

        ShortenedUrlDTO shortenedUrlDTO = urlService.getShortenedUrlByShortKey(mockHttpServletRequest, VALID_SHORT_KEY);

        Assertions.assertNotNull(shortenedUrlDTO);
        Assertions.assertEquals(shortenedUrlDTO.getUrl(), getValidUrl().getOriginalUrl());
        Assertions.assertEquals(shortenedUrlDTO.getShortenedUrl(), SHORT_URL);
    }

    @Test
    public void getShortenedUrlByShortKeyNotFoundError() {
        when(urlRepository.findOneByShortKey(SHORT_KEY_NOT_FOUND))
                .thenReturn(Optional.empty());

        final Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            urlService.getShortenedUrlByShortKey(mockHttpServletRequest, SHORT_KEY_NOT_FOUND);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertNull(exception.getMessage());
    }

    @Test
    public void saveUrlSuccessfully() {
        when(urlRepository.save(any(Url.class)))
                .thenReturn(getValidUrl());
        when(shortenerService.generateShortKeyFromId(anyLong()))
                .thenReturn(VALID_SHORT_KEY);
        when(shortenerService.getShortenedUrl(any(HttpServletRequest.class), anyString()))
                .thenReturn(SHORT_URL);

        ShortenedUrlDTO shortenedUrlDTO = urlService.saveUrl(mockHttpServletRequest, VALID_URL);

        Assertions.assertNotNull(shortenedUrlDTO);
        Assertions.assertEquals(shortenedUrlDTO.getUrl(), getValidUrl().getOriginalUrl());
        Assertions.assertEquals(shortenedUrlDTO.getShortenedUrl(), SHORT_URL);
    }
}
