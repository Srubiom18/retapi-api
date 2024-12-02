package com.port.retapi.config.security;

import com.port.retapi.config.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Lo primero es obtener el token de la cabecera de la petición
        String token = getTokenToHEaderPetition(request);

        // Validamos que el token no sea nulo, y además que sea válido
        if (Objects.nonNull(token) && jwtTokenProvider.validateToken(token)) {
            // Si el token es correcto recuperamos el nickname dek usuario que viene en el token
            String nickname = jwtTokenProvider.getNicknameOfToken(token);
            // Con el nickname, procedemos a recuperarlo de la BBDD para posteriormente meterlo en el contexto de spring
            // Este método está sobrescrito para que funcione con el nickname
            UserDetails userDetails = userDetailsService.loadUserByUsername(nickname);

            // Si se ha encontrado el usuario, lo autenticamos
            if (Objects.nonNull(userDetails)) {
                // Autenticamos el usuario y establecemos la información en el contexto de seguridad de Spring
                JwtAuthenticationToken authentication = new JwtAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    // Método utilizado para obtener el token de la cabecera de la petición
    private String getTokenToHEaderPetition(HttpServletRequest request) {
        // Obtenemos el token que viene en la cabecera con la clave Authorization
        String bearerToken = request.getHeader("Authorization");

        if (Objects.nonNull(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
