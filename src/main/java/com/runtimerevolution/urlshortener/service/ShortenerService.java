package com.runtimerevolution.urlshortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Date;

/**
 * Shortener service, containing the rules to create short keys
 * and get the full shortened URL
 */
@Component
public class ShortenerService {
    @Autowired
    private HttpServletRequest request;

    public String getShortKeyFromId(Long id) {
        final Long sourceId = id + new Date().getTime();
        final byte[] keyByteArray = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(sourceId).array();
        return Base64.getUrlEncoder().encodeToString(keyByteArray).replaceAll("=", "");
    }

    public String getShortenedUrl(String shortKey) {
        return String.format("%s://%s:%d/%s", request.getScheme(), request.getServerName(), request.getServerPort(), shortKey);
    }
}
