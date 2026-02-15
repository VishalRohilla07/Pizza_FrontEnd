-- Sample data for pizza ordering system
-- This file can be used to populate initial data

-- Note: Passwords are BCrypt hashed
-- admin123 = $2a$10$xQxZ8Z8Z8Z8Z8Z8Z8Z8Z8uK5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y
-- user123 = $2a$10$yRyR9R9R9R9R9R9R9R9R9uL6Z6Z6Z6Z6Z6Z6Z6Z6Z6Z6Z6Z6Z6Z6Z

-- Insert sample users (run these manually or use a data initialization class)
-- INSERT INTO users (name, email, password, role, created_at, updated_at) VALUES
-- ('Admin User', 'admin@pizza.com', '$2a$10$xQxZ8Z8Z8Z8Z8Z8Z8Z8Z8uK5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y', 'ADMIN', NOW(), NOW()),
-- ('John Doe', 'user@pizza.com', '$2a$10$yRyR9R9R9R9R9R9R9R9R9uL6Z6Z6Z6Z6Z6Z6Z6Z6Z6Z6Z6Z6Z6Z6Z', 'CUSTOMER', NOW(), NOW());

-- Sample pizzas
INSERT INTO pizzas (name, description, price, category, image_url, available, created_at, updated_at) VALUES
('Margherita', 'Classic tomato sauce, fresh mozzarella, basil leaves, and extra virgin olive oil', 12.99, 'VEG', 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=500&h=500&fit=crop', true, NOW(), NOW()),
('Pepperoni Feast', 'Loaded with premium pepperoni, mozzarella cheese, and our signature tomato sauce', 15.99, 'NON_VEG', 'https://images.unsplash.com/photo-1628840042765-356cda07504e?w=500&h=500&fit=crop', true, NOW(), NOW()),
('Garden Veggie', 'Bell peppers, mushrooms, onions, olives, tomatoes on a bed of mozzarella', 13.99, 'VEG', 'https://images.unsplash.com/photo-1511689660979-10d2b1aada49?w=500&h=500&fit=crop', true, NOW(), NOW()),
('BBQ Chicken', 'Grilled chicken, smoky BBQ sauce, red onions, cilantro, and mozzarella', 16.99, 'NON_VEG', 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&h=500&fit=crop', true, NOW(), NOW()),
('Four Cheese', 'Mozzarella, parmesan, gorgonzola, and ricotta with garlic cream sauce', 14.99, 'VEG', 'https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500&h=500&fit=crop', true, NOW(), NOW()),
('Meat Lovers', 'Pepperoni, Italian sausage, bacon, ham, and ground beef with extra cheese', 18.99, 'NON_VEG', 'https://images.unsplash.com/photo-1594007654729-407eedc4be65?w=500&h=500&fit=crop', true, NOW(), NOW()),
('Mushroom Truffle', 'Wild mushrooms, truffle oil, fontina cheese, and fresh thyme', 17.99, 'VEG', 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?w=500&h=500&fit=crop', true, NOW(), NOW()),
('Spicy Diavola', 'Spicy salami, chili flakes, roasted peppers, and mozzarella', 15.99, 'NON_VEG', 'https://images.unsplash.com/photo-1604382355076-af4b0eb60143?w=500&h=500&fit=crop', false, NOW(), NOW());
