package com.shop.service.message;

import java.util.LinkedHashMap;
import java.util.Map;

public class InfoMessage implements Message {

    private final String key = "message";
    private final Map<String, String> resultMessage = new LinkedHashMap<>();

    public InfoMessage() {
        resultMessage.put(key, "");
    }

    public Map<String, String> build(String value) {
        resultMessage.put(key, value);
        return resultMessage;
    }
}
