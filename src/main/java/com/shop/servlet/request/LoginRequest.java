package com.shop.servlet.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String name;
    private String password;
}
