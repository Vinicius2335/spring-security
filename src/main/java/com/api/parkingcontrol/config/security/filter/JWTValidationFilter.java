package com.api.parkingcontrol.config.security.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.api.parkingcontrol.config.security.service.UserDetailsServiceImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.RequiredArgsConstructor;

public class JWTValidationFilter extends BasicAuthenticationFilter{
	public static final String HEADER_STRING = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer "; // tem que ter um espaço
	private final UserDetailsServiceImpl userDetailsServiceImpl;

	public JWTValidationFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsServiceImpl) {
		super(authenticationManager);
		this.userDetailsServiceImpl = userDetailsServiceImpl;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(HEADER_STRING);
		
		// se o header for nulo ou nao começar com o prefixo faz, retorna nada
		if(header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		String token = header.replace(TOKEN_PREFIX, "");
		UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);
		
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
		String user = JWT.require(Algorithm.HMAC512(JWTAuthenticationFilter.SECRET))
				.build()
				.verify(token)
				.getSubject();
		
		UserDetails userByUsername = userDetailsServiceImpl.loadUserByUsername(user);
		
		return user != null ? new UsernamePasswordAuthenticationToken(user, null, userByUsername.getAuthorities()) : null;
	}
	
	

}
