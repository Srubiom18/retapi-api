package com.port.retapi.web.auth;

import com.port.retapi.config.security.JwtTokenProvider;
import com.port.retapi.config.security.model.CustomUserDetails;
import com.port.retapi.dto.request.DataLogin;
import com.port.retapi.dto.request.DataRegister;
import com.port.retapi.service.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthServiceImpl authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthServiceImpl authService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    // Metodo utilizado para registrar un usuario sin activar
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody DataRegister data) {
        return ResponseEntity.ok(authService.registerUser(data));
    }

    // Endpoint para autenticar y generar el token
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody DataLogin dataLogin) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dataLogin.getNickname(), dataLogin.getPassword()));

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(jwtTokenProvider.createToken(customUserDetails.getNickname(), customUserDetails.getUserRol()));
    }

}
