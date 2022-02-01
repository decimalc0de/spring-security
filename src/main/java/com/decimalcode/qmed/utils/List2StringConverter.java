package com.decimalcode.qmed.utils;

import static java.util.Collections.emptyList;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Converter
public class List2StringConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<String> listOfString) {
        return listOfString != null ? String.join(SPLIT_CHAR, listOfString) : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String string2List) {
        return string2List != null ? Arrays.asList(string2List.split(SPLIT_CHAR)): emptyList();
    }

}
