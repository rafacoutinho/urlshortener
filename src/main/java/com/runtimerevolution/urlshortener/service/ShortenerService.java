package com.runtimerevolution.urlshortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Component
public class ShortenerService {
    @Autowired
    private HttpServletRequest request;

    public String getShortKeyFromId(Long id) {
        final String firstKey = String.join("", id.toString(), new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        final byte[] keyByteArray = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(Long.parseLong(firstKey)).array();
        return Base64.getUrlEncoder().encodeToString(keyByteArray).replaceAll("=", "");
    }

    public String getShortenedUrl(String shortKey) {
        return String.format("%s://%s:%d/%s", request.getScheme(), request.getServerName(), request.getServerPort(), shortKey);
    }
}
