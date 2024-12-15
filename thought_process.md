# Thinking About Technology Choices

## Summary of Needs

1. **High Throughput**: The app should process at least 10,000 requests per second.
2. **RESTful Service**: A GET link to accept incoming requests with specific parameters.
3. **Logging and Deduplication**: Track unique request counts every minute and remove duplicate IDs from multiple instances.
4. **External Communication**: Send HTTP requests (GET/POST) with the unique count to a specified endpoint.
5. **Distributed Compatibility**: Ensure that deduplication is compatible with a load balancer.  
6. **Distributed Streaming**: Send unique ID counts to a distributed streaming service instead of logging.

### Key Technology Choices  
1. **Tomcat** as the web server and servlet container.  
2. **JPA (Java Persistence API)** for data management.  
3. **Kafka** as the distributed streaming service.

---

## Why Choose Tomcat?

### 1. **High Performance for REST APIs**
- Tomcat is a lightweight, high-performance servlet container that is suitable for high throughput when configured well. It is very well suited for serving RESTful APIs.
- It integrates with Spring Boot, which is perfect for developing production-ready microservices.

### 2. **Ease of Deployment**
- Tomcat requires minimal setup and configuration. This makes it a perfect fit for applications that need to be deployed quickly.

### 3. **Scalability**
- Tomcat can work in clusters and works well with a load balancer. This means it allows for horizontal scaling, which is important when handling more than 10,000 requests per second.

---

## Why JPA (Java Persistence API)?

### 1. **Efficient Deduplication and Persistence**
- JPA provides an easy way to interact with the database, which makes it very easy to store and get unique IDs for removing duplicates.
- Using a relational database (such as PostgreSQL or MySQL) with JPA assists in setting up primary key rules to ensure IDs are unique.
- Hibernate, being a JPA tool, provides for caching and better query execution to handle high-demand scenarios.

### 2. **Concurrency and Consistency**
- JPA, with a relational database that obeys ACID rules, ensures the data is consistent. This is important when removing duplicate IDs across multiple instances behind a load balancer.
- Optimistic locking and transactional annotations in JPA ensure that requests with the same ID are processed simultaneously without conflicts.

### 3. **Flexibility**
- JPA supports several types of databases, making it easy to use in other places. It also suits well with Spring Data to make the management of repositories easier.

---

## Why Kafka?

### 1. **High Throughput and Low Latency**
- Kafka is a distributed streaming platform for high-throughput data pipelines. It is particularly suitable for sending and processing the count of unique IDs in real-time.
- Partitioning and replication by Kafka ensure data availability and scalability.

### 2. **Durability and Fault Tolerance**
- Kafka provides message durability, and the unique count is not lost even in case of failure of the application or infrastructure.

### 3. **Ease of Integration**
- Kafka connects elegantly with Spring Boot using the Spring Kafka library, thereby easily configuring producers and consumers.

### 4. **Future-Proofing**
- Using Kafka provides ample room for extending an application into analytics, real-time monitoring, or incorporation with other microservices, all of which can be done with minimal rework.

---
# Design Choices for Extensions
### Extension 1: Firing POST Requests
- Modification to use POST requests involves changing the request body to unique count with JSON as a data structure.
- POST is better for sending structured data than GET.

### Extension 2: Compatibility with a Load Balancer
-To avoid duplicate data across several instances:
  - Use a shared database with a unique rule on the ID column. This avoids duplicates even if the same ID is processed by several instances.
  - Or, use a distributed in-memory cache such as Redis with atomic operations (e.g., `SETNX`) for fast deduplication.

### Extension 3: Distributed Streaming with Kafka
- Change the logging system to a Kafka producer that sends the unique count to a Kafka topic.
- A special consumer can handle these messages later, allowing more analysis or visualization processes.
- Kafka's distributed design makes sure it is always available and reliable, even when there is a lot of demand.

---

## Summary
- **Tomcat**: A small, flexible, and fast option for serving REST APIs.
- **JPA**: Provides strong persistence and flexible deduplication of IDs.
- **Kafka**: A platform of distributed streaming that suits processing in real time, is scalable for the future.

In this application, a combination of these technologies would enable the fulfillment of high-throughput requirements, reliability, distributed compatibility, and extensibility.

