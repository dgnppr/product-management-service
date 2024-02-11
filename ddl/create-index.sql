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
