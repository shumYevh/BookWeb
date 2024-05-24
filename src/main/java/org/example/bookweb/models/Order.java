package org.example.bookweb.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@SQLDelete(sql = "UPDATE books SET is_deleted = true WHERE id=?")
@SQLRestriction(value = "is_deleted=false")
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private Long id;
    @ManyToOne
    private User user;
    private Status status;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private String shippingAddress;
    @OneToMany
    private Set<OrderItem> items;
    @Column(name = "is_deleted",nullable = false)
    private boolean isDeleted = false;

    public enum Status {
        NEW,          // Нещодавно створене замовлення
        PENDING,      // Очікує на обробку
        PROCESSING,   // В процесі обробки
        SHIPPED,      // Відправлено
        DELIVERED,    // Доставлено
        COMPLETED,    // Завершено
        CANCELLED,    // Скасовано
        RETURNED,     // Повернено
        FAILED        // Не вдалося виконати
    }
}


