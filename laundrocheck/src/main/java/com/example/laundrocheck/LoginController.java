package com.example.laundrocheck;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login/google")
    public String loginWithGoogle() {
        return "redirect:/oauth2/authorization/google";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @GetMapping("/failure")
    public String failure() {
        return "failure";
    }
}
