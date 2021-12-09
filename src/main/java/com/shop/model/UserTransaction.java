package com.shop.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder(toBuilder = true)
public class UserTransaction {
    private String name;
    private Integer quantity;
    private Integer user_id;
    private Date date;
}
