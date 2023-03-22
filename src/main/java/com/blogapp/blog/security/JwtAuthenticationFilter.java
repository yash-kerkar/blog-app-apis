package com.blogapp.blog.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private CustomUserDetailService userDetailsService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
			throws ServletException, IOException {
		String tokenString = request.getHeader("Authorization");
		String token = null;
		String username = null;
		
		if(tokenString == null || !tokenString.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		token = tokenString.substring(7); 
		username = this.jwtTokenHelper.extractUsername(token);
		System.out.println(SecurityContextHolder.getContext()==null);
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userdetails = this.userDetailsService.loadUserByUsername(username);
			if(jwtTokenHelper.validateToken(token, userdetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(userdetails,null,userdetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}else {
				System.out.println("Invalid JWT Token");
			}
		}
		else {
			System.out.println("Username is null or context is null");
		}
		
		filterChain.doFilter(request, response);
		
		
	}

}
