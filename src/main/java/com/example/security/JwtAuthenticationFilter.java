package com.example.security;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.model.ERole;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authorizationHeader = request.getHeader("Authorization");

        String accessToken = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        	accessToken = authorizationHeader.substring(7);
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
        	if (accessToken != null && jwtUtil.validateAccessToken(accessToken)) {
        		Long userId = jwtUtil.extractUserIdInAccessToken(accessToken);
				ERole role = jwtUtil.extractRoleInAccessToken(accessToken);
        	
        		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        				userId, null, this.getAuthorities(role));
        		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        		SecurityContextHolder.getContext().setAuthentication(authToken);        		
        	}
        }
        
        filterChain.doFilter(request, response);
	}

	private Collection<? extends GrantedAuthority> getAuthorities(ERole role) {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}
}
