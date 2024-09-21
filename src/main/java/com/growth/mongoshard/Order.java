package com.growth.mongoshard;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String customerId;// Used as shard key for both hashed and zone sharding
    private String coffeeType;
    private int quantity;
}

