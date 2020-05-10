package com.runtimerevolution.urlshortener.repository;

import com.runtimerevolution.urlshortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA repository for Url class
 */
public interface UrlRepository extends JpaRepository<Url, Long> {
    /**
     * Find Url by short key
     * @param shortKey the unique short key
     * @return the Url containing the short key
     */
    Optional<Url> findOneByShortKey(String shortKey);
}
