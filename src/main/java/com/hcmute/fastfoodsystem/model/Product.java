package com.hcmute.fastfoodsystem.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "image")
    private String image;
    @Column(name = "price")
    private double price;
    @Column(name = "quantity")
    private int quantity;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    @JsonIgnore
    @ManyToOne
    private Category category;


    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    @JsonIgnore
    @OneToMany(mappedBy = "product", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderDetail> orderDetailList = new ArrayList<>();

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private Set<CartItems> cartItems;

}
