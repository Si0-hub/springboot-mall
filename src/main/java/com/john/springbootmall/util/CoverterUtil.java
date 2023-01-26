package com.john.springbootmall.util;

import com.john.springbootmall.constant.EnumBase;
import com.john.springbootmall.constant.impl.ProductCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public abstract class CoverterUtil<T extends Enum<T> & EnumBase<E>, E> implements AttributeConverter<T, E> {

    private final Class<T> clazz;

    public CoverterUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public E convertToDatabaseColumn(T attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public T convertToEntityAttribute(E dbData) {
        T[] enums = clazz.getEnumConstants();

        for (T e : enums) {
            if (e.getValue().equals(dbData)) {
                return e;
            }
        }

        return null;
    }
}
