# MongoDB Sharding Setup

This document outlines the steps to set up sharding in MongoDB.

## Setting up and Adding Shards

```javascript 
// Create directories for your config server
mkdir -p /data/configdb

// Start the config server
mongod --configsvr --port 27019 --dbpath /data/configdb --replSet configReplSet

// Once the config server is running, connect to it:
mongosh --port 27019

// Initialize the replica set
rs.initiate()

// Create directories for shards
mkdir -p /data/shard1 /data/shard2

// Connect to shard 1
mongosh --port 27018
rs.initiate()

// Connect to shard 2
mongosh --port 27017
rs.initiate()

// Start shard 1
mongod --shardsvr --port 27018 --dbpath /data/shard1 --replSet shard1ReplSet

// Start shard 2
mongod --shardsvr --port 27017 --dbpath /data/shard2 --replSet shard2ReplSet

// Start the mongos router
mongos --configdb configReplSet/localhost:27019 --port 27020

// Now, connect to the mongos instance and add both shards to the cluster:
mongo --port 27020

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

## You can choose any of the sharding techniques below. In the blog, we have used range-based sharding as an example.

## 1. For Range sharding
// Split the collection at a specific value
sh.splitAt("coffeeShop.orders", { "customerId": 100 })

// Move the chunk to the first shard
sh.moveChunk("coffeeShop.orders", { "customerId": MinKey() }, "shard1ReplSet")

// Move the chunk to the second shard
sh.moveChunk("coffeeShop.orders", { "customerId": 100 }, "shard2ReplSet")

## 2. For Hashed sharding
// Shard the collection with a hashed shard key on 'customerId'. The shard key customerId is hashed to distribute the data across shards. This ensures even distribution but does not guarantee that a specific range of customerId values will be placed in the same shard.

sh.shardCollection("coffeeShop.orders", { "customerId": "hashed" });

## 3. Zone-based Sharding
// Zone-based sharding can be configured to ensure that certain ranges of customerId values are stored in specific shards.
// Define the zones

sh.addShardTag("shard1ReplSet", "ZoneA");
sh.addShardTag("shard2ReplSet", "ZoneB");

// Enable zone-based sharding on the collection

sh.updateZoneKeyRange("coffeeShop.orders", { "customerId": MinKey }, { "customerId": 5000 }, "ZoneA");
sh.updateZoneKeyRange("coffeeShop.orders", { "customerId": 5000 }, { "customerId": MaxKey }, "ZoneB");

## We are using shar
// Get shard distribution
db.orders.getShardDistribution()

// Check the balancer state
sh.getBalancerState()

// Get the sharding status
sh.status()

## Insert data into 'orders' collection via mongo shell
db.orders.insertMany([
  { "id": "1", "customerId": 50, "coffeeType": "Espresso", "quantity": 3 },
  { "id": "2", "customerId": 100, "coffeeType": "Latte", "quantity": 2 },
  { "id": "3", "customerId": 150, "coffeeType": "Mocha", "quantity": 1 },
  { "id": "4", "customerId": 200, "coffeeType": "Cappuccino", "quantity": 4 },
  { "id": "5", "customerId": 250, "coffeeType": "Black Coffee", "quantity": 5 }
])

## Insert data into "orders" collection via hitting api from postman
curl --location 'http://localhost:8080/api/orders' \
--header 'Content-Type: application/json' \
--data '{
    "customerId": 71,
    "coffeeType": "Espresso",
    "quantity": 2
}'


