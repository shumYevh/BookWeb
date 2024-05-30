package org.example.bookweb.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Setter
@Getter
@SQLDelete(sql = "UPDATE orders SET is_deleted = true WHERE id=?")
@SQLRestriction(value = "is_deleted=false")
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private String shippingAddress;
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private Set<OrderItem> orderItems;
    @Column(name = "is_deleted",nullable = false)
    private boolean isDeleted = false;

    public enum Status {
        NEW,
        PENDING,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        COMPLETED,
        CANCELLED,
        RETURNED,
        FAILED;

        public static Status fromString(String strStatus) {
            for (Status status : Status.values()) {
                if (status.name().equalsIgnoreCase(strStatus)) {
                    return status;
                }
            }
            throw new IllegalArgumentException(
                    "No enum constant " + Status.class.getCanonicalName()
                            + " for value " + strStatus);
        }

        @Override
        public String toString() {
            return name();
        }
    }
}
