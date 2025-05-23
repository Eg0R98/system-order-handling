-- Создание последовательности (sequence) для генерации уникальных значений ID пользователей
-- Если последовательность уже существует — повторно не создается
CREATE SEQUENCE IF NOT EXISTS user_id_seq
    START WITH 1            -- Начальное значение
    INCREMENT BY 1;         -- Шаг увеличения

-- Создание таблицы "userEntities", если она ещё не существует
CREATE TABLE IF NOT EXISTS users (
   id BIGINT NOT NULL,                      -- Уникальный идентификатор пользователя
   username VARCHAR NOT NULL,               -- Имя пользователя (логин)
   password VARCHAR NOT NULL,               -- Хэш пароля
   role VARCHAR NOT NULL,                   -- Роль (например, USER, ADMIN)

   -- Установка первичного ключа на поле id
   CONSTRAINT pk_users PRIMARY KEY (id),

   -- Уникальность имени пользователя (нельзя дважды зарегистрировать одного и того же логина)
   CONSTRAINT UC_USERS_USERNAME UNIQUE (username),

   -- Уникальность email (один email — один пользователь)
   CONSTRAINT UC_USERS_EMAIL UNIQUE (email)
);