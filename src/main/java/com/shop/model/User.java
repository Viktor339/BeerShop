package com.shop.model;

import lombok.Builder;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Data
@Builder(toBuilder = true)
@JsonIgnoreProperties(value = {"password", "role", "uuid"})
public class User {
    private String name;
    private String password;
    private String email;
    private String role;
    private String UUID;
}
