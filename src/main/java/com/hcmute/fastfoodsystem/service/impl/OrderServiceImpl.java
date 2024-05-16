package com.hcmute.fastfoodsystem.service.impl;


import com.hcmute.fastfoodsystem.model.*;
import com.hcmute.fastfoodsystem.repository.*;
import com.hcmute.fastfoodsystem.service.CartService;
import com.hcmute.fastfoodsystem.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository detailRepository;
    private final UserRepository customerRepository;
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository detailRepository, UserRepository customerRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.detailRepository = detailRepository;
        this.customerRepository = customerRepository;
        this.cartService = cartService;
    }

    @Override
    @Transactional
    public Order save(Cart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setUser(shoppingCart.getUser());
        order.setTax(2);
        order.setTotalAmount(shoppingCart.getTotalPrice());
        order.setAccept(false);
        order.setPaymentMethod("Cash");
        order.setOrderStatus("Pending");
        Order savedOrder = orderRepository.save(order);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartItems item : shoppingCart.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(savedOrder);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setPrice(item.getUnitPrice());
            orderDetail.setProduct(item.getProduct());
            detailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
        }
        order.setOrderDetails(orderDetailList);
        cartService.deleteCartById(shoppingCart.getId());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll(String username) {
        User customer = customerRepository.findByEmail(username);
        List<Order> orders = customer.getOrders();
        return orders;
    }

    @Override
    public List<Order> findALlOrders() {
        return orderRepository.findAll();
    }


    @Override
    public Order acceptOrder(Long id) {
        Order order = orderRepository.getById(id);
        order.setAccept(true);
        order.setDeliveryDate(new Date());
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
