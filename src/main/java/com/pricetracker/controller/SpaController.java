package com.pricetracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {
    @GetMapping(value = "/{path:^(?!api).+$}")
    public String forward() {
        return "forward:/index.html";
    }

    @GetMapping(value = "/{path:^(?!api).+$}/{subpath:^(?!api).+$}")
    public String forwardSubpath() {
        return "forward:/index.html";
    }
}
