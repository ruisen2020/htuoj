package org.example.htuoj.project.config;


import org.example.htuoj.common.utils.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//public class LoginInterceptor implements HandlerInterceptor {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (request.getMethod().equals("OPTIONS")) {
//            //设置了登录全局拦截，会导致跨域的OPTIONS尝试请求被拦截，这里放开
//            return true;
//        }
//        // 1.判断是否需要拦截（ThreadLocal中是否有用户）
//        if (UserHolder.getUser() == null) {
//            // 没有，需要拦截，设置状态码
//            response.setStatus(401);
////             拦截
//            return false;
//        }
//        // 有用户，则放行
//        return true;
//    }
//}