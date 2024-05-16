package com.hcmute.fastfoodsystem.builder;

import com.hcmute.fastfoodsystem.dto.ProductDto;

public abstract class AbstractProductDtoBuilder {
    protected ProductDto productDto;

    public AbstractProductDtoBuilder() {
        productDto = new ProductDto();
    }

    public abstract void buildCriteria();
    public abstract ProductDto getCriteria();
}
