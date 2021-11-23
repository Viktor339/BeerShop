package com.shop.service.message;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserMessage implements Message {

    private final String key = "uuid";
    private final Map<String, String> resultMessage = new LinkedHashMap<>();

    public UserMessage() {
        resultMessage.put(key, "");
    }

    public Map<String, String> build(String value) {
        resultMessage.put(key, value);
        return resultMessage;
    }
}
