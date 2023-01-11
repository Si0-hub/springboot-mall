package com.john.springbootmall.util;

import com.john.springbootmall.constant.ProductCategory;

import javax.persistence.AttributeConverter;

public class CategoryConverter implements AttributeConverter<ProductCategory, String> {

    @Override
    public String convertToDatabaseColumn(ProductCategory category) {
        return category.name();
    }

    @Override
    public ProductCategory convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return ProductCategory.valueOf(dbData);
    }
}
