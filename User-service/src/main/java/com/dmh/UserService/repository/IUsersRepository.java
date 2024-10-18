package com.dmh.UserService.repository;

import com.dmh.UserService.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
    Users findByDni(String dni);
}