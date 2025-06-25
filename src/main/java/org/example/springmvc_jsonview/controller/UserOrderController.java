package org.example.springmvc_jsonview.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.springmvc_jsonview.dto.Views;
import org.example.springmvc_jsonview.model.User;
import org.example.springmvc_jsonview.model.UserOrder;
import org.example.springmvc_jsonview.service.UserOrderService;
import org.example.springmvc_jsonview.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v.1.0.0/api/orders")
public class UserOrderController {

    private final UserOrderService orderService;
    private final UserService userService;

    public UserOrderController(UserOrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/all")
    @JsonView(Views.UserDetails.class)
    public List<UserOrder> getAllOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping("/{id}")
    @JsonView(Views.UserDetails.class)
    public ResponseEntity<UserOrder> getOrderById(@PathVariable Long id) {
        UserOrder order = orderService.findOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<UserOrder> createOrder(@PathVariable Long userId, @RequestBody UserOrder order) {
        User user = userService.findUserById(userId);
        order.setUser(user);
        UserOrder savedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok(savedOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserOrder> updateOrder(@PathVariable Long id, @RequestBody UserOrder orderDetails) {
        UserOrder order = orderService.findOrderById(id);
        order.setProductName(orderDetails.getProductName());
        order.setQuantity(orderDetails.getQuantity());
        order.setTotalAmount(orderDetails.getTotalAmount());
        order.setStatus(orderDetails.getStatus());
        UserOrder updatedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.findOrderById(id);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}