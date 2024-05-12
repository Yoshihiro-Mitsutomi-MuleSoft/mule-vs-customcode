package com.example.americanflight.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

/**
 * 各オブジェクトをHashMap/Listへの変換を行うために使用
 */
public class Convertable {
    ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, Object>> asListMap() {
        return objectMapper.convertValue(this, new TypeReference<List<Map<String, Object>>>() {
        });
    }

    public Map<String, Object> asMap() {
        return objectMapper.convertValue(this, new TypeReference<Map<String, Object>>() {
        });
    }

}
