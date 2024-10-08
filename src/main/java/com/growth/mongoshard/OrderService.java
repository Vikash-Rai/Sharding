package com.growth.mongoshard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    //The sharding logic is handled by MongoDB at the database level, and the application code remains the same.

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order placeOrder(Order order) {
        return orderRepository.save(order);
    }
    
    // Get orders by customerId (shard key)
    public List<Order> findCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}

