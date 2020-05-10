package com.runtimerevolution.urlshortener.controller;

import com.runtimerevolution.urlshortener.dto.ShortenedUrlDTO;
import com.runtimerevolution.urlshortener.service.UrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

/**
 * Controller for the URL Shortener
 */
@Controller
public class UrlShortenerController {
    private final Logger logger = LoggerFactory.getLogger(UrlShortenerController.class);

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private UrlService urlService;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("shortenedUrl", new ShortenedUrlDTO());
        return "home";
    }

    @PostMapping("/")
    public String formSubmit(HttpServletRequest request,
                             Model model,
                             @ModelAttribute @Valid ShortenedUrlDTO shortenedUrl,
                             BindingResult bindingResult) {
        String errorMessage;
        model.addAttribute("appName", appName);

        if (bindingResult.hasErrors()) {
            return formSubmitError(model, shortenedUrl, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        try {
            shortenedUrl = urlService.saveUrl(request, shortenedUrl.getUrl().trim());
            model.addAttribute("shortenedUrl", shortenedUrl);
            return "result";
        } catch (ConstraintViolationException cvex) {
            logger.error("Bean Validation Exception", cvex);
            errorMessage = cvex.getMessage();
        } catch (Exception ex) {
            logger.error("Generic Exception", ex);
            errorMessage = "Unknown error";
        }

        return formSubmitError(model, shortenedUrl, errorMessage);
    }

    private String formSubmitError(Model model, ShortenedUrlDTO shortenedUrl, String errorMessage) {
        model.addAttribute("shortenedUrl", shortenedUrl);
        model.addAttribute("errorMessage", errorMessage);
        return "home";
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

    @GetMapping("/{shortKey}")
    public ModelAndView redirectToShortenedUrl(HttpServletRequest request,
                                               Model model,
                                               @PathVariable String shortKey) {
        try {
            ShortenedUrlDTO shortenedUrlDTO = urlService.getShortenedUrlByShortKey(request, shortKey);
            return new ModelAndView("redirect:" + shortenedUrlDTO.getUrl());
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid URL", ex);
            model.addAttribute("appName", appName);
            return new ModelAndView("error");
        }
    }
}
