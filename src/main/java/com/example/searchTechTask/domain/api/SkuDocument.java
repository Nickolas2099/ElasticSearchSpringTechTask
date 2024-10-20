package com.example.searchTechTask.domain.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuDocument {

    private Integer skuId;
    private String skuColor;
    private String skuSize;
    private double skuPrice;
}
