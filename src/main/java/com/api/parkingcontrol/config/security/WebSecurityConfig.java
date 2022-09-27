package com.api.parkingcontrol.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
//@Configuration
//@EnableWebSecurity // desabilita todas as configuraçoes default do spring security
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;

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
                .anyRequest()
//                .authenticated()
                .permitAll()
                .and()
                .csrf().disable();

    }

    // Autorizaçao
    // só aceita password com PasswordEncoder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

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
