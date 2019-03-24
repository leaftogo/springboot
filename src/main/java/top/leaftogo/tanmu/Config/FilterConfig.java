package top.leaftogo.tanmu.Config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import top.leaftogo.tanmu.Filter.*;

import javax.servlet.Filter;

@Configuration
public class FilterConfig {
    @Bean //被@Bean标记的方法会在项目初始化的时候被执行一边，获取一个对象，这里是TestFilter类
    public Filter TestFilter(){ //这里返回的是你的过滤器类对象
        return new TestFilter();
    }

    @Bean
    public FilterRegistrationBean IdentityRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("TestFilter"));//你的过滤器的类名
        registration.addUrlPatterns("/login");//你的过滤器拦截的路径
        registration.setName("TestFilter");//你的过滤器的类名
        registration.setOrder(3);//该过滤器优先级
        return registration;
    }
}
