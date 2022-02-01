package com.decimalcode.qmed.utils;

import org.apache.logging.log4j.util.Strings;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.*;

@Converter
public class Map2StringConverter implements AttributeConverter<Map<String, String>, String> {

    private static final String SPLIT_CHAR_0 = ";";
    private static final String SPLIT_CHAR_1 = "&";

    @Override
    @SuppressWarnings("CodeBlock2Expr")
    public String convertToDatabaseColumn(Map<String, String> mapOfString) {
        if(mapOfString != null) {
            StringBuilder builder = new StringBuilder();
            mapOfString.forEach((k, v) -> {
                builder.append(k)
                        .append('&')
                        .append(v)
                        .append(';');
            });
            return builder.toString();
        }
        return Strings.EMPTY;
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String string2Map) {
        if(string2Map != null) {
            Map<String, String> collections = new LinkedHashMap<>();
            List<String> strings = Arrays.asList(string2Map.split(SPLIT_CHAR_0));
            strings.forEach((v) ->{
                if(!v.trim().isEmpty()) {
                    String[] s = v.split(SPLIT_CHAR_1);
                    collections.put(s[0], s[1]);
                }
            });
            return collections;
        }
        return Collections.emptyMap();
    }

}
