CREATE TABLE IF NOT EXISTS orders (
   id UUID NOT NULL,
   user_id BIGINT NOT NULL,
   CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS products (
   id UUID NOT NULL,
   name VARCHAR,
   discounted_price DECIMAL,
   total_value_with_discount DECIMAL,
   quantity INTEGER,
   sale DECIMAL,
   order_id UUID,
   CONSTRAINT pk_products PRIMARY KEY (id),
   CONSTRAINT fk_products_orders FOREIGN KEY (order_id) REFERENCES orders(id)
);