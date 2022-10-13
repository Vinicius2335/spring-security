package com.api.parkingcontrol.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.api.parkingcontrol.config.filter.JWTFilter;
import com.api.parkingcontrol.config.filter.LoginFilter;

//@Configuration OBS: Com usar EnableWebSecurity nao precisa dessa anotaçao
//em vez de usar o antMatcher podemos usar uma anotaçao nos endpoint para as permiçoes
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig2 {
	@Autowired
	@Lazy // pra nao dar nenhum erro
	private LoginFilter loginFilter;

	@Autowired
	private JWTFilter jwtFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


//	Não funcionou usando o usuário cadastrado no banco de dados, da erro um erro de passwordEncoder "null",
//	porem funcionou com o usuário cadastrado em memoria
//	
//	@Bean
//	public AuthenticationManager authenticationManager(UserDetailsServiceImpl userDetailsService) {
//		DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
//		dao.setUserDetailsService(userDetailsService);
//		return new ProviderManager(dao);
//	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder,
			UserDetailsServiceImpl userDetailService) throws Exception {

		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailService)
				.passwordEncoder(passwordEncoder).and().build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		// configs
		httpSecurity.csrf().disable();
//		httpSecurity.httpBasic(); Comentado pq estamos usando JWT
		httpSecurity.authorizeHttpRequests().anyRequest().authenticated();
//                .antMatchers(HttpMethod.GET, "/parking-spot/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/parking-spot").hasAnyRole("ADMIN", "USER")
//                .antMatchers(HttpMethod.DELETE, "/parking-spot/**").hasRole("ADMIN")
		httpSecurity.addFilterBefore(loginFilter, BasicAuthenticationFilter.class);
		httpSecurity.addFilterBefore(jwtFilter, BasicAuthenticationFilter.class);
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		return httpSecurity.build();
	}

}
