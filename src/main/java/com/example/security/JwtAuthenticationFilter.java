package com.example.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService customUserDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authorizationHeader = request.getHeader("Authorization");

        String accessToken = null;
        String username = null;
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        	accessToken = authorizationHeader.substring(7);
        }
		System.out.println("accessToken: " + accessToken);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
        	if (accessToken != null && jwtUtil.validateAccessToken(accessToken)) {
        		username = jwtUtil.extractUsernameInAccessToken(accessToken);
        	}
			System.out.println("username: " + username);
        	if (username != null) {
        		LoginUser loginUser = (LoginUser) customUserDetailsService.loadUserByUsername(username); 
        		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        				loginUser, null, loginUser.getAuthorities());
        		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        		SecurityContextHolder.getContext().setAuthentication(authToken);        		
        	}
        }
        
        filterChain.doFilter(request, response);
	}
}
