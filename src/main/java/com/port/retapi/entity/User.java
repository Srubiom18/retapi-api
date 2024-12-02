package com.port.retapi.entity;

import com.port.retapi.config.security.enums.UserRol;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname; // Nombre del Usuario

    private String email; // Email del Usuario

    private String password; // Contraseña del Usuario

    @Enumerated(EnumType.STRING)
    private UserRol userRol; // Indica el rol del usuario para controlar el acceso a los endpoints


}
