# Brokerage Firm API

A Spring Boot-based REST API for a brokerage firm that allows employees to manage stock orders for their customers.

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.7**
- **Spring Security** (Basic Authentication)
- **Spring Data JPA**
- Spring Boot Starter Web
- Spring Boot Starter Validation
- Spring Boot Starter Test
- **H2 Database** (In-memory)
- **Lombok**
- MapStruct 1.6.3
- **Jakarta Bean Validation**
- **Maven**

## Default Credentials

### Admin User

- **Username**: `admin`
- **Password**: `admin123`
- **Role**: ADMIN

### Sample Customers

- **Customer 1**
    - Username: `customer1`
    - Password: `123456`
    - Initial TRY Balance: 100,000
    - Initial AAPL Shares: 50


## API Endpoints

### Authentication

All endpoints require Basic Authentication. Include credentials in the Authorization header:

```
Authorization: Basic base64(username:password)
```

### Order Endpoints

#### Create Order

```http
POST /api/orders
Content-Type: application/json
Authorization: Basic YWRtaW46YWRtaW4xMjM=

{
  "customerId": 1,
  "assetName": "AAPL",
  "orderSide": "BUY",
  "size": 10,
  "price": 150.50
}
```

**Order Side**: `BUY` or `SELL`

#### List Orders

```http
GET /api/orders?customerId=1
GET /api/orders?customerId=1&startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

#### Cancel Order

```http
DELETE /api/orders/{orderId}
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

**Note**: Only PENDING orders can be canceled.

### Asset Endpoints

#### List Customer Assets

```http
GET /api/assets?customerId=1
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

### Admin Endpoints

#### Match Order (Admin Only)

```http
POST /api/admin/match-order
Content-Type: application/json
Authorization: Basic YWRtaW46YWRtaW4xMjM=

{
  "orderId": 1
}
```

**Note**: This endpoint is restricted to ADMIN role.

## Database Access

H2 Console is available at: `http://localhost:8080/h2-console`

- **JDBC URL**: `jdbc:h2:mem:brokeragedb`
- **Username**: `sa`
- **Password**: `password`

## Business Logic

### Order Creation

**BUY Orders**:

- System checks if customer has sufficient TRY balance
- Required amount = size × price
- Amount is locked (deducted from usableSize) until order is matched or canceled

**SELL Orders**:

- System checks if customer has sufficient asset quantity
- Required quantity = size
- Shares are locked (deducted from usableSize) until order is matched or canceled

### Order Cancellation

- Only PENDING orders can be canceled
- Locked amounts are released back to usableSize
- Order status is updated to CANCELED

### Order Matching (Admin Only)

**BUY Order Matching**:

- Deducts TRY from customer's size
- Adds purchased shares to customer's asset (both size and usableSize)

**SELL Order Matching**:

- Deducts shares from customer's asset size
- Adds TRY revenue to customer's balance (both size and usableSize)

## Testing

### Run Unit Tests

```bash
mvn test
```

### Test Coverage

The project includes unit tests for:

- Order creation (BUY/SELL)
- Order cancellation
- Order matching
- Balance validation
- Error scenarios

### Manual Testing with cURL

#### Create a BUY order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -d '{
    "customerId": 1,
    "assetName": "AAPL",
    "orderSide": "BUY",
    "size": 5,
    "price": 150.00
  }'
```

#### List orders

```bash
curl -X GET "http://localhost:8080/api/orders?customerId=1" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

#### Cancel order

```bash
curl -X DELETE http://localhost:8080/api/orders/1 \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

#### Match order (Admin only)

```bash
curl -X POST http://localhost:8080/api/admin/match-order \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -d '{"orderId": 1}'
```

#### List assets

```bash
curl -X GET "http://localhost:8080/api/assets?customerId=1" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

## Project Structure

```
src/
├── main/
│   ├── java/com/brokerage/
│   │   ├── config/              # Security and data initialization
│   │   ├── controller/          # REST controllers
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── entity/              # JPA entities
│   │   ├── exception/           # Custom exceptions and handlers
│   │   ├── repository/          # Data access layer
│   │   ├── service/             # Business logic layer
│   │   └── BrokerageApiApplication.java
│   └── resources/
│       └── application.yml      # Application configuration
└── test/
    └── java/com/brokerage/
        └── service/             # Unit tests
```

## Error Handling

The API returns appropriate HTTP status codes and error messages:

- **200 OK**: Successful GET requests
- **201 Created**: Successful POST requests
- **204 No Content**: Successful DELETE requests
- **400 Bad Request**: Validation errors, insufficient balance, invalid status
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Unexpected errors

Example error response:

```json
{
  "status": 400,
  "message": "Insufficient TRY balance. Required: 1500.00, Available: 1000.00",
  "timestamp": "2024-01-15T10:30:00"
}
```

## Design Considerations

1. **Transaction Management**: All operations that modify multiple records use `@Transactional` to ensure atomicity
2. **Asset Locking**: The `usableSize` field represents available balance, while `size` represents total balance
3. **Security**: Basic authentication is implemented for simplicity, but can be extended with JWT tokens
4. **Validation**: Input validation using Jakarta Bean Validation annotations
5. **Error Handling**: Global exception handler for consistent error responses
6. **Logging**: SLF4J with Logback for application logging

## Future Enhancements

- JWT-based authentication
- Customer self-service endpoints with authorization filters
- Order book and market data
- Real-time order matching engine
- WebSocket support for live updates
- Audit logging
- Rate limiting
- API documentation with Swagger/OpenAPI

## Notes

- TRY is stored as an asset in the asset table (not a separate table)
- All orders are against TRY (you can only buy/sell with TRY)
- The H2 database is in-memory and will reset on application restart
- For production, configure a persistent database (PostgreSQL, MySQL, etc.)
