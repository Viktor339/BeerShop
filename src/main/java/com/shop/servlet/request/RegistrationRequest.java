package com.shop.servlet.request;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String name;
    private String password;
    private String email;
}
