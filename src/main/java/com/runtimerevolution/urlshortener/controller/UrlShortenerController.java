package com.runtimerevolution.urlshortener.controller;

import com.runtimerevolution.urlshortener.dto.ShortenedUrlDTO;
import com.runtimerevolution.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UrlShortenerController {
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
    public String formSubmit(Model model, @ModelAttribute ShortenedUrlDTO shortenedUrl) {
        model.addAttribute("appName", appName);
        shortenedUrl = urlService.saveUrl(shortenedUrl.getUrl());
        model.addAttribute("shortenedUrl", shortenedUrl);
        return "result";
    }

    @GetMapping("/{shortKey}")
    public ModelAndView redirectToShortenedUrl(@PathVariable String shortKey) {
        ShortenedUrlDTO shortenedUrlDTO = urlService.getShortenedUrlByShortKey(shortKey);
        return new ModelAndView("redirect:" + shortenedUrlDTO.getUrl());
    }
}
