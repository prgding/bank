package cc.ding.bankweb.config;

import cc.ding.bankweb.filter.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class ServletConfig {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public FilterRegistrationBean securityFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        //创建SecurityFilter对象
        SecurityFilter securityFilter = new SecurityFilter();
        //给SecurityFilter对象注入redis模板
        securityFilter.setStringRedisTemplate(stringRedisTemplate);
        //注册SecurityFilter
        filterRegistrationBean.setFilter(securityFilter);
        //配置SecurityFilter拦截所有请求
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
