SELECT id INTO @user_id FROM users WHERE email = 'john.doe@example.com';

-- Вставка запису в таблицю Order
INSERT INTO orders (user_id, status, total, order_date, shipping_address, is_deleted)
VALUES (@user_id, 'NEW', 100.00, '2024-06-10 12:34:56', '123 Main St', false);