package com.hcmute.fastfoodsystem.controller;

import com.hcmute.fastfoodsystem.model.User;
import com.hcmute.fastfoodsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        model.addAttribute("title", "Change password");
        model.addAttribute("page", "Change password");
        return "changepassword";
    }
    @PostMapping("/change-password")
    public String changePass(@RequestParam("oldPassword") String oldPassword,
                             @RequestParam("newPassword") String newPassword,
                             @RequestParam("repeatNewPassword") String repeatPassword,
                             RedirectAttributes attributes,
                             Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        } else {
            String username = authentication.getName();
            User customer = userService.getUserByEmail(username);
            if (encoder.matches(oldPassword, customer.getPassword())
                    && !encoder.matches(newPassword, oldPassword)
                    && !encoder.matches(newPassword, customer.getPassword())
                    && repeatPassword.equals(newPassword) && newPassword.length() >= 5) {
                customer.setPassword(encoder.encode(newPassword));
                userService.changePassword(customer);
                attributes.addFlashAttribute("success", "Your password has been changed successfully!");
                return "login";
            } else {
                model.addAttribute("message", "Your password is wrong");
                return "changepassword";
            }
        }
    }
    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        String username = authentication.getName();
        User customer = userService.getUserByEmail(username);
        model.addAttribute("customer", customer);
        model.addAttribute("title", "Profile");
        model.addAttribute("page", "Profile");
        return "profile";

    }
    @PostMapping("/update-profile")
    public String updateProfile(@Valid @ModelAttribute("customer") User user,
                                RedirectAttributes attributes,
                                Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        String username = authentication.getName();
        userService.update(user);
        User customerUpdate = userService.getUserByEmail(username);
        attributes.addFlashAttribute("success", "Update successfully!");
        model.addAttribute("customer", customerUpdate);
        return "redirect:/profile";
    }
}
