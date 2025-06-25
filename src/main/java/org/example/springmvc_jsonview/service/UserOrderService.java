package org.example.springmvc_jsonview.service;

import org.example.springmvc_jsonview.exception.ResourceNotFoundException;
import org.example.springmvc_jsonview.model.UserOrder;
import org.example.springmvc_jsonview.repository.UserOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOrderService {
    private final UserOrderRepository orderRepository;

    public UserOrderService(UserOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<UserOrder> findAllOrders() {
        return orderRepository.findAll();
    }

    public UserOrder findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден"));
    }

    public UserOrder saveOrder(UserOrder order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
