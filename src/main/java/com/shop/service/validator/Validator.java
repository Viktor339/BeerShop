package com.shop.service.validator;

import com.shop.service.Response;
import com.shop.servlet.request.RegistrationRequest;

public interface Validator<T> {

    Response getMessage(RegistrationRequest value);
}
