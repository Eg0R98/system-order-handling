-- Создание таблицы товаров, если она не существует
CREATE TABLE IF NOT EXISTS products (
  id UUID NOT NULL,                 -- id продукта в формате UUID
  name VARCHAR NOT NULL,            -- Название продукта, не может быть пустым
  price DECIMAL NOT NULL,           -- Цена продукта, не может быть пустой
  quantity INTEGER,                 -- Количество продукта на складе (может быть NULL)
  sale DECIMAL,                    -- Размер скидки (например, 0.10 для 10%), может быть NULL
  CONSTRAINT pk_products PRIMARY KEY (id),  -- Первичный ключ по полю id
  CONSTRAINT UC_PRODUCTS_NAME UNIQUE (name) -- Уникальное ограничение на название продукта
);