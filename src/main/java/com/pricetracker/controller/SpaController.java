package com.pricetracker.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class SpaController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public Resource serveIndex() throws IOException {
        return new ClassPathResource("static/index.html");
    }

    @GetMapping(value = "/{path:^(?!api|actuator|assets).+$}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public Resource serveSpaRoute() throws IOException {
        return new ClassPathResource("static/index.html");
    }

    @GetMapping(value = "/{path:^(?!api|actuator|assets).+$}/**", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public Resource spaNestedRoute() throws IOException {
        return new ClassPathResource("static/index.html");
    }
}
