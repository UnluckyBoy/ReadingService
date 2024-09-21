package com.cloudstudio.readingservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName WebController
 * @Author Create By matrix
 * @Date 2024/8/31 15:44
 */
@Controller
public class WebController {
    @RequestMapping("/index")
    public String Index(){
        return "index";
    }

    @RequestMapping("/chat")
    public String Chat(){
        return "chat";
    }
}
