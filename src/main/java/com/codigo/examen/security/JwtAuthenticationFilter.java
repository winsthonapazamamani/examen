package com.codigo.examen.security;

import com.codigo.examen.service.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
        setAuthenticationManager(authenticationManager);
        this.jwtService = jwtService;
        setFilterProcessesUrl("/ms-examen/v1/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequest.class);

            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                throw new IllegalArgumentException("Username or Password is empty");
            }

            return super.getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList())
            );
        } catch (IOException e) {
            throw new RuntimeException("Error al intentar autenticar el usuario", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = jwtService.generateToken(authResult.getName());
        response.addHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext(); // Limpiar el contexto de seguridad
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("Error de autenticación: " + failed.getMessage());
    }
}

