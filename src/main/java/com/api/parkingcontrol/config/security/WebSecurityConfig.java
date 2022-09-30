package com.api.parkingcontrol.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.api.parkingcontrol.config.security.filter.JWTAuthenticationFilter;
import com.api.parkingcontrol.config.security.filter.JWTValidationFilter;
import com.api.parkingcontrol.config.security.service.UserDetailsServiceImpl;

@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableWebSecurity // desabilita todas as configuraçoes default do spring security
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    // Autenticaçao
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeHttpRequests()
                // restrinçao em relaçao as roles
//                .antMatchers(HttpMethod.GET, "/parking-spot/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/parking-spot").hasAnyRole("ADMIN","USER")
//                .antMatchers(HttpMethod.DELETE, "/parking-spot/**").hasRole("ADMIN")
                //
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTValidationFilter(authenticationManager(), userDetailsServiceImpl))
             
                // agora nao temos mais estado gravado no servidor
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); 

    }

    // Autorizaçao
    // só aceita password com PasswordEncoder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }
    
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//    	final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
//    	source.registerCorsConfiguration("/**", corsConfiguration);
//		return (CorsConfigurationSource) source;
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

/*
Autenticação
                http.httpBasic() // basic auth
                .and()
                .authorizeHttpRequests() // estamos autorizando http request
                .anyRequest() // para qualquer requisição
                .permitAll();// permitindo todos os acessos
        // permitindo o acesso sem autenticaçao para qualquer requisição http

                http.httpBasic()
                .and()
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated();
        // todas as requisiçoes precisao ser autenticadas
        // esse é o default quando implementamos a dependencia spring security
        // o usuário nao poderá usar o POST/PUT/DELETE pq nao tem autorizaçao 403 Forbbiden, proteçao CSRF vem por default, quando nao estamos usando serviços de Terceiros pode desabilitar

                http.httpBasic()
                .and()
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated();
                .and()
                .csrf().disable();
       // desabilitando o CSRF

Autorizaçao em memoria
                auth.inMemoryAuthentication()
                    .withUser("vinicius")
                    .password(passwordEncoder().encode("devdojo"))
                    .roles("ADMIN");
 */
