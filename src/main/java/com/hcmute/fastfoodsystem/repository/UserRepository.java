package com.hcmute.fastfoodsystem.repository;

import com.hcmute.fastfoodsystem.model.Role;
import com.hcmute.fastfoodsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByRoles(Role role);
    Boolean existsByEmail(String email);
}
