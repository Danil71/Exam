# Интернет-магазин: товары, отзывы и оценки

Full-stack SPA для варианта «Интернет-магазин. Продажа товаров, отзывы».

## Архитектура

- **Backend:** Spring Boot 3, REST API, JPA, SQLite (`backend/data/shop.db`)
- **Frontend:** React 19 + Vite 6 (SPA), Bootstrap 5, MobX, ESLint

## Сущности

| Сущность | Описание |
|----------|----------|
| User | Учётная запись (логин, email, пароль, роль) |
| Product | Товар магазина |
| Review | Отзыв пользователя к товару |
| Rating | Оценка товара от 1 до 5 |
| Purchase | Покупка товара пользователем |
| Category | Категория товара |

Связи: User 1→N Product/Purchase/Review/Rating; Product 1→N Purchase/Review/Rating; Product M↔N Category.

## Страницы

| Страница | URL | Фильтрация (сервер) |
|----------|-----|---------------------|
| Вход | `/login` | — |
| Регистрация | `/register` | — |
| **A — Мои покупки** | `/my-purchases` | список купленных товаров |
| **B — Каталог товаров** | `/products` | название, продавец, категория, минимальная средняя оценка |
| Детали товара | `/products/:id` | покупка товара; отзыв доступен только после покупки |
| Админ: товары, категории, пользователи | `/admin/*` | только MANAGER |

## Запуск

### Backend

```bash
cd backend
# Требуется Java 21 и Maven
```

Backend API: http://localhost:8080/api/v1  
Swagger: http://localhost:8080/swagger-ui/index.html

Docker Compose передаёт аргумент `--populate`, поэтому тестовые данные создаются при первом запуске пустой БД. Для чистой БД удалите `backend/data/shop.db`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Приложение: http://localhost:5173

```bash
npm run lint
npm run build
```

## Тестовые учётные записи

| Логин | Пароль | Роль |
|-------|--------|------|
| admin | Admin123 | MANAGER |
| ivanov | User1234 | USER |
| petrova | User1234 | USER |

`backend/data/seed.sql` — описание тестовых данных для отчёта; загрузка в БД выполняется `EntityInitializer` при аргументе `--populate`.

## Файлы данных

- База SQLite: `backend/data/shop.db` (создаётся при запуске)
- Справочный SQL: `backend/data/seed.sql`

## Переменные окружения

JWT-ключ задаётся в `backend/src/main/resources/application.properties` (`com.software.software-development.jwt.secret-key`).
