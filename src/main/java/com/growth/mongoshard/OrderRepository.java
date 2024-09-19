package com.growth.mongoshard;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
    // Find orders by customerId (shard key)
    List<Order> findByCustomerId(String customerId);
}

