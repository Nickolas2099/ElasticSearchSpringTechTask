package com.example.searchTechTask.domain.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDocument {

    private Integer productId;
    private String productName;
    private boolean productActive;
    private Date productStartDate;
    private List<SkuDocument> skus;

}
