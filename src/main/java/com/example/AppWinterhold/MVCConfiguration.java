package com.example.AppWinterhold;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfiguration implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("forward:/home/index");
        registry.addViewController("/books").setViewName("forward:/category/index");
        registry.addViewController("/loginProcess").setViewName("forward:/home/index");
    }
}
