package com.example.searchTechTask.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Sku {

    @Id
    private Long id;
}
