package com.hcmute.fastfoodsystem.controller;

import com.hcmute.fastfoodsystem.dto.UserDto;
import com.hcmute.fastfoodsystem.model.ERole;
import com.hcmute.fastfoodsystem.model.User;
import com.hcmute.fastfoodsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminUserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsersByRole(ERole.ROLE_USER);
        model.addAttribute("users", users);
        return "admin/accounts";
    }

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, Model model) {
        String errorMessage = "User not found with id: " + id;
        try {
            User userToDelete = userService.getUserByIdOrElseThrow(id, errorMessage);
            userService.deleteUser(userToDelete);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while deleting the user.");
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/updateUser")
    public String updateUser(UserDto userDto, RedirectAttributes redirectAttributes) {
        try {
            User userToUpdate = userService.getUserByIdOrElseThrow(userDto.getId(), "User not found with id: " + userDto.getId());
            userToUpdate.setFirstName(userDto.getFirstName());
            userToUpdate.setLastName(userDto.getLastName());
            userToUpdate.setPhoneNumber(userDto.getPhoneNumber());
            userToUpdate.setAddress(userDto.getAddress());
            userService.updateUser(userToUpdate);
            redirectAttributes.addFlashAttribute("success", "User updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while updating the user");
        }
        return "redirect:/admin/users";
    }
}