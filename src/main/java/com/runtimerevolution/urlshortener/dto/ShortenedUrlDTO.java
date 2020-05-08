package com.runtimerevolution.urlshortener.dto;

import java.io.Serializable;

/**
 * DTO for shortened URL
 */
public class ShortenedUrlDTO implements Serializable {
    private String url;
    private String shortenedUrl;

    public ShortenedUrlDTO() {
        super();
    }

    public ShortenedUrlDTO(String originalUrl, String shortenedUrl) {
        super();
        this.url = originalUrl;
        this.shortenedUrl = shortenedUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }
}
