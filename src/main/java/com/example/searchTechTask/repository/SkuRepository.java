package com.example.searchTechTask.repository;

import com.example.searchTechTask.domain.entity.Sku;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkuRepository extends JpaRepository<Sku, Integer> {
}
