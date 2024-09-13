package com.growth.mongoshard;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String customerId;
    private String coffeeType;
    private int quantity;
}

