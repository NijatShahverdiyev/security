package az.gov.mia.security.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SecurityController {

    @GetMapping("/demo")
    public String demo(){
        return "Hello demo";
    }

    @GetMapping("/demo2")
    public String demo2(){
        return "Hello demo2";
    }

    @GetMapping("/demo-user")
    public String demoUser(){
        return "Hello User page";
    }

    @GetMapping("/demo-admin")
    public String demoAdmin(){
        return "Hello Admin page";
    }
}
