package com.kh.moida.config;

import com.kh.moida.model.UserPrincipal;
import com.kh.moida.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String jwt = null;

        String authHeader = httpRequest.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        } else {
            if (httpRequest.getCookies() != null) {
                for (Cookie c : httpRequest.getCookies()) {
                    if ("JWT_TOKEN".equals(c.getName())) {
                        jwt = c.getValue();
                        break;
                    }
                }
            }
        }

        if (jwt != null) {
            try {
                String email = jwtUtil.getUsernameFromToken(jwt);
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserPrincipal userDetails = (UserPrincipal) userDetailsService.loadUserByUsername(email);
                    if (jwtUtil.validateToken(jwt) != null) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (ExpiredJwtException | IllegalArgumentException e) {
                SecurityContextHolder.clearContext();
                httpRequest.getSession().invalidate();

                Cookie expiredCookie = new Cookie("JWT_TOKEN", null);
                expiredCookie.setPath("/");
                expiredCookie.setMaxAge(0);
                expiredCookie.setHttpOnly(true);
                httpResponse.addCookie(expiredCookie);

                httpResponse.sendRedirect("/sign/in");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
