package com.hcmute.fastfoodsystem.service.impl;

import com.hcmute.fastfoodsystem.builder.ProductDtoBuilder;
import com.hcmute.fastfoodsystem.builder.ProductDtoDirector;
import com.hcmute.fastfoodsystem.dto.ProductDto;
import com.hcmute.fastfoodsystem.model.Category;
import com.hcmute.fastfoodsystem.model.Product;
import com.hcmute.fastfoodsystem.repository.ProductRepository;
import com.hcmute.fastfoodsystem.service.CategoryService;
import com.hcmute.fastfoodsystem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository){this.productRepository = productRepository;}
    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductByIdOrElseThrow(long id, String message) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceAccessException(message));
    }

    @Override
    public List<Product> getProductsByCategory(long id) {
        Category category = categoryService.getCategoryByIdOrElseThrow(id, "Category not found");
        return productRepository.findAllByCategory(category);
    }

    @Override
    public Optional<Product> getProductById(long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    @Override
    public void deleteProductById(long id) {
        String message = "Product not found with id: " + id;
        getProductByIdOrElseThrow(id, message);
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> addAllProducts(List<Product> products) {
        products.forEach(ele -> {
            if (ele.getId() != 0) {
                ele.setId(0);
            }
        });
        return productRepository.saveAll(products);
    }

    @Override
    public ProductDto getById(long id) {
        ProductDto productDto = new ProductDto();
        Product product = productRepository.findById(id).orElseThrow();
        productDto.setId(product.getId());
        productDto.setProductName(product.getProductName());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        productDto.setCategory(product.getCategory().getCategory());
        productDto.setImage(product.getImage());
        return productDto;
    }

    @Override
    public List<Product> searchProducts(String keyword){
        return  productRepository.searchProducts(keyword);
    }

    @Override
    public List<Product> findProductsByPriceRange(int minPrice, int maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public List<Product> getRandomProducts(int numberOfProducts) {
        List<Product> allProducts = getAllProduct();
        Collections.shuffle(allProducts);
        return allProducts.subList(0, Math.min(numberOfProducts, allProducts.size()));
    }


}
