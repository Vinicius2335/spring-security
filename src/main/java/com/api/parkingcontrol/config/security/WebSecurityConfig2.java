package com.api.parkingcontrol.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // em vez de usar o antMatcher podemos usar uma anotaçao nos endpoint para as permiçoes
public class WebSecurityConfig2 {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // configs
        httpSecurity.httpBasic()
                .and()
                .authorizeHttpRequests()
                // restrinçao em relaçao as roles
                //.antMatchers(HttpMethod.GET, "/parking-spot/**").permitAll()
                //.antMatchers(HttpMethod.POST, "/parking-spot").hasAnyRole("ADMIN", "USER")
                //.antMatchers(HttpMethod.DELETE, "/parking-spot/**").hasRole("ADMIN")
                //
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
