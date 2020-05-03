package com.mepeng.cn.pen.common.configurer;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ActiveJdbcConfig {
    @Value("${spring.db.id}")
    private String id;
    @Value("${spring.db.driver}")
    private String driver;
    @Value("${spring.db.url}")
    private String url;
    @Value("${spring.db.user}")
    private String user;
    @Value("${spring.db.password}")
    private String password;
}
