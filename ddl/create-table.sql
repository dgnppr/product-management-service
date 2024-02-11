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
