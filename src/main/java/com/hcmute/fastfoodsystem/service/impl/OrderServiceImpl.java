package com.hcmute.fastfoodsystem.service.impl;

import com.hcmute.fastfoodsystem.dto.CartDto;
import com.hcmute.fastfoodsystem.dto.CartItemDto;
import com.hcmute.fastfoodsystem.dto.PlaceOrderDto;
import com.hcmute.fastfoodsystem.model.Order;
import com.hcmute.fastfoodsystem.model.OrderDetail;
import com.hcmute.fastfoodsystem.model.User;
import com.hcmute.fastfoodsystem.repository.CartRepository;
import com.hcmute.fastfoodsystem.repository.OrderRepository;
import com.hcmute.fastfoodsystem.repository.ProductRepository;
import com.hcmute.fastfoodsystem.repository.UserRepository;
import com.hcmute.fastfoodsystem.security.jwt.AuthTokenFilter;
import com.hcmute.fastfoodsystem.service.CartService;
import com.hcmute.fastfoodsystem.service.OrderDetailService;
import com.hcmute.fastfoodsystem.service.OrderService;
import com.hcmute.fastfoodsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
/*
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    UserService userService;
    @Autowired
    CartService cartService;


    @Override
    public void placeOrder(long userId, String sessionId) {
        User user = userService.getUserById(userId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + userId));
        CartDto cartDto = cartService.listCartItems(user);

        PlaceOrderDto placeOrderDto = new PlaceOrderDto();
        placeOrderDto.setUserId(userId);
        placeOrderDto.setTotalPrice(cartDto.getTotalCost());

        long orderId = saveOrder(placeOrderDto, userId, sessionId);

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new UsernameNotFoundException("Order Not Found with id: " + orderId));
        List<CartItemDto> cartItemDtoList = cartDto.getCartItems();
        for (CartItemDto cartItemDto : cartItemDtoList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(cartItemDto.getQuantity());
            orderDetail.setPrice(cartItemDto.getProduct().getPrice());
            orderDetail.setProduct(cartItemDto.getProduct());
            orderDetail.setOrder(order);
            orderDetailService.addOrderedProducts(orderDetail);
        }
        cartService.deleteCartItems(userId);
    }

    @Override
    public long saveOrder(PlaceOrderDto orderDto, long userId, String sessionID) {
        Order order = getOrderFromDto(orderDto,userId,sessionID);
        return orderRepository.save(order).getOrderId();
    }

    @Override
    public Order getOrderFromDto(PlaceOrderDto orderDto, long userId, String sessionID) {
        User user = userService.getUserById(userId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + userId));
        return new Order(orderDto,user,sessionID);
    }

    @Override
    public List<Order> listOrders(long userId) {
        User user = userService.getUserById(userId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + userId));;
        return orderRepository.findAllByUserOrderByOrderDateDesc(user);
    }

 */
}
