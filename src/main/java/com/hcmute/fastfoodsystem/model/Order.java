package com.hcmute.fastfoodsystem.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hcmute.fastfoodsystem.dto.PlaceOrderDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "`Order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long orderId;

    @NotNull
    @Column(name = "order_date")
    private Date orderDate;

    @NotNull
    @Column(name = "total_amount")
    private double totalAmount;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    @JsonBackReference
    @ManyToOne
    private User user;

    @Column(name = "session_id")
    private String sessionId;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Order(PlaceOrderDto orderDto, User user, String sessionId){
        this.user = user;
        this.orderDate = new Date();
        this.totalAmount = orderDto.getTotalPrice();
        this.sessionId = sessionId;
    }
}
