package com.example.wk_prognose;

import com.example.wk_prognose.config.ScoreProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableConfigurationProperties(ScoreProperties.class)
public class WkPrognoseApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(WkPrognoseApplication.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addRedirectViewController("/","/home");
        registry.addViewController("/login").setViewName("login");
    }
}
