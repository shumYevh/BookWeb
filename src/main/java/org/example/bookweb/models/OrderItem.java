package org.example.bookweb.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "orders")
public class OrderItem {
    @Id
    private Long id;
    @ManyToOne
    private Order order;
    @ManyToOne
    private Book book;
    private int quantity;
    private BigDecimal price;
}
