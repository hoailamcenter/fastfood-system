package com.hcmute.fastfoodsystem.controller;

import com.hcmute.fastfoodsystem.model.*;
import com.hcmute.fastfoodsystem.service.OrderService;
import com.hcmute.fastfoodsystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    UserService userService;

    @GetMapping("/check-out")
    public String checkOut( Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        String username = authentication.getName();
        User customer = userService.getUserByEmail(username);
        if (customer.getAddress() == null || customer.getPhoneNumber() == null) {
            model.addAttribute("information", "You need update your information before check out");
            model.addAttribute("customer", customer);
            model.addAttribute("title", "Profile");
            model.addAttribute("page", "Profile");
            return "profile";
        } else {
            Cart cart = userService.getUserByEmail(username).getCart();
            model.addAttribute("customer", customer);
            model.addAttribute("title", "Check-Out");
            model.addAttribute("page", "Check-Out");
            model.addAttribute("shoppingCart", cart);
            model.addAttribute("grandTotal", cart.getTotalItems());
            return "chackout";
        }
    }

    @GetMapping("/info")
    public String getOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        } else {
            String username = authentication.getName();
            User customer = userService.getUserByEmail(username);
            List<Order> orderList = customer.getOrders();
            model.addAttribute("orders", orderList);
            model.addAttribute("title", "Order");
            model.addAttribute("page", "Order");
            return "orders";
        }
    }
    @RequestMapping(value = "/cancel-order/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String cancelOrder(@PathVariable("id") Long id, RedirectAttributes attributes) {
        orderService.cancelOrder(id);
        attributes.addFlashAttribute("success", "Cancel order successfully!");
        return "order/info";
    }
    @RequestMapping(value = "/add-order", method = {RequestMethod.POST})
    public String createOrder(Model model,
                              HttpSession session,
                              @RequestParam(value = "paymentMethod", required = false, defaultValue = "Cash On Delivery") String payment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        } else {
            String username = authentication.getName();
            User customer = userService.getUserByEmail(username);
            Cart cart = customer.getCart();
            Order order = orderService.save(cart, payment);
            session.removeAttribute("totalItems");
            model.addAttribute("order", order);
            model.addAttribute("title", "Order");
            model.addAttribute("page", "Order");
            model.addAttribute("success", "Add order successfully");
            return "orders";
        }
    }
}
