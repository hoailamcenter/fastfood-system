package com.hcmute.fastfoodsystem.service.impl;


import com.hcmute.fastfoodsystem.model.*;
import com.hcmute.fastfoodsystem.repository.*;
import com.hcmute.fastfoodsystem.service.CartService;
import com.hcmute.fastfoodsystem.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public Order save(Cart shoppingCart, String payment) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setUser(shoppingCart.getUser());
        order.setTax(2);
        order.setTotalAmount(shoppingCart.getTotalPrice());
        order.setAccept(false);
        order.setPaymentMethod(payment);
        order.setOrderStatus("Delivery");
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
    @Transactional
    public Order acceptOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setAccept(true);
            order.setOrderStatus("Delivery");
            order.setDeliveryDate(new Date());
            return orderRepository.save(order);
        } else {
            throw new EntityNotFoundException("Order with id " + id + " not found");
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
