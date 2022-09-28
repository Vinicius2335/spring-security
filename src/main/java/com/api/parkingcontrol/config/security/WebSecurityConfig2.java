package com.api.parkingcontrol.config.security;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.api.parkingcontrol.config.security.filter.JWTAuthenticationFilter;
import com.api.parkingcontrol.config.security.filter.JWTValidationFilter;
import com.api.parkingcontrol.config.security.service.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true) // em vez de usar o antMatcher podemos usar uma anotaçao nos endpoint para as permiçoes
////@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig2 {
	private final JWTAuthenticationFilter jtwAuthenticationFilter;
	private final JWTValidationFilter jwtValidationFilter;
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    	
        // configs
        httpSecurity
        		.httpBasic()
                .and()
                .authorizeHttpRequests()
//                .antMatchers(HttpMethod.GET, "/parking-spot/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/parking-spot").hasAnyRole("ADMIN", "USER")
//                .antMatchers(HttpMethod.DELETE, "/parking-spot/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .addFilterAt(jtwAuthenticationFilter, BasicAuthenticationFilter.class)
                .addFilterAt(jwtValidationFilter, BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // agora nao temos mais estado gravado no servidor

        return httpSecurity.build();
    }
    
    
    // liberando o cors
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
    	final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
    	source.registerCorsConfiguration("/**", corsConfiguration);
		return (CorsConfigurationSource) source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
