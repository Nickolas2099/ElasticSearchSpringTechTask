package com.example.searchTechTask.repository;

import com.example.searchTechTask.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
