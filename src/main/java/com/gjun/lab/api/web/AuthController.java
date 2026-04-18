package com.gjun.lab.api.web;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gjun.lab.common.dto.LoginRequest;
import com.gjun.lab.common.dto.RefreshTokenRequest;
import com.gjun.lab.common.dto.TokenResponse;
import com.gjun.lab.security.jwt.JwtTokenService;

import io.jsonwebtoken.JwtException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final JwtTokenService jwtTokenService;

  public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
      JwtTokenService jwtTokenService) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.jwtTokenService = jwtTokenService;
  }

  @PostMapping("/login")
  public TokenResponse login(@RequestBody LoginRequest request) {
    if (request.username() == null || request.password() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username and password are required");
    }
    try {

      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.username(), request.password()));
      UserDetails principal = (UserDetails) authentication.getPrincipal();
      String access = jwtTokenService.createAccessToken(principal);
      String refresh = jwtTokenService.createRefreshToken(principal.getUsername());
      return new TokenResponse(access, refresh, "Bearer", jwtTokenService.getAccessTokenTtlSeconds());
    } catch (BadCredentialsException ex) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }
  }

  @PostMapping("/refresh")
  public TokenResponse refresh(@RequestBody RefreshTokenRequest request) {
    if (request.refreshToken() == null || request.refreshToken().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "refreshToken is required");
    }
    try {
      String username = jwtTokenService.subjectFromRefreshToken(request.refreshToken());
      UserDetails user = userDetailsService.loadUserByUsername(username);
      String access = jwtTokenService.createAccessToken(user);
      String refresh = jwtTokenService.createRefreshToken(user.getUsername());
      return new TokenResponse(access, refresh, "Bearer", jwtTokenService.getAccessTokenTtlSeconds());
    } catch (JwtException | UsernameNotFoundException | IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token");
    }
  }
}
