package com.api.parkingcontrol.config.security.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.api.parkingcontrol.models.UserModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	public static final long EXPIRATION_TIME = 600_000L;	 // 10 minutos;
	public static final String SECRET = "1ece0e83-66ed-41c0-87ca-f6f641f484f3";
	
	private final AuthenticationManager authenticationManager;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			UserModel user = new ObjectMapper().readValue(request.getInputStream(), UserModel.class);

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),
					user.getPassword(), user.getAuthorities()));
		} catch (IOException e) {
			throw new RuntimeException("Failed to authenticate user", e); // BUG
		}
	}

	// Caso aconteça um sucesso na autenticaçao oque temos que fazer
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		UserModel user = (UserModel) authResult.getPrincipal();
		
		// Auth0
		String token = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(SECRET));
		
		response.getWriter().write(token);
		response.getWriter().flush();
	}

}
