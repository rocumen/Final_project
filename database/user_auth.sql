CREATE DATABASE IF NOT EXISTS inventory_system
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE inventory_system;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS suppliers;

CREATE TABLE IF NOT EXISTS users (
    user_id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'staff',
    full_name VARCHAR(100) NOT NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    UNIQUE KEY uq_users_username (username)
);

CREATE TABLE IF NOT EXISTS suppliers (
    supplier_id INT NOT NULL AUTO_INCREMENT,
    supplier_name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100) DEFAULT NULL,
    phone VARCHAR(30) DEFAULT NULL,
    email VARCHAR(100) DEFAULT NULL,
    address VARCHAR(255) DEFAULT NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (supplier_id),
    UNIQUE KEY uq_suppliers_name (supplier_name)
);

CREATE TABLE IF NOT EXISTS items (
    item_id INT NOT NULL AUTO_INCREMENT,
    item_name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    sku VARCHAR(50) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    low_stock_threshold INT NOT NULL DEFAULT 10,
    unit_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    supplier_id INT DEFAULT NULL,
    description VARCHAR(255) DEFAULT NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (item_id),
    UNIQUE KEY uq_items_sku (sku),
    KEY idx_items_category (category),
    KEY idx_items_supplier_id (supplier_id),
    CONSTRAINT fk_items_supplier
        FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

INSERT INTO users (username, password, role, full_name, is_active)
VALUES
    ('admin', 'admin123', 'admin', 'System Administrator', 1),
    ('staff1', 'staff123', 'staff', 'Sample Staff', 1)
ON DUPLICATE KEY UPDATE
    password = VALUES(password),
    role = VALUES(role),
    full_name = VALUES(full_name),
    is_active = VALUES(is_active);

INSERT INTO suppliers (supplier_name, contact_person, phone, email, address, is_active)
VALUES
    ('ABC Trading', 'Maria Santos', '09171234567', 'abc.trading@example.com', 'Manila City', 1),
    ('Prime Supply Co.', 'John Reyes', '09181234567', 'prime.supply@example.com', 'Quezon City', 1),
    ('North Star Distributors', 'Angela Cruz', '09191234567', 'northstar@example.com', 'Caloocan City', 1)
ON DUPLICATE KEY UPDATE
    contact_person = VALUES(contact_person),
    phone = VALUES(phone),
    email = VALUES(email),
    address = VALUES(address),
    is_active = VALUES(is_active);

INSERT INTO items (item_name, category, sku, stock, low_stock_threshold, unit_price, supplier_id, description, is_active)
VALUES
    ('Bond Paper A4', 'Office Supplies', 'SKU-1001', 120, 20, 250.00, 1, 'A4 bond paper 80gsm', 1),
    ('Ballpen Blue', 'Office Supplies', 'SKU-1002', 80, 15, 12.50, 1, 'Blue ink ballpen', 1),
    ('Printer Ink Black', 'Printing', 'SKU-1003', 8, 10, 850.00, 2, 'Black printer ink cartridge', 1),
    ('Notebook Large', 'School Supplies', 'SKU-1004', 45, 10, 55.00, 3, '200-page notebook', 1),
    ('Stapler Heavy Duty', 'Office Equipment', 'SKU-1005', 6, 8, 320.00, 2, 'Heavy duty stapler', 1)
ON DUPLICATE KEY UPDATE
    item_name = VALUES(item_name),
    category = VALUES(category),
    stock = VALUES(stock),
    low_stock_threshold = VALUES(low_stock_threshold),
    unit_price = VALUES(unit_price),
    supplier_id = VALUES(supplier_id),
    description = VALUES(description),
    is_active = VALUES(is_active);
