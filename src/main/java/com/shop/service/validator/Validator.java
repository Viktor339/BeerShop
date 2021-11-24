package com.shop.service.validator;

import com.shop.servlet.request.RegistrationRequest;

public interface Validator<T> {
    boolean isValid(RegistrationRequest value);
    String getMessage();
}
