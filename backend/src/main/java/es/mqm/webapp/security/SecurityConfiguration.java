package es.mqm.webapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import es.mqm.webapp.security.jwt.JwtRequestFilter;
import es.mqm.webapp.security.jwt.JwtTokenProvider;
import es.mqm.webapp.security.jwt.UnauthorizedHandlerJwt;
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

	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;


	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider());
		http.securityMatcher("/api/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

		http.authorizeHttpRequests(authorize -> authorize
			.requestMatchers("/api/v1/orders/", "/api/v1/products/", "/api/v1/images/**", "/api/v1/locations/",
				"/api/v1/users/").permitAll()
			.requestMatchers(HttpMethod.PUT, "/api/v1/images/*/media").hasRole("USER")
			.requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole("USER")
			// PUBLIC ENDPOINTS
			.anyRequest().authenticated());

		// Disable Form login Authentication
		http.formLogin(formLogin -> formLogin.disable());

		// Disable CSRF protection (it is difficult to implement in REST APIs)
		http.csrf(csrf -> csrf.disable());

		// Disable Basic Authentication
		http.httpBasic(httpBasic -> httpBasic.disable());

		// Stateless session
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(new JwtRequestFilter(userDetailsService, jwtTokenProvider),
				UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http
				.authorizeHttpRequests(authorize -> authorize
						// PUBLIC PAGES
						.requestMatchers("/", "/css/**", "/images/**", "/search", "/product/**", "/user_profile/**",
								"/error", "/register", "/api/**")
						.permitAll()
						.requestMatchers("/v3/api-docs*/**").permitAll()
						.requestMatchers("/swagger-ui.html").permitAll()
						.requestMatchers("/swagger-ui/**").permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
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
                		.ignoringRequestMatchers("/api/**") // temporary
                		.csrfTokenRepository(new HttpSessionCsrfTokenRepository()));


		return http.build();
	}

}
