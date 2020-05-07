package com.runtimerevolution.urlshortener.controller;

import com.runtimerevolution.urlshortener.model.ShortenedUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UrlShortenerController {
    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("shortenedUrl", new ShortenedUrl());
        return "home";
    }

    @PostMapping("/")
    public String formSubmit(Model model, @ModelAttribute ShortenedUrl shortenedUrl) {
        model.addAttribute("appName", appName);
        //TODO Shorten the URL and set it to the model
        shortenedUrl.setShortenedUrl("TEST");
        return "result";
    }

    @GetMapping("/{urlId}")
    public String redirectToShortenedUrl(@PathVariable String urlId) {
        // TODO Get the unshortened URL by ID and redirect
        return "home";
    }
}
