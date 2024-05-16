package com.hcmute.fastfoodsystem.builder;

import com.hcmute.fastfoodsystem.dto.ProductDto;
import com.hcmute.fastfoodsystem.model.Product;

public class ProductDtoBuilder extends AbstractProductDtoBuilder{
    private final Product product;

    public ProductDtoBuilder(Product product) {
        super();
        this.product = product;
    }

    @Override
    public void buildCriteria() {
        productDto.setId(product.getId());
        productDto.setProductName(product.getProductName());
        productDto.setImage(product.getImage());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        productDto.setCategory(product.getCategory().getCategory());
    }
    @Override
    public ProductDto getCriteria() {
        return productDto;
    }
}
