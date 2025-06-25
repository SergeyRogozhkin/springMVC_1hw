package org.example.springmvc_jsonview.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springmvc_jsonview.controller.UserOrderController;
import org.example.springmvc_jsonview.model.OrderStatus;
import org.example.springmvc_jsonview.model.User;
import org.example.springmvc_jsonview.model.UserOrder;
import org.example.springmvc_jsonview.service.UserOrderService;
import org.example.springmvc_jsonview.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserOrderController.class)
class UserOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserOrderService orderService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /v.1.0.0/api/orders/all - Should return all orders")
    void testGetAllOrders() throws Exception {
        UserOrder order1 = new UserOrder();
        order1.setProductName("Телефон");
        order1.setQuantity(1);
        order1.setTotalAmount(new BigDecimal("30000"));
        order1.setStatus(OrderStatus.ORDERED);

        UserOrder order2 = new UserOrder();
        order2.setProductName("Ноутбук");
        order2.setQuantity(1);
        order2.setTotalAmount(new BigDecimal("80000"));
        order2.setStatus(OrderStatus.DELIVERED);

        when(orderService.findAllOrders()).thenReturn(List.of(order1, order2));

        mockMvc.perform(get("/v.1.0.0/api/orders/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /v.1.0.0/api/orders/{id} - Should return order by id")
    void testGetOrderById() throws Exception {
        UserOrder order = new UserOrder();
        order.setProductName("Мышка");
        order.setQuantity(1);
        order.setTotalAmount(new BigDecimal("2000"));
        order.setStatus(OrderStatus.ORDERED);

        when(orderService.findOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/v.1.0.0/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Мышка"));
    }

    @Test
    @DisplayName("POST /v.1.0.0/api/orders/user/{userId} - Should create order")
    void testCreateOrder() throws Exception {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@example.com");

        UserOrder order = new UserOrder();
        order.setProductName("Монитор");
        order.setQuantity(2);
        order.setTotalAmount(new BigDecimal("40000"));
        order.setStatus(OrderStatus.ORDERED);

        UserOrder savedOrder = new UserOrder();
        savedOrder.setProductName("Монитор");
        savedOrder.setQuantity(2);
        savedOrder.setTotalAmount(new BigDecimal("40000"));
        savedOrder.setStatus(OrderStatus.ORDERED);
        savedOrder.setUser(user);

        when(userService.findUserById(1L)).thenReturn(user);
        when(orderService.saveOrder(any(UserOrder.class))).thenReturn(savedOrder);

        mockMvc.perform(post("/v.1.0.0/api/orders/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Монитор"))
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    @DisplayName("PUT /v.1.0.0/api/orders/{id} - Should update order")
    void testUpdateOrder() throws Exception {
        UserOrder existingOrder = new UserOrder();
        existingOrder.setProductName("Клавиатура");
        existingOrder.setQuantity(1);
        existingOrder.setTotalAmount(new BigDecimal("3000"));
        existingOrder.setStatus(OrderStatus.ORDERED);

        UserOrder updatedOrder = new UserOrder();
        updatedOrder.setProductName("Клавиатура механическая");
        updatedOrder.setQuantity(1);
        updatedOrder.setTotalAmount(new BigDecimal("5000"));
        updatedOrder.setStatus(OrderStatus.PAID);

        when(orderService.findOrderById(1L)).thenReturn(existingOrder);
        when(orderService.saveOrder(any(UserOrder.class))).thenReturn(updatedOrder);

        mockMvc.perform(put("/v.1.0.0/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Клавиатура механическая"))
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    @DisplayName("DELETE /v.1.0.0/api/orders/{id} - Should delete order")
    void testDeleteOrder() throws Exception {
        UserOrder order = new UserOrder();
        order.setProductName("Наушники");

        when(orderService.findOrderById(1L)).thenReturn(order);
        doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/v.1.0.0/api/orders/1"))
                .andExpect(status().isNoContent());
    }
}
