package com.shop.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class User {
    private String name;
    private String password;
    private String email;
    private String role;
    private String UUID;
}
