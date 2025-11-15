INSERT INTO assets (id, customer_id, asset_name, size, usable_size)
VALUES (RANDOM_UUID(), '1', 'TRY', 100, 100),
       (RANDOM_UUID(), '1', 'APPL', 10, 10);

INSERT INTO customer (customer_id,username,password,role)
VALUES (RANDOM_UUID(), 'onderguzeloglu', '$2a$10$eMaI1ln5gMoPnrHtDE1sEOHDlIVIQIMP8wSqa13qmnlRt6B9rqUYm','CUSTOMER'),
       (RANDOM_UUID(), 'admin', '$2a$10$JO5rYIu6hQPUfnk2nmyTi.0PCt2jWEZXzY57bh.iXYxE9m1RIAVri', 'ADMIN');
