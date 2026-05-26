-- =============================================================================
-- Интернет-магазин — справочный файл для сдачи
-- =============================================================================
-- Это НЕ скрипт автоматической загрузки в SQLite.
-- Реальные данные создаёт Java-класс EntityInitializer при запуске backend
-- с аргументом --populate.
-- Файл БД: backend/data/shop.db
-- =============================================================================

-- Учётные записи (пароли хранятся в БД в виде BCrypt-хеша):
--   admin   / Admin123  — MANAGER
--   ivanov  / User1234  — USER
--   petrova / User1234  — USER

-- Категории (4):
--   Электроника, Дом, Спорт, Книги

-- Товары (6):
--   Смартфон Nova X, Наушники AirBeat, Кофеварка Morning,
--   Рюкзак City 25, Java для начинающих, Умная лампа Glow

-- Связи:
--   Product M:N Category через product_category
--   User 1:N Product, User 1:N Review, User 1:N Rating
--   Product 1:N Review, Product 1:N Rating

-- Отзывы и оценки (6):
--   пользователи оставляют отзывы с оценками 1..5;
--   для пары user/product допускается только один review и один rating.

-- Схема соответствует сущностям:
--   user_info, product, category, product_category,
--   review, rating, refresh_token
