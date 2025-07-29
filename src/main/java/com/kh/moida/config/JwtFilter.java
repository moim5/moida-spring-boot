package com.kh.moida.config;

import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kh.moida.model.UserPrincipal;
import com.kh.moida.service.CustomUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) {
        String jwt = null;
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        } else if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("JWT_TOKEN".equals(c.getName())) {
                    jwt = c.getValue();
                    break;
                }
            }
        }

        try {
            if (jwt != null) {
                String email = jwtUtil.getUsernameFromToken(jwt);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserPrincipal userDetails = (UserPrincipal) userDetailsService.loadUserByUsername(email);

                    if (jwtUtil.validateToken(jwt) != null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException | IllegalArgumentException e) {
            e.printStackTrace();
            SecurityContextHolder.clearContext();
            clearJwtCookie(response);
        } catch (Exception e) {
            e.printStackTrace();
            SecurityContextHolder.clearContext();
        }
    }

    private void clearJwtCookie(HttpServletResponse response) {
        Cookie expiredCookie = new Cookie("JWT_TOKEN", null);
        expiredCookie.setPath("/");
        expiredCookie.setMaxAge(0);
        expiredCookie.setHttpOnly(true);
        response.addCookie(expiredCookie);
    }
}
