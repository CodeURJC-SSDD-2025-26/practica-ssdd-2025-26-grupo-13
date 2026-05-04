package es.mqm.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.mqm.webapp.security.jwt.AuthResponse;
import es.mqm.webapp.security.jwt.AuthResponse.Status;
import es.mqm.webapp.security.jwt.LoginRequest;
import es.mqm.webapp.security.jwt.UserLoginService;
import jakarta.servlet.http.HttpServletResponse;
import es.mqm.webapp.security.jwt.RegisterRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginRestController {
	
	@Autowired
	private UserLoginService userService;

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(
			@RequestBody LoginRequest loginRequest,
			HttpServletResponse response) {
		
		return userService.login(response, loginRequest);
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(
		@RequestBody RegisterRequest registerRequest,
		HttpServletResponse response){
		return userService.register(response,registerRequest);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refreshToken(
			@CookieValue(name = "RefreshToken", required = false) String refreshToken, HttpServletResponse response) {

		return userService.refresh(response, refreshToken);
	}

	@PostMapping("/logout")
	public ResponseEntity<AuthResponse> logOut(HttpServletResponse response) {
		return ResponseEntity.ok(new AuthResponse(Status.SUCCESS, userService.logout(response)));
	}
}
