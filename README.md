# MongoDB Sharding Setup

This document outlines the steps to set up sharding in MongoDB.

## Adding Shards

```javascript
// Add the first shard
sh.addShard("shard1ReplSet/localhost:27018")

// Add the second shard
sh.addShard("shard2ReplSet/localhost:27017")

// Switch to the desired database
use coffeeShop

// Enable sharding on the database
sh.enableSharding("coffeeShop")

// Shard the 'orders' collection on the 'customerId' field
sh.shardCollection("coffeeShop.orders", { "customerId": 1 })

// Split the collection at a specific value
sh.splitAt("coffeeShop.orders", { "customerId": 100 })

// Move the chunk to the first shard
sh.moveChunk("coffeeShop.orders", { "customerId": MinKey() }, "shard1ReplSet")

// Move the chunk to the second shard
sh.moveChunk("coffeeShop.orders", { "customerId": 100 }, "shard2ReplSet")

// Get shard distribution
db.orders.getShardDistribution()

// Check the balancer state
sh.getBalancerState()

// Get the sharding status
sh.status()

// Insert data into 'orders' collection
db.orders.insertMany([
  { "id": "1", "customerId": 50, "coffeeType": "Espresso", "quantity": 3 },
  { "id": "2", "customerId": 100, "coffeeType": "Latte", "quantity": 2 },
  { "id": "3", "customerId": 150, "coffeeType": "Mocha", "quantity": 1 },
  { "id": "4", "customerId": 200, "coffeeType": "Cappuccino", "quantity": 4 },
  { "id": "5", "customerId": 250, "coffeeType": "Black Coffee", "quantity": 5 }
])


