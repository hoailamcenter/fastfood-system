package com.hcmute.fastfoodsystem.controller;

import com.hcmute.fastfoodsystem.model.Order;
import com.hcmute.fastfoodsystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard")
    public String getOrders(Model model) {
        List<Order> orders = orderService.findALlOrders();
        model.addAttribute("orders", orders);
        return "admin/dashboard";
    }

    @RequestMapping(value = "/confirm-order/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    public String confirmOrder(@PathVariable("id") Long id, RedirectAttributes attributes) {
        Order order = orderService.acceptOrder(id);
        if (order != null) {
            attributes.addFlashAttribute("success", "Order confirmed successfully!");
        } else {
            attributes.addFlashAttribute("error", "Failed to confirm order!");
        }
        return "redirect:/admin/dashboard";
    }
}