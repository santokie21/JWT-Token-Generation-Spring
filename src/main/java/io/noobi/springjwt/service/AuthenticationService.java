package io.noobi.springjwt.service;

import io.noobi.springjwt.model.User;
import io.noobi.springjwt.repository.UserRepository;
import io.noobi.springjwt.response.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  public AuthenticationResponse register(User request) {
    User user=new User();
    user.setName(request.getName());
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    user.setRole(request.getRole());

    userRepository.save(user);

    String token =jwtService.generateToken(user);

    return new AuthenticationResponse(token);
  }

  public AuthenticationResponse login(User request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    );

    User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
    String token = jwtService.generateToken(user);

    return new AuthenticationResponse(token);
  }
}
