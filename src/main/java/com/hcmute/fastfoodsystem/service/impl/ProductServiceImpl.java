package com.hcmute.fastfoodsystem.service.impl;

import com.hcmute.fastfoodsystem.dto.ProductDto;
import com.hcmute.fastfoodsystem.model.Category;
import com.hcmute.fastfoodsystem.model.Product;
import com.hcmute.fastfoodsystem.repository.CategoryRepository;
import com.hcmute.fastfoodsystem.repository.ProductRepository;
import com.hcmute.fastfoodsystem.service.CategoryService;
import com.hcmute.fastfoodsystem.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Autowired
    CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository){this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
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
    public void deleteProductsByIds(List<Long> productIds) {
        for (Long id : productIds) {
            deleteProductById(id);
        }
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
    public Product addProduct(ProductDto productDto) {
        // 1. Retrieve Category (and handle potential errors)
        Category category = categoryRepository.findByCategory(productDto.getCategory());

        // 2. Convert DTO to Product (and associate category)
        Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setImage(productDto.getImage());
        product.setCategory(category); // Set the retrieved category

        // 3. Save Product to Database
        return productRepository.save(product);
    }

    @Override
    public void updateProduct(Product existingProduct, ProductDto productDto) {
        // 1. Retrieve Category (and handle potential errors)
        Category category = categoryRepository.findByCategory(productDto.getCategory());
        if (category == null) {
            throw new EntityNotFoundException("Category not found: " + productDto.getCategory());
        }

        // 2. Update Existing Product Fields
        existingProduct.setProductName(productDto.getProductName());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setQuantity(productDto.getQuantity());
        // Only update image if a new one is provided
        if (!productDto.getImage().isEmpty()) {
            existingProduct.setImage(productDto.getImage());
        }
        existingProduct.setCategory(category);

        // 3. Save Updated Product
        productRepository.save(existingProduct); // This will perform an update operation
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
    @Override
    public List<Product> getBestSellingProducts() {
        return productRepository.findTop6ByOrderByQuantityAsc();
    }

    public List<Product> getTopFourProductsByPrice() {
        return productRepository.findTop4ByOrderByPriceAsc();
    }
}
