# Order Processing System — Flow Overview

This document explains the order lifecycle implemented by this project,
including the REST endpoint responsible for each transition.

```
                    +--------------------------+
                    |       ORDER CREATED       |
                    |  POST /api/orders          |
                    |  status = CREATED          |
                    +--------------------------+
                                |
                                v
                    +--------------------------+
                    |   NOTIFICATION SENT       |
                    |  NotificationService fires |
                    |  an "order created" event  |
                    |  (log / email placeholder) |
                    +--------------------------+
                                |
                                v
                    +--------------------------+
                    |       PROCESSING          |
                    |  PATCH /api/orders/{id}/  |
                    |         process           |
                    |  status = PROCESSING       |
                    +--------------------------+
                                |
                                v
                    +--------------------------+
                    | SHIPPED (TRACKING ADDED)  |
                    |  PATCH /api/orders/{id}/  |
                    |          ship             |
                    |  status = SHIPPED          |
                    |  trackingNo set            |
                    +--------------------------+
                                |
                                v
                    +--------------------------+
                    |        DELIVERED          |
                    |  PATCH /api/orders/{id}/  |
                    |        deliver            |
                    |  status = DELIVERED        |
                    +--------------------------+
```

## Cancellation

An order can be cancelled instead of continuing down the happy path, but only
while it is still early in its lifecycle:

```
                    +--------------------------+
                    |        CANCELLED          |
                    |  PATCH /api/orders/{id}/  |
                    |         cancel            |
                    |  status = CANCELLED        |
                    +--------------------------+
```

- Reachable from **CREATED** — nothing has happened yet, safe to cancel.
- Reachable from **PROCESSING** — order hasn't shipped yet, still cancellable.
- **Not** reachable from **SHIPPED** — once physically shipped, this becomes a
  return, not a cancellation, and is out of scope for this project.
- **Not** reachable from **DELIVERED** or an already-**CANCELLED** order — both
  are terminal states.

## Valid status transitions (full picture)

```
CREATED    -> PROCESSING, CANCELLED
PROCESSING -> SHIPPED, CANCELLED
SHIPPED    -> DELIVERED
DELIVERED  -> (terminal, no further transitions)
CANCELLED  -> (terminal, no further transitions)
```

Any attempt at a transition outside this table is rejected by the service
layer and throws `InvalidStatusTransitionException`, handled centrally by
`GlobalExceptionHandler` (returns HTTP 400 with an error body).

## Layers involved per request

```
HTTP request
   |
   v
OrderController        (translates HTTP <-> service calls, validates input shape)
   |
   v
OrderService/Impl       (business rules: valid transitions, triggers notifications)
   |
   v
OrderRepository         (Spring Data JPA — persistence)
   |
   v
H2 in-memory database
```

## Where notifications fit in

`NotificationService` is called by `OrderServiceImpl` right after an order
is created. It is defined as an interface so the concrete implementation
(currently just logging, via `LoggingNotificationService`) can later be
swapped for a real email/queue-based sender without touching the service
layer.
