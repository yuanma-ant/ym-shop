package bat.ke.qq.com.intercepter;


import bat.ke.qq.com.annotation.IgnoreAuth;
import bat.ke.qq.com.common.utils.CookieUtil;
import bat.ke.qq.com.common.utils.ResultUtil;
import bat.ke.qq.com.manager.dto.front.Member;
import bat.ke.qq.com.sso.service.LoginService;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * token拦截认证
 * 源码学院-ANT
 * 只为培养BAT程序员而生
 * http://bat.ke.qq.com
 * 往期视频加群:516212256 暗号:6
 */
@Component
public class TokenIntercepter extends HandlerInterceptorAdapter {

    @Autowired
    private LoginService loginService;

    public static String ACCESS_TOKEN="SN";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Object bean=handlerMethod.getBean();
        if(isIgnoreAuth(handlerMethod)){
            return true;
        }
        String token= CookieUtil.getCookieValue(request,ACCESS_TOKEN);
        if(StringUtils.isEmpty(token)){
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(
                    JSONUtil.toJsonStr(new ResultUtil<String>().setErrorMsg("登录已失效请重新登录")));
            return false;
        }
        Member member =loginService.getUserByToken(token);
        if (member!=null){
            MemberUtils.setMemberThreadLocal(member);
            return super.preHandle(request, response, handler);
        }else{
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(
                    JSONUtil.toJsonStr(new ResultUtil<String>().setErrorMsg("登录已失效请重新登录")));
        }
        return false;
    }

    private boolean isIgnoreAuth(HandlerMethod handlerMethod){
        Object bean=handlerMethod.getBean();
        Class clazz=bean.getClass();
        if(clazz.getAnnotation(IgnoreAuth.class)!=null){
            return true;
        }
        Method method=handlerMethod.getMethod();
        return method.getAnnotation(IgnoreAuth.class)!=null;
    }
}
