package com.github.souzafcharles.api.user.repository;

import com.github.souzafcharles.api.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findByUsernameContainingIgnoreCase(String keyword);
    long countByUsernameIsNotNull();
}
