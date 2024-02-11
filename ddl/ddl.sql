-- 사장님
CREATE TABLE manager (
    manager_id BIGINT NOT NULL AUTO_INCREMENT,
    phone_number VARCHAR(20),
    password VARCHAR(255),
    created_at DATETIME,
    last_modified_at DATETIME,
    deleted_at DATETIME,
    PRIMARY KEY (manager_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 가게
CREATE TABLE store (
    store_id BIGINT NOT NULL AUTO_INCREMENT,
    manager_id BIGINT,
    company_registration_number VARCHAR(50),
    business_name VARCHAR(50),
    created_at DATETIME,
    last_modified_at DATETIME,
    deleted_at DATETIME,
    PRIMARY KEY (store_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 카테고리
CREATE TABLE category (
    category_id BIGINT NOT NULL AUTO_INCREMENT,
    store_id BIGINT,
    name VARCHAR(20),
    created_at DATETIME,
    last_modified_at DATETIME,
    deleted_at DATETIME,
    PRIMARY KEY (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 상품
CREATE TABLE product (
    product_id BIGINT NOT NULL AUTO_INCREMENT,
    store_id BIGINT,
    price_amount DECIMAL(38,2),
    price_currency VARCHAR(3),
    cost_amount DECIMAL(38,2),
    cost_currency VARCHAR(3),
    name VARCHAR(50),
    description TEXT,
    barcode VARCHAR(50),
    expiration_date DATETIME,
    size ENUM('SMALL', 'MEDIUM', 'LARGE'),
    created_at DATETIME,
    last_modified_at DATETIME,
    deleted_at DATETIME,
    PRIMARY KEY (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 상품 카테고리
CREATE TABLE product_category (
    product_category_id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT,
    category_id BIGINT,
    created_at DATETIME,
    last_modified_at DATETIME,
    deleted_at DATETIME,
    PRIMARY KEY (product_category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 상품 이름
CREATE TABLE product_name (
    product_name_id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT,
    name VARCHAR(50),
    created_at DATETIME,
    last_modified_at DATETIME,
    deleted_at DATETIME,
    PRIMARY KEY (product_name_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Naming 규칙: idx_테이블명_컬럼명_컬럼명

-- manager 테이블
CREATE UNIQUE INDEX idx_manager_phone_number ON manager(phone_number);

-- store 테이블
CREATE UNIQUE INDEX idx_store_company_registration_number ON store(company_registration_number);
CREATE INDEX idx_store_store_id_manager_id ON store(store_id, manager_id);

-- 카테고리 테이블
CREATE INDEX idx_category_store_id ON category(store_id);

-- 상품 테이블
CREATE INDEX idx_product_store_id ON product(store_id);
CREATE UNIQUE INDEX idx_product_name ON product(name);
CREATE INDEX idx_product_store_id_name ON product(store_id, name);

-- 상품 카테고리 테이블
CREATE INDEX idx_product_category_product_id ON product_category(product_id);
CREATE INDEX idx_product_category_category_id ON product_category(category_id);
CREATE INDEX idx_product_category_product_id_category_id ON product_category(product_id, category_id);

-- 상품 이름 테이블
CREATE INDEX idx_product_name_product_id ON product_name(product_id);
CREATE INDEX idx_product_name_product_id_name ON product_name(product_id, name);