package com.shop.servlet.request;

import lombok.Data;

@Data
public class RegistrationRequest extends Request{
    private String name;
    private String password;
    private String email;
}
