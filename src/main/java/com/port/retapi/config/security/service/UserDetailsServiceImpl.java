package com.port.retapi.config.security.service;

import com.port.retapi.config.security.model.CustomUserDetails;
import com.port.retapi.entity.User;
import com.port.retapi.config.security.repository.UserRepositorySecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepositorySecurity userRepositorySecurity;

    public UserDetailsServiceImpl(UserRepositorySecurity userRepositorySecurity) {
        this.userRepositorySecurity = userRepositorySecurity;
    }


    // Método utilizado para cargar el usuario por el nickname
    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        // Vamos a buscar si existe el usuario en la bbdd
        Optional<User> user = userRepositorySecurity.findByNickname(nickname);

        // Si no existe lanzamos excepción
        if (user.isEmpty()) {
            // TODO: LANZAR EXCEPCION
        }

        // Retornamos los detalles del usuario para que Spring Security pueda verificar
        return new CustomUserDetails(
                user.get().getNickname(),
                user.get().getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_" + user.get().getUserRol()),
                user.get().getUserRol()
        );
    }
}
