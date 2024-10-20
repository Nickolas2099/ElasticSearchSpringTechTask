package com.example.searchTechTask.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private boolean active;

    @Column(name = "start_date")
    private Date startDate;

    @OneToMany
    @JoinColumn(name = "product_id")
    private Set<Sku> skus;

}
