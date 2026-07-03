-- Enable UUID extension for PostgreSQL
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================
-- users table
-- ============================================
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);

-- ============================================
-- categories table
-- userId = NULL => global category
-- userId NOT NULL => personal category
-- ============================================
CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    icon VARCHAR(50) NOT NULL,
    user_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_categories_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_categories_user_id ON categories(user_id);

-- ============================================
-- products table
-- ============================================
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    category_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(10) NOT NULL CHECK (unit IN ('KG', 'UNIT', 'LITER')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);

CREATE INDEX idx_products_user_id ON products(user_id);
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_name ON products(name);

-- ============================================
-- price_entries table
-- Cascade delete on product deletion
-- ============================================
CREATE TABLE price_entries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    store VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_price_entries_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE INDEX idx_price_entries_product_id ON price_entries(product_id);
CREATE INDEX idx_price_entries_date ON price_entries(date);

-- ============================================
-- Seed: Default global categories (userId = NULL)
-- ============================================
INSERT INTO categories (name, icon, user_id, created_at, updated_at) VALUES
    ('Fruits & Légumes', 'apple', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Viande & Poisson', 'meat', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Produits Laitiers', 'cheese', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Boulangerie', 'bread', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Boissons', 'cup', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Surgelés', 'snowflake', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Epicerie', 'shopping-cart', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Non-alimentaire', 'package', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
