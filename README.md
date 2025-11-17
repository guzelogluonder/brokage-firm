# brokage-firm
Brokage Firm Case Project

## Tech Stack

- Java 21
- Spring Boot 3.5.7
- Spring Boot Starter Data JPA
- Spring Boot Starter Web
- Spring Boot Starter Validation
- Spring Boot Starter Test
- H2 Database (in-memory database)
- Lombok
- MapStruct 1.6.3
- Maven
- Docker

## Build and Run With Docker

- Preconditions
    - Docker must be installed and run in your Machine.
    - The docker-compose.yml file must be located in the project root directory.


- Navigate the project root Directory
    - Navigate to the folder where the docker-compose.yml file is located.

### Run The Project
  ```bash
  docker-compose up
  ```

- Access the App
    - Once the services are ready, you can access them according to the port defined in docker-compose.yml.
    - example: http://localhost:8080

## Default Credentials

### Admin User
- **Username**: `admin`
- **Password**: `admin123`
- **Role**: ADMIN

### Sample Customers
- **Customer 1**
    - Username: `customer1`
    - Password: `123456`
    - Initial TRY Balance: 100
    - Initial AAPL Shares: 10

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
  "size": 1,
  "price": 10
}

POST /api/orders
Content-Type: application/json
Authorization: Basic YWRtaW46YWRtaW4xMjM=

{
  "customerId": 1,
  "assetName": "AAPL",
  "orderSide": "SELL",
  "size": 1,
  "price": 10
}
```

**Order Side**: `BUY` or `SELL`

#### List Orders
```http
GET /api/orders?customerId=1
GET http://localhost:8080/api/orders?customerId=1&startDate=2025-01-01T00:00:00&endDate=2025-12-31T23:59:59
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


## Testing

### Run Unit Tests

```bash
mvn test
```

![img.png](src/main/resources/img.png)
