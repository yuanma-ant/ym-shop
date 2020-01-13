package bat.ke.qq.com.front.interceptor;

import bat.ke.qq.com.intercepter.TokenIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * @author 源码学院
 */
@Configuration
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {

    @Resource
    private TokenIntercepter tokenIntercepter;

    @Autowired
    private LimitRaterInterceptor limitRaterInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 注册拦截器
        InterceptorRegistration ir = registry.addInterceptor(limitRaterInterceptor);
        // 配置拦截的路径
        ir.addPathPatterns("/**");

        InterceptorRegistration ti = registry.addInterceptor(tokenIntercepter);
        ti.addPathPatterns("/**");
    }
}
