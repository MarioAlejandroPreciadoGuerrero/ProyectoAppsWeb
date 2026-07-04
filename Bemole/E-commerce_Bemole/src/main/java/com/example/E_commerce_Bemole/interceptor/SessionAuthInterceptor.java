package com.example.E_commerce_Bemole.interceptor;

import com.example.E_commerce_Bemole.controller.AuthViewController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionAuthInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object token = request.getSession().getAttribute(AuthViewController.SESSION_TOKEN);

        if (token == null) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
