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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Controller for the URL Shortener
 */
@Controller
public class UrlShortenerController {
    Logger logger = LoggerFactory.getLogger(UrlShortenerController.class);

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
    public String formSubmit(Model model,
                             @ModelAttribute @Valid ShortenedUrlDTO shortenedUrl,
                             BindingResult bindingResult) {
        model.addAttribute("appName", appName);
        if (bindingResult.hasErrors()) {
            model.addAttribute("shortenedUrl", shortenedUrl);
            model.addAttribute("errorMessage", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "home";
        }
        shortenedUrl = urlService.saveUrl(shortenedUrl.getUrl());
        model.addAttribute("shortenedUrl", shortenedUrl);
        return "result";
    }

    @GetMapping("/{shortKey}")
    public ModelAndView redirectToShortenedUrl(Model model, @PathVariable String shortKey) {
        try {
            ShortenedUrlDTO shortenedUrlDTO = urlService.getShortenedUrlByShortKey(shortKey);
            return new ModelAndView("redirect:" + shortenedUrlDTO.getUrl());
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid URL", ex);
            model.addAttribute("appName", appName);
            return new ModelAndView("error");
        }
    }
}
