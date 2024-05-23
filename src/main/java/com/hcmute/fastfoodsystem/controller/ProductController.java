package com.hcmute.fastfoodsystem.controller;

import com.hcmute.fastfoodsystem.builder.ProductDtoBuilder;
import com.hcmute.fastfoodsystem.builder.ProductDtoDirector;
import com.hcmute.fastfoodsystem.dto.ProductDto;
import com.hcmute.fastfoodsystem.model.Category;
import com.hcmute.fastfoodsystem.model.Product;
import com.hcmute.fastfoodsystem.service.CategoryService;
import com.hcmute.fastfoodsystem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String getAllProducts(Model model) {
        List<Product> randomProducts = productService.getRandomProducts(3);
        List<Product> products =  productService.getAllProduct();
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("randomProducts", randomProducts);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("title", "Shop");
        return "shop";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable("id") long id, Model model){
        Product product = productService.getProductByIdOrElseThrow(id, "Product not found");
        ProductDtoBuilder builder = new ProductDtoBuilder(product);
        ProductDtoDirector director = new ProductDtoDirector(builder);
        director.buildCriteria();
        ProductDto productDto = director.getCriteria();
        model.addAttribute("product", productDto);
        model.addAttribute("title", "Detail");
        return "shop-detail";
    }

    @GetMapping("/category/{id}")
    public String getProductByCategory(@PathVariable("id") long id, Model model){
        List<Category> categories = categoryService.getAllCategory();
        List<Product> products = productService.getProductsByCategory(id);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "shop";
    }

    @GetMapping("/search-product")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model) {
        List<Category> categories = categoryService.getAllCategory();
        List<Product> products = productService.searchProducts(keyword);
        model.addAttribute("title", "Search Products");
        model.addAttribute("page", "Result Search");
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "shop";
    }

    @GetMapping("/search-by-price")
    public String searchByPrice(@RequestParam("rangeInput") int maxPrice, Model model) {
        List<Category> categories = categoryService.getAllCategory();
        List<Product> products = productService.findProductsByPriceRange(0, maxPrice);
        model.addAttribute("title", "Search Products by Price");
        model.addAttribute("page", "Result Search");
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "shop";
    }
}
