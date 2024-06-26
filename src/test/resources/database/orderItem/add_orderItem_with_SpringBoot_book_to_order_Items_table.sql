-- Витягуємо ID для замовлення
SELECT id INTO @order_id FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'john.doe@example.com') ORDER BY order_date DESC LIMIT 1;

-- Витягуємо ID для першої книги
SELECT id INTO @book_id FROM books WHERE isbn = '9781234567897';

INSERT INTO order_items (order_id, book_id, quantity, price)
VALUES (@order_id, @book_id, 2, 29.99);
