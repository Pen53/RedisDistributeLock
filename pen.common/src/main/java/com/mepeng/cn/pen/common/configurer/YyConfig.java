package com.mepeng.cn.pen.common.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mepeng.cn.pen.common.filter.LoginInterceptor;
import com.mepeng.cn.pen.common.filter.RepeatedCheckFilter;
import com.mepeng.cn.pen.common.util.XssObjectMappper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableCaching
@Configuration
public class YyConfig extends WebMvcConfigurerAdapter {

    @Autowired
	LoginInterceptor loginInterceptor;
	@Autowired
	RepeatedCheckFilter repeatedCheckFilter;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//registry.addInterceptor(repeatedCheckFilter);
        registry.addInterceptor(loginInterceptor);
		super.addInterceptors(registry);
	}

//	@Bean
//    public RepeatedCheckFilter repeatedCheckFilter(){
//        return new RepeatedCheckFilter();
//    }

//    @Bean
//    public LoginInterceptor loginInterceptor(){
//        return new LoginInterceptor();
//    }
//
//	@Bean
//	public FilterRegistrationBean testFilterRegistration() {
//		FilterRegistrationBean registration = new FilterRegistrationBean();
//		registration.setFilter(new YcFilter());// 添加过滤器
//		registration.addUrlPatterns("/*");// 设置过滤路径，/*所有路径
//		// registration.addInitParameter("name", "alue");//添加默认参数
//		registration.setName("MyFilter");// 设置优先级
//		registration.setOrder(0);// 设置优先级
//		return registration;
//	}
//
//	@Bean
//	public CacheManager cacheManager() {
//		Map<String, CacheConfig> cacheConfigMap = new HashMap<String, CacheConfig>();
//		CacheConfig cacheConfig = new CacheConfig();
//		cacheConfig.setTTL(315360000000L);
//		cacheConfigMap.put("sysDataCache", cacheConfig);
//		return new RedissonSpringCacheManager(redissonClient, cacheConfigMap);
//	}
//
//
//    /**
//     * 定义消息转换器
//     *
//     * @param converters
//     * @return void
//     * @author zhangxianchao
//     * @since 2018/11/16 0016
//     */
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.set(0, mappingJackson2HttpMessageConverter());
//    }
//
//    /**
//     * 定义MapperConvert
//     *
//     * @param
//     * @return org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter
//     * @author zhangxianchao
//     * @since 2018/11/16 0016
//     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter converter = new
                MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }
//
//	@Bean
//	public MqSenderDefaultImpl mqSenderDefaultImpl(RabbitOperations rabbitOperations) {
//		MqSenderDefaultImpl mqSenderDefaultImpl = new MqSenderDefaultImpl();
//		mqSenderDefaultImpl.setRabbitOperations(rabbitOperations);
//		return mqSenderDefaultImpl;
//	}
//
//	@Bean
//	public MessageConverter messageConverter() {
//		Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
//		return jsonMessageConverter;
//	}
//
//    /**
//     * 定义ObjectMapper
//     *
//     * @param
//     * @return com.fasterxml.jackson.databind.ObjectMapper
//     * @author zhangxianchao
//     * @since 2018/11/16 0016
//     */
    @Bean
    public ObjectMapper objectMapper(){
        return new XssObjectMappper();
    }
}
