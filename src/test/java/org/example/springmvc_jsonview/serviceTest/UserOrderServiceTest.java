package org.example.springmvc_jsonview.serviceTest;

import org.example.springmvc_jsonview.exception.ResourceNotFoundException;
import org.example.springmvc_jsonview.model.UserOrder;
import org.example.springmvc_jsonview.repository.UserOrderRepository;
import org.example.springmvc_jsonview.service.UserOrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserOrderServiceTest {

    @Mock
    private UserOrderRepository orderRepository;

    @InjectMocks
    private UserOrderService orderService;

    @Test
    void testFindAllOrders() {
        List<UserOrder> orders = List.of(new UserOrder(), new UserOrder());
        when(orderRepository.findAll()).thenReturn(orders);

        List<UserOrder> result = orderService.findAllOrders();

        assertEquals(2, result.size());
        verify(orderRepository).findAll();
    }

    @Test
    void testFindOrderById_OrderExists() {
        UserOrder order = new UserOrder();
        order.setProductName("Test Product");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        UserOrder result = orderService.findOrderById(1L);

        assertEquals(order, result);
        verify(orderRepository).findById(1L);
    }

    @Test
    void testFindOrderById_OrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.findOrderById(1L);
        });

        assertEquals("Пользователь с id 1 не найден", exception.getMessage());
    }

    @Test
    void testSaveOrder() {
        UserOrder order = new UserOrder();
        order.setProductName("Phone");

        when(orderRepository.save(order)).thenReturn(order);

        UserOrder saved = orderService.saveOrder(order);

        assertEquals("Phone", saved.getProductName());
        verify(orderRepository).save(order);
    }

    @Test
    void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(orderRepository).deleteById(1L);
    }
}