package com.hcmute.fastfoodsystem.controller;

import com.hcmute.fastfoodsystem.dto.ProductDto;
import com.hcmute.fastfoodsystem.model.Category;
import com.hcmute.fastfoodsystem.model.Product;
import com.hcmute.fastfoodsystem.service.CategoryService;
import com.hcmute.fastfoodsystem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAllProducts(Model model) {
        List<Product> products =  productService.getAllProduct();
        List<Category> categories = categoryService.getAllCategory();
        List<Product> bestSellers = productService.getBestSellingProducts();
        List<Product> topFourProducts = productService.getTopFourProductsByPrice();
        model.addAttribute("topFourProducts", topFourProducts);
        model.addAttribute("bestSellers", bestSellers);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("title", "Shop");
        return "index";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable("id") long id, Model model){
        ProductDto product = ProductDto.of(productService.getProductByIdOrElseThrow(id, "Product not found"));
        model.addAttribute("product", product);
        return "shop-detail";
    }

    @GetMapping("/category/{id}")
    public String getProductByCategory(@PathVariable("id") long id, Model model){
        List<Category> categories = categoryService.getAllCategory();
        List<Product> products = productService.getProductsByCategory(id);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "index";
    }



}

