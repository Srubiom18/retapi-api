package com.port.retapi.service;

import com.port.retapi.config.security.enums.UserRol;
import com.port.retapi.dto.request.DataRegister;
import com.port.retapi.entity.User;
import com.port.retapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public String registerUser(DataRegister data) {
        // Creamos el objeto sin validar por lo mencionado antes, ya que todos los datos
        // deben venir ya correctos
        try {
            User user = User.builder()
                    .nickname(data.getNickname())
                    .password(passwordEncoder.encode(data.getPassword()))
                    .userRol(UserRol.STANDARD)
                    .email(data.getMail())
                    .build();

            // Registramos el usuario en la BBDD sin verificar, para que el usuario
            // tenga que hacerlo desde su email personal
            userRepository.save(user);


            return "Usuario registrado correctamente";
        } catch (Exception e) {
            // TODO: CONTROLAR EXCEPCIÓN
            return "Parece que algo falló al registrar el usuario";
        }
    }

}
