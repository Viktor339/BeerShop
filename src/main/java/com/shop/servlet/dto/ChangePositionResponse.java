package com.shop.servlet.dto;

import com.shop.model.BottleBeerData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChangePositionResponse {
    private long id;
    private BottleBeerData beerInfo;
}
