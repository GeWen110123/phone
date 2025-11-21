package com.phone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@EnableAsync
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.phone.system.mapper") // <-- 扫描 Mapper 接口
public class RuoYiApplication {
    public static void main(String[] args) {
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("OJBKﾞ ");
    }
}