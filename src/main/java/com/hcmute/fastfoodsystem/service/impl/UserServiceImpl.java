package com.hcmute.fastfoodsystem.service.impl;

import com.hcmute.fastfoodsystem.model.ERole;
import com.hcmute.fastfoodsystem.model.Role;
import com.hcmute.fastfoodsystem.model.User;
import com.hcmute.fastfoodsystem.repository.RoleRepository;
import com.hcmute.fastfoodsystem.repository.UserRepository;
import com.hcmute.fastfoodsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public User getUserByIdOrElseThrow(long id, String message) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceAccessException(message));
    }
    @Override
    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    @Override
    public void deleteUser(User user){
        userRepository.delete(user);
    }
    @Override
    public void deleteUserById(long id) {
        String message = "User not found with id: " + id;
        getUserByIdOrElseThrow(id, message);
        userRepository.deleteById(id);
    }
    @Override
    public List<User> addAllUsers(List<User> users) {
        users.forEach(ele -> {
            if (ele.getId() != 0) {
                ele.setId(0);
            }
        });
        return userRepository.saveAll(users);
    }
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByPassword(String password) {
        return Optional.empty();
    }

    @Override
    public User changePassword(User user) {
        User customer = userRepository.findByEmail(user.getEmail());
        customer.setPassword(user.getPassword());
        return userRepository.save(customer);
    }
    @Override
    public List<User> getAllUsersByRole(ERole role) {
        // Fetch the corresponding Role entity from the roleRepository
        Role userRole = roleRepository.findByName(role)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + role));
        return userRepository.findByRoles(userRole);
    }
    @Override
    public User update(User user) {
        User customer = userRepository.findByEmail(user.getEmail());
        customer.setAddress(user.getAddress());
        customer.setPhoneNumber(user.getPhoneNumber());
        return userRepository.save(customer);
    }
    @Override
    public void updateUser(User userToUpdate) {
        userRepository.save(userToUpdate);
    }
}
