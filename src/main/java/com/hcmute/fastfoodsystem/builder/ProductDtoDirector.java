package com.hcmute.fastfoodsystem.builder;

import com.hcmute.fastfoodsystem.dto.ProductDto;

public class ProductDtoDirector extends AbstractProductDtoDirector{
    public ProductDtoDirector(AbstractProductDtoBuilder builder) {
        super(builder);
    }

    @Override
    public void buildCriteria() {
        builder.buildCriteria();
    }

    @Override
    public ProductDto getCriteria() {
        return builder.getCriteria();
    }
}
