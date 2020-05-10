package com.runtimerevolution.urlshortener.service;

import com.runtimerevolution.urlshortener.dto.ShortenedUrlDTO;
import com.runtimerevolution.urlshortener.model.Url;
import com.runtimerevolution.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

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

    public UrlService() {
        super();
    }

    public UrlService(UrlRepository urlRepository,
                      ShortenerService shortenerService) {
        super();
        this.urlRepository = urlRepository;
        this.shortenerService = shortenerService;
    }

    /**
     * Given a short key, gets the URL information from DB
     * @param request the http request
     * @param shortKey the short key to find the URL
     * @return the shortened URL DTO
     * @throws IllegalArgumentException when the short key is not found
     */
    public ShortenedUrlDTO getShortenedUrlByShortKey(HttpServletRequest request, String shortKey) throws IllegalArgumentException {
        final Url url = urlRepository.findOneByShortKey(shortKey).orElseThrow(IllegalArgumentException::new);
        final String shortenedUrl = shortenerService.getShortenedUrl(request, url.getShortKey());

        return new ShortenedUrlDTO(url.getOriginalUrl(), shortenedUrl);
    }

    /**
     * Given a URL, shortens the URL and saves all information in DB
     * @param request the http request
     * @param urlToSave the URL to be saved
     * @return the shortened URL DTO
     */
    public ShortenedUrlDTO saveUrl(HttpServletRequest request, String urlToSave) {
        Url savedUrl = urlRepository.save(new Url(urlToSave));

        ShortenedUrlDTO savedUrlDto = new ShortenedUrlDTO();
        savedUrlDto.setUrl(urlToSave);

        String shortKey = shortenerService.generateShortKeyFromId(savedUrl.getId());
        savedUrl.setShortKey(shortKey);
        urlRepository.save(savedUrl);

        savedUrlDto.setShortenedUrl(shortenerService.getShortenedUrl(request, shortKey));

        return savedUrlDto;
    }
}
