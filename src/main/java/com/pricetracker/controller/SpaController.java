package com.pricetracker.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Controller
public class SpaController {

    @GetMapping(value = {"/{path:^(?!api|actuator|assets).+$}", "/{path:^(?!api|actuator|assets).+$}/**"})
    @ResponseBody
    public String serveSpa() throws IOException {
        // Serve index.html for all SPA routes (non-API, non-assets)
        Resource indexHtml = new ClassPathResource("static/index.html");
        return new String(Files.readAllBytes(indexHtml.getFile().toPath()), StandardCharsets.UTF_8);
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String serveIndex() throws IOException {
        Resource indexHtml = new ClassPathResource("static/index.html");
        return new String(Files.readAllBytes(indexHtml.getFile().toPath()), StandardCharsets.UTF_8);
    }
}
