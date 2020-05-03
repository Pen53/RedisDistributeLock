package com.mepeng.cn.pen.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ServletComponentScan
@ComponentScan("com.mepeng.cn")
@EnableAsync
public class OrderApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}
}
