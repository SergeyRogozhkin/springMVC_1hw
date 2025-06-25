package org.example.springmvc_jsonview.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import org.example.springmvc_jsonview.dto.Views;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
public class UserOrder {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.UserDetails.class)
    private Long id;
    @JsonView(Views.UserDetails.class)
    private String productName;
    @JsonView(Views.UserDetails.class)
    private int quantity;
    @JsonView(Views.UserDetails.class)
    private BigDecimal totalAmount;
    @JsonView(Views.UserDetails.class)
    private OrderStatus status;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }
}
