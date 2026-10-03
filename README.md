# Order Processing API

A REST API for managing the lifecycle of customer orders — from creation through processing, shipping, delivery, and cancellation — built with Spring Boot, Spring Data JPA, and H2.

## Overview

This project demonstrates a small but complete order management backend, including:

- Layered architecture (Controller → Service → Repository)
- Entity/DTO separation with request validation
- A status state machine enforcing valid order transitions
- Centralized exception handling with clean, consistent error responses
- An extensible notification hook triggered on order creation

## Tech Stack

- **Java 21**
- **Spring Boot 4.1**
- **Spring Web** (REST API)
- **Spring Data JPA** (persistence)
- **H2 Database** (in-memory, zero-config)
- **Bean Validation** (`jakarta.validation`)
- **Lombok** (reduces boilerplate)
- **Maven** (build tool, via the included Maven Wrapper)

## Order Lifecycle

```
CREATED → PROCESSING → SHIPPED → DELIVERED
   |            |
   └──────► CANCELLED 
```

- An order can be cancelled while `CREATED` or `PROCESSING`.
- Once `SHIPPED`, an order can only move to `DELIVERED`.
- `DELIVERED` and `CANCELLED` are terminal states — no further transitions are allowed.
- Any invalid transition attempt returns a `400 Bad Request` with a descriptive error message.

See [ORDER_FLOW.md](./ORDER_FLOW.md) for a full diagram of the flow and layer responsibilities.

## API Endpoints

| Method | Endpoint                     | Description                                  |
|--------|-------------------------------|-----------------------------------------------|
| POST   | `/api/orders`                 | Create a new order                            |
| GET    | `/api/orders/{orderId}`       | Fetch a single order by id                    |
| PATCH  | `/api/orders/{orderId}/process` | Move an order to `PROCESSING`               |
| PATCH  | `/api/orders/{orderId}/ship`    | Move an order to `SHIPPED`, sets tracking # |
| PATCH  | `/api/orders/{orderId}/deliver` | Move an order to `DELIVERED`                |
| PATCH  | `/api/orders/{orderId}/cancel`  | Cancel an order (if eligible)                |

### Example — create an order

```
POST /api/orders
Content-Type: application/json

{
  "item": "Laptop",
  "quantity": 1,
  "customerName": "Name",
  "customerEmail": "Name@example.com",
  "price": 999.99,
  "address": "123 Main St"
}
```

### Example — ship an order

```
PATCH /api/orders/1/ship
Content-Type: application/json

{
  "trackingNo": "TRACK01234"
}
```

## Getting Started

### Prerequisites

- JDK 21
- No separate Maven installation needed — the project includes the Maven Wrapper (`mvnw` / `mvnw.cmd`)

### Run the application

```bash
# Windows
mvnw.cmd spring-boot:run

# Mac/Linux
./mvnw spring-boot:run
```

Or, in IntelliJ IDEA: open `OrderappApplication.java` and click the run icon next to `main()`.

The app starts on **http://localhost:8080**.

### Testing the endpoints

Sample requests are available as an IntelliJ HTTP Client file (`*.http`) in the project, covering create, get, process, ship, deliver, and cancel — or import the endpoints above into Postman.

### H2 Console (optional)

If enabled in `application.properties`, the in-memory database can be inspected at:
```
http://localhost:8080/h2-console
```

## Project Structure

```
src/main/java/com/example/orderapp/
├── controller/      REST endpoints
├── service/         Business logic and status-transition rules
├── repository/      Spring Data JPA repository
├── data/            Order entity and OrderStatus enum
├── dto/             Request/response data transfer objects
├── notification/    Order-created notification hook
├── exception/        Custom exceptions and global exception handler
└── config/
```

## Possible Next Steps

- Persist to a real database (PostgreSQL/MySQL) instead of in-memory H2
- Support multiple line items per order
- Replace the logging-based notification with real email/queue integration
- Add unit and integration tests (JUnit + Mockito)
