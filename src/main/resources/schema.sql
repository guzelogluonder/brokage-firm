-- Asset Table
CREATE TABLE assets
(
    id          VARCHAR(36) PRIMARY KEY,
    customer_id VARCHAR(36),
    asset_name  VARCHAR(255),
    size        INT,
    usable_size INT
);

-- Orders Table
CREATE TABLE orders
(
    order_id    VARCHAR(36) PRIMARY KEY,
    customer_id VARCHAR(36),
    asset_name  VARCHAR(255),
    order_side  VARCHAR(10),
    size        VARCHAR(255),
    price       DOUBLE PRECISION,
    status      VARCHAR(20),
    create_date TIMESTAMP
);
CREATE TABLE customer
(
    customer_id VARCHAR(36) PRIMARY KEY,
    username   VARCHAR(255),
    password    VARCHAR(255),
    role        VARCHAR(255)

);
