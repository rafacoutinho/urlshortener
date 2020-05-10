package com.runtimerevolution.urlshortener.model;

import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * URL persistence entity
 */
@Entity
@Table(name = "url")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "original_url", nullable = false)
    @NotBlank(message = "{validation.url.blank}")
    @URL(message = "{validation.url.invalid}")
    private String originalUrl;

    @Column(name = "short_key", unique = true)
    private String shortKey;

    public Url() {
        super();
    }

    public Url(String originalUrl) {
        super();
        this.setOriginalUrl(originalUrl);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortKey() {
        return shortKey;
    }

    public void setShortKey(String shortKey) {
        this.shortKey = shortKey;
    }
}
