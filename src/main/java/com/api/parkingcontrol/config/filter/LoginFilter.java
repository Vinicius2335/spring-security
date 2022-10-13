package com.api.parkingcontrol.config.filter;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginFilter extends OncePerRequestFilter{
	private final AuthenticationManager authManager;
	private final JWTHelper jwtHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {
		String username = request.getHeader("username");
		String password = request.getHeader("password");
		
		Authentication authenticate = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		
		response.setHeader(HttpHeaders.AUTHORIZATION, createJwtToken(authenticate));
	}

	private String createJwtToken(Authentication authenticate) {
//		UserModel user = (UserModel)authenticate.getPrincipal();
		User user = (User)authenticate.getPrincipal();
		
		String rolesString = user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		return jwtHelper.createToken(user.getUsername(), Map.of("roles", rolesString));
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String method = request.getMethod();
		String uri = request.getRequestURI();
		boolean isLogin = HttpMethod.POST.matches(method) && uri.startsWith("/login");
		
		return !isLogin;
	}
	
	
	
}
