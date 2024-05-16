package com.hcmute.fastfoodsystem.builder;

import com.hcmute.fastfoodsystem.dto.ProductDto;

public abstract class AbstractProductDtoDirector {
    protected AbstractProductDtoBuilder builder;

    public AbstractProductDtoDirector(AbstractProductDtoBuilder builder) {
        this.builder = builder;
    }

    public abstract void buildCriteria();
    public abstract ProductDto getCriteria();
}
