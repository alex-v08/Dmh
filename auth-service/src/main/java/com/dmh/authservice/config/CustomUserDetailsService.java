package com.dmh.authservice.config;


import com.dmh.authservice.client.UserServiceClient;
import com.dmh.authservice.exception.UserNotFoundException;
import com.dmh.authservice.model.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            log.debug("Buscando usuario por email: {}", username);

            ResponseEntity<UserDTO> response = userServiceClient.findByEmail(username);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.error("Usuario no encontrado con email: {}", username);
                throw new UsernameNotFoundException("No user found with email: " + username);
            }

            UserDTO user = response.getBody();
            log.debug("Usuario encontrado: {}", user.getEmail());

            return new CustomUserDetails(user);

        } catch (Exception e) {
            log.error("Error al cargar usuario: {}", username, e);
            throw new UsernameNotFoundException("Error loading user: " + username, e);
        }
    }

    /**
     * Método auxiliar para buscar usuario por ID
     */
    public UserDetails loadUserById(Long id) {
        try {
            log.debug("Buscando usuario por ID: {}", id);

            ResponseEntity<UserDTO> response = userServiceClient.findById(id);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.error("Usuario no encontrado con ID: {}", id);
                throw new UserNotFoundException ("User not found with id: " + id);
            }

            UserDTO user = response.getBody();
            log.debug("Usuario encontrado: {}", user.getEmail());

            return new CustomUserDetails(user);

        } catch (Exception e) {
            log.error("Error al cargar usuario por ID: {}", id, e);
            throw new UserNotFoundException("Error loading user by id: " + id, e);
        }
    }
}
