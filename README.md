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

