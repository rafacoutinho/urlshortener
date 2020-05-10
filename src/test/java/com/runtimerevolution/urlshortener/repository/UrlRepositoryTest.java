package com.runtimerevolution.urlshortener.repository;

import com.runtimerevolution.urlshortener.model.Url;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UrlRepositoryTest {

    private static final String VALIDATION_URL_BLANK = "{validation.url.blank}";
    private static final String VALIDATION_URL_INVALID = "{validation.url.invalid}";
    private static final String VALID_SHORTKEY = "AbCd1234";
    private static final String VALID_URL = "http://www.google.com/";

    @Autowired
    private UrlRepository urlRepository;

    @Test
    public void emptyDBWhenFindByIdThenReturnEmptyOptional() {
        final Optional<Url> foundUrlById = urlRepository.findById(1L);

        Assertions.assertFalse(foundUrlById.isPresent());
    }

    @Test
    public void emptyDBWhenFindOneByShortKeyThenReturnEmptyOptional() {
        final Optional<Url> foundUrlByShortKey = urlRepository.findOneByShortKey("empty");

        Assertions.assertFalse(foundUrlByShortKey.isPresent());
    }

    @Test
    public void saveNewBlankUrlValidationError() {
        final Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            final Url url = new Url();
            urlRepository.save(url);
        });

        Assertions.assertTrue(exception.getMessage().contains(VALIDATION_URL_BLANK));
    }

    @Test
    public void saveNewInvalidUrlValidationError() {
        final Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            final Url url = new Url();
            url.setOriginalUrl("invalid URL");
            urlRepository.save(url);
        });

        Assertions.assertTrue(exception.getMessage().contains(VALIDATION_URL_INVALID));
    }

    public void saveValidUrlSuccessfully() {
        Url url = new Url();
        url.setOriginalUrl(VALID_URL);
        url.setShortKey(VALID_SHORTKEY);
        url = urlRepository.save(url);

        final Optional<Url> dbUrl = urlRepository.findById(url.getId());

        Assertions.assertTrue(dbUrl.isPresent());
        Assertions.assertEquals(url.getId(), dbUrl.get().getId());
        Assertions.assertEquals(url.getShortKey(), dbUrl.get().getShortKey());
        Assertions.assertEquals(url.getOriginalUrl(), dbUrl.get().getOriginalUrl());
    }

    public void saveValidUrlSuccessfullyAndFindByShortKey() {
        Url url = new Url();
        url.setOriginalUrl(VALID_URL);
        url.setShortKey(VALID_SHORTKEY);
        url = urlRepository.save(url);

        final Optional<Url> dbUrl = urlRepository.findOneByShortKey(url.getShortKey());

        Assertions.assertTrue(dbUrl.isPresent());
        Assertions.assertEquals(url.getId(), dbUrl.get().getId());
        Assertions.assertEquals(url.getShortKey(), dbUrl.get().getShortKey());
        Assertions.assertEquals(url.getOriginalUrl(), dbUrl.get().getOriginalUrl());
    }

    @Test
    public void saveUrlUniqueShortKeyError() {
        final Exception exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            final Url url = new Url();
            url.setOriginalUrl(VALID_URL);
            url.setShortKey(VALID_SHORTKEY);
            urlRepository.save(url);

            final Url url2 = new Url();
            url2.setOriginalUrl(VALID_URL + "?");
            url2.setShortKey(VALID_SHORTKEY);
            urlRepository.save(url2);
        });

        Assertions.assertTrue(exception.getMessage().contains("constraint [\"PUBLIC.CONSTRAINT_INDEX_1 ON PUBLIC.URL(SHORT_KEY) VALUES 1\""));
    }
}
