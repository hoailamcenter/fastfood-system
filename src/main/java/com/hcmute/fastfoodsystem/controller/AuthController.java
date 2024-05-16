package com.hcmute.fastfoodsystem.controller;

import com.hcmute.fastfoodsystem.model.ERole;
import com.hcmute.fastfoodsystem.model.Role;
import com.hcmute.fastfoodsystem.dto.RegisterDto;
import com.hcmute.fastfoodsystem.model.User;
import com.hcmute.fastfoodsystem.repository.RoleRepository;
import com.hcmute.fastfoodsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("title", "Login Page");
        model.addAttribute("page", "Home");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Register");
        model.addAttribute("page", "Register");
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    @PostMapping("/do-register")
    public String registerCustomer(@Valid @ModelAttribute("registerDto") RegisterDto registerDto,
                                   BindingResult result,
                                   Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("registerDto", registerDto);
                return "register";
            }
            String username = registerDto.getEmail();
            User customer = userService.getUserByEmail(username);
            if (customer != null) {
                model.addAttribute("registerDto", registerDto);
                model.addAttribute("error", "Email has been register!");
                return "register";
            }
            if (registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
                User user = new User();
                user.setFirstName(registerDto.getFirstName());
                user.setLastName(registerDto.getLastName());
                user.setEmail(registerDto.getEmail());
                user.setPhoneNumber(registerDto.getPhoneNumber());
                user.setAddress(registerDto.getAddress());
                user.setPassword(encoder.encode(registerDto.getPassword()));
                Set<String> strRoles = registerDto.getRole();
                Set<Role> roles = new HashSet<>();

                if (strRoles == null) {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                } else {
                    strRoles.forEach(role -> {
                        if (role.equals("admin")) {
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);
                        } else {
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                        }
                    });
                }
                user.setRoles(roles);
                userService.saveUser(user);
                model.addAttribute("success", "Register successfully!");
            } else {
                model.addAttribute("error", "Password is incorrect");
                model.addAttribute("registerDto", registerDto);
                return "register";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Server is error, try again later!");
        }
        return "register";
    }
}
