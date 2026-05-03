package es.mqm.webapp.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.mqm.webapp.model.Image;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.LocationService;
import es.mqm.webapp.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.core.io.ClassPathResource;

@Service
public class UserLoginService {

	private static final Logger log = LoggerFactory.getLogger(UserLoginService.class);

	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final LocationService locationService;

	public UserLoginService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, 
			JwtTokenProvider jwtTokenProvider, UserService userService, PasswordEncoder passwordEncoder, 
			LocationService locationService) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.locationService = locationService;
	}

	public ResponseEntity<AuthResponse> login(HttpServletResponse response, LoginRequest loginRequest) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		
		String username = loginRequest.getUsername();
		UserDetails user = userDetailsService.loadUserByUsername(username);

		HttpHeaders responseHeaders = new HttpHeaders();
		var newAccessToken = jwtTokenProvider.generateAccessToken(user);
		var newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

		response.addCookie(buildTokenCookie(TokenType.ACCESS, newAccessToken));
		response.addCookie(buildTokenCookie(TokenType.REFRESH, newRefreshToken));

		AuthResponse loginResponse = new AuthResponse(AuthResponse.Status.SUCCESS,
				"Auth successful. Tokens are created in cookie.");
		return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
	}

	public ResponseEntity<AuthResponse> register(HttpServletResponse response, RegisterRequest registerRequest) {
		try {
			// Check if user already exists
			if (userService.findByEmail(registerRequest.getUsername()).isPresent()) {
				AuthResponse registerResponse = new AuthResponse(AuthResponse.Status.FAILURE,
						"User already exists with this email");
				return ResponseEntity.ok().body(registerResponse);
			}

			// Create default image
			Image image = new Image();
			try (InputStream inputStream = new ClassPathResource("static/images/usuario anonimo.jpg").getInputStream()) {
				image.setImageFile(new SerialBlob(inputStream.readAllBytes()));
			} catch (Exception e) {
				log.error("Failed to load default user image", e);
				AuthResponse registerResponse = new AuthResponse(AuthResponse.Status.FAILURE,
						"Failed to create user profile");
				return ResponseEntity.ok().body(registerResponse);
			}

			// Handle location
			var location = registerRequest.getLocation();
			if (location != null) {
				var existingLocation = locationService.findByCity(location.getName());
				if (existingLocation.isPresent()) {
					location = existingLocation.get();
				} else {
					location = locationService.save(location);
				}
			}

			// Create and save user
			User user = new User(
				registerRequest.getName(),
				registerRequest.getSurnames(),
				registerRequest.getUsername(),
				image,
				passwordEncoder.encode(registerRequest.getPassword()),
				location,
				"USER"
			);
			userService.save(user);

			// Now authenticate the newly created user
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(registerRequest.getUsername(), registerRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			String username = registerRequest.getUsername();
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			HttpHeaders responseHeaders = new HttpHeaders();
			var newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);
			var newRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

			response.addCookie(buildTokenCookie(TokenType.ACCESS, newAccessToken));
			response.addCookie(buildTokenCookie(TokenType.REFRESH, newRefreshToken));

			AuthResponse registerResponse = new AuthResponse(AuthResponse.Status.SUCCESS,
					"Registration successful. Tokens are created in cookie.");
			return ResponseEntity.ok().headers(responseHeaders).body(registerResponse);

		} catch (Exception e) {
			log.error("Error during user registration", e);
			AuthResponse registerResponse = new AuthResponse(AuthResponse.Status.FAILURE,
					"Registration failed: " + e.getMessage());
			return ResponseEntity.ok().body(registerResponse);
		}
	}

	public ResponseEntity<AuthResponse> refresh(HttpServletResponse response, String refreshToken) {
		try {
			var claims = jwtTokenProvider.validateToken(refreshToken);
			UserDetails user = userDetailsService.loadUserByUsername(claims.getSubject());

			var newAccessToken = jwtTokenProvider.generateAccessToken(user);
			response.addCookie(buildTokenCookie(TokenType.ACCESS, newAccessToken));

			AuthResponse loginResponse = new AuthResponse(AuthResponse.Status.SUCCESS,
					"Auth successful. Tokens are created in cookie.");
			return ResponseEntity.ok().body(loginResponse);

		} catch (Exception e) {
			log.error("Error while processing refresh token", e);
			AuthResponse loginResponse = new AuthResponse(AuthResponse.Status.FAILURE,
					"Failure while processing refresh token");
			return ResponseEntity.ok().body(loginResponse);
		}
	}

	public String logout(HttpServletResponse response) {
		SecurityContextHolder.clearContext();
		response.addCookie(removeTokenCookie(TokenType.ACCESS));
		response.addCookie(removeTokenCookie(TokenType.REFRESH));

		return "logout successfully";
	}

	private Cookie buildTokenCookie(TokenType type, String token) {
		Cookie cookie = new Cookie(type.cookieName, token);
		cookie.setMaxAge((int) type.duration.getSeconds());
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		return cookie;
	}

	private Cookie removeTokenCookie(TokenType type){
		Cookie cookie = new Cookie(type.cookieName, "");
		cookie.setMaxAge(0);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		return cookie;
	}
}