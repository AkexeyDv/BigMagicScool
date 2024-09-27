package com.pro.sky.ScoolMagic.Controllers;

import com.pro.sky.ScoolMagic.ScoolMagicApplication;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/port")
public class InfoController {
    @Value("${server.port}")
    private String port;
    @GetMapping
    public String getPort(){
        return port;
    }
}
