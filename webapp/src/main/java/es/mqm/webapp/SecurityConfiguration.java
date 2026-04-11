package es.mqm.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import es.mqm.webapp.service.RepositoryUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

	@Autowired
	public RepositoryUserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http
				.authorizeHttpRequests(authorize -> authorize
						// PUBLIC PAGES
						.requestMatchers("/", "/css/**", "/images/**", "/search", "/product/**", "/user_profile/**",
								"/error", "/register")
						.permitAll()
						.requestMatchers("/h2-console/**").permitAll()
						.requestMatchers("/admin").hasRole("ADMIN")
						// PRIVATE PAGES
						.anyRequest().authenticated())
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.usernameParameter("inputEmail")
						.passwordParameter("inputPassword")
						.failureUrl("/login?error")
						.defaultSuccessUrl("/")
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.permitAll())
				.csrf(csrf -> csrf
                		.csrfTokenRepository(new HttpSessionCsrfTokenRepository()));

		// Allow H2 console iframe to load (SAMEORIGIN)
		http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

		return http.build();
	}

}
