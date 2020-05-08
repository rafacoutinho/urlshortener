package com.runtimerevolution.urlshortener.repository;

import com.runtimerevolution.urlshortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for Url class
 */
public interface UrlRepository extends JpaRepository<Url, Long> {
    /**
     * Find Url by short key
     * @param shortKey the unique short key
     * @return the Url containing the short key
     */
    Url findByShortKey(String shortKey);
}
