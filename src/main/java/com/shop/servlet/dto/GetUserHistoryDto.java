package com.shop.servlet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class GetUserHistoryDto {
    private String name;
    private Integer quantity;
    private Date date;
}
