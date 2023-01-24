package com.john.springbootmall.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.john.springbootmall.util.JwtToken;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.auth.message.AuthException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@WebFilter(filterName = "", urlPatterns = {"/*"})
@Order(value = 1)
public class AuthorizationCheckFilter extends OncePerRequestFilter {

    // 不需做檢查的api
    public static final String USER_PATH = "/users/**";
    public static final String PROUDUCT_PATH = "/product/getProducts/**";
    public static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        if(!antPathMatcher.match(USER_PATH, servletPath) && !antPathMatcher.match(PROUDUCT_PATH, servletPath)){
            String authorHeader = req.getHeader("Authorization");

            if (authorHeader != null) {
                try {
                    JwtToken jwtToken = new JwtToken();
                    jwtToken.validateToken(authorHeader);
                } catch (AuthException e) {
                    System.err.println("Error : "+e);
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);

                    Map<String, String> err = new HashMap<>();
                    err.put("jwt_err", e.getMessage());
                    res.setContentType("application/json");
                    new ObjectMapper().writeValue(res.getOutputStream(), err);
                }

                chain.doFilter(req, res);
            } else {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        } else {
            chain.doFilter(req, res);
        }
    }
}
