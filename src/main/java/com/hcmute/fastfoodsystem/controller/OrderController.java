package com.hcmute.fastfoodsystem.controller;

import com.hcmute.fastfoodsystem.config.ApiResponse;
import com.hcmute.fastfoodsystem.exceptions.AuthenticationFailException;
import com.hcmute.fastfoodsystem.model.Order;
import com.hcmute.fastfoodsystem.model.User;
import com.hcmute.fastfoodsystem.security.jwt.AuthTokenFilter;
import com.hcmute.fastfoodsystem.security.jwt.JwtUtils;
import com.hcmute.fastfoodsystem.service.OrderService;
import com.hcmute.fastfoodsystem.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UserService userService;
}
