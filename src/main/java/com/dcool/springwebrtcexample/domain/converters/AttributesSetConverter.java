package com.dcool.springwebrtcexample.domain.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;

@Converter(autoApply = true)
public class AttributesSetConverter implements AttributeConverter<AttributesSet, String> {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public String convertToDatabaseColumn(AttributesSet attribute) {
        try {
            if (attribute == null) return null;

            if (attribute.hasText()) {
                return attribute.getText();
            } else {
                return mapper.writeValueAsString(attribute);
            }

        } catch(Exception e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public AttributesSet convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) return null;
            return new AttributesSet(mapper.readValue(dbData, Collection.class));
        } catch(Exception e) {
            return null;
        }
    }
}