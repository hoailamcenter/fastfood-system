package com.hcmute.fastfoodsystem.service;

import com.hcmute.fastfoodsystem.dto.ProductDto;
import com.hcmute.fastfoodsystem.model.Product;
import com.hcmute.fastfoodsystem.model.User;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProduct();
    Product getProductByIdOrElseThrow(long id, String message);
    List<Product> getProductsByCategory(long id);
    Optional<Product> getProductById(long id);
    Product saveProduct(Product product);
    void deleteProduct(Product product);
    void deleteProductsByIds(List<Long> productIds);
    void deleteProductById(long id);
    List<Product> addAllProducts(List<Product> products);
    List<Product> getBestSellingProducts();
    List<Product> getTopFourProductsByPrice();
    ProductDto getById(long id);
    List<Product> searchProducts(String keyword);
    List<Product> findProductsByPriceRange(int minPrice, int maxPrice);
    List<Product> getRandomProducts(int numberOfProducts);
    Product addProduct(ProductDto productDto);
    void updateProduct(Product existingProduct, ProductDto productDto);
}
