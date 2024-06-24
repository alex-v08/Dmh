package com.dmh.UserService.repository;

import com.dmh.UserService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserById(Long userId);
    Optional<User> getUserByEmail(String email);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
}
