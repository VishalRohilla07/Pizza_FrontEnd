package com.pizza.ordering.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Pizza entity representing menu items
 */
@Entity
@Table(name = "pizzas", indexes = {
    @Index(name = "idx_category", columnList = "category"),
    @Index(name = "idx_available", columnList = "available")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pizza {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Pizza name is required")
    @Column(nullable = false, unique = true)
    private String name;
    
    @NotBlank(message = "Description is required")
    @Column(nullable = false, length = 500)
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PizzaCategory category;
    
    @Column(nullable = false)
    private String imageUrl;
    
    @Column(nullable = false)
    private Boolean available = true;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public enum PizzaCategory {
        VEG,
        NON_VEG
    }
}
