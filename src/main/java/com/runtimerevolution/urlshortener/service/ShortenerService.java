package com.runtimerevolution.urlshortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Date;

/**
 * Shortener service, containing the rules to generate short keys
 * and get the full shortened URL
 */
@Component
public class ShortenerService {

    /**
     * Given an URL ID, generates a new unique short key for the URL
     * @param id the URL ID in DB
     * @return the generated short key
     */
    public String generateShortKeyFromId(Long id) {
        final Long sourceId = id + new Date().getTime();
        final byte[] keyByteArray = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(sourceId).array();
        return Base64.getUrlEncoder().encodeToString(keyByteArray).replaceAll("=", "");
    }

    /**
     * Given a short key, returns the full shortened URL
     * @param request the http request
     * @param shortKey the short key
     * @return the full shortened URL
     */
    public String getShortenedUrl(HttpServletRequest request, String shortKey) {
        return String.format("%s://%s:%d/%s", request.getScheme(), request.getServerName(), request.getServerPort(), shortKey)
                .replace(":80/", "/"); // replace port in URL if is the default (80)
    }
}
