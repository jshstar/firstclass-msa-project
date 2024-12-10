package com.first_class.msa.auth.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            // JwtUtil 인스턴스를 통해 validateToken 메서드를 호출합니다.
            if (jwtUtil.validateToken(token, jwtUtil.extractAccount(token))) {

                // 토큰이 유효하면 SecurityContext에 인증 정보 설정
                String account = jwtUtil.extractAccount(token);
                String role = jwtUtil.extractRole(token);

                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(account, null, List.of(new SimpleGrantedAuthority(role))));
            }
        }

        filterChain.doFilter(request, response);
    }
}