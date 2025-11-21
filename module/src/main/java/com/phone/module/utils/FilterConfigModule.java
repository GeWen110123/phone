package com.phone.module.utils;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfigModule {

    @Autowired
    private ModuleFilter moduleFilter;
    @Bean
    public FilterRegistrationBean<Filter> FilterConfigModuleInit() {
        FilterRegistrationBean<Filter> filterRegistrationBean=new FilterRegistrationBean<Filter>();
        filterRegistrationBean.setFilter(moduleFilter);
        String[] list= {"/login", "/register", "/captchaImage"};
//         filterRegistrationBean.addUrlPatterns("/module/merge/*","/module/order/*");
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("moduleFilter");
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.setEnabled(true);
        return filterRegistrationBean;
    }
}