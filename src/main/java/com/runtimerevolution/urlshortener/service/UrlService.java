package com.runtimerevolution.urlshortener.service;

import com.runtimerevolution.urlshortener.dto.ShortenedUrlDTO;
import com.runtimerevolution.urlshortener.entity.Url;
import com.runtimerevolution.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for Url
 */
@Component
@Transactional(propagation = Propagation.REQUIRED)
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private ShortenerService shortenerService;

    public ShortenedUrlDTO getShortenedUrlByShortKey(String shortKey) {
        final Url url = urlRepository.findByShortKey(shortKey);
        final String shortenedUrl = shortenerService.getShortenedUrl(url.getShortKey());

        return new ShortenedUrlDTO(url.getOriginalUrl(), shortenedUrl);
    }

    public ShortenedUrlDTO saveUrl(String urlToSave) {
        Url savedUrl = urlRepository.save(new Url(urlToSave));

        ShortenedUrlDTO savedUrlDto = new ShortenedUrlDTO();
        savedUrlDto.setUrl(urlToSave);

        String shortKey = shortenerService.getShortKeyFromId(savedUrl.getId());
        savedUrl.setShortKey(shortKey);
        urlRepository.save(savedUrl);

        savedUrlDto.setShortenedUrl(shortenerService.getShortenedUrl(shortKey));

        return savedUrlDto;
    }
}
