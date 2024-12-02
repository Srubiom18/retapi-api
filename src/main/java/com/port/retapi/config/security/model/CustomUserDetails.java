package com.port.retapi.config.security.model;

import com.port.retapi.config.security.enums.UserRol;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class CustomUserDetails extends User {

    private final String nickname;
    private final UserRol userRol;

    public CustomUserDetails(String nickname, String password, Collection<? extends GrantedAuthority> authorities, UserRol userRol) {
        super(nickname, password, authorities);
        this.nickname = nickname;
        this.userRol = userRol;
    }
}
