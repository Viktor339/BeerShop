package com.shop.servlet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetUserHistoryResponse {
    private List<GetUserHistoryDto> entries;
}
