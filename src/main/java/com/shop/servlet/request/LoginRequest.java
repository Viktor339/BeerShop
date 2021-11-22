package com.shop.servlet.request;

import lombok.Data;

@Data
public class LoginRequest extends Request {
    private String name;
    private String password;
}
