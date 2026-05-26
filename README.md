# Телефонный справочник организации

Вариант госэкзамена по дисциплине «Интернет-программирование»: подразделения, сотрудники, телефонные номера.

## Архитектура

- **Backend:** Spring Boot 3, REST API, JPA, SQLite (`backend/data/phonebook.db`)
- **Frontend:** React 19 + Vite 6 (SPA), Bootstrap 5, MobX, ESLint

## Сущности

| Сущность | Описание |
|----------|----------|
| User | Учётная запись (логин, email, пароль, роль) |
| Department | Подразделение |
| Employee | Сотрудник (ФИО, должность, отдел) |
| PhoneNumber | Телефон (номер, тип, добавочный) |

Связи: Department 1→N Employee; Employee M↔N PhoneNumber.

## Страницы

| Страница | URL | Фильтрация (сервер) |
|----------|-----|---------------------|
| Вход | `/login` | — |
| Регистрация | `/register` | — |
| **A — Мой отдел** | `/my-employees` | ФИО, отдел, должность, телефон, добавочный (диапазон) |
| **B — Справочник** | `/directory` | ФИО, отдел, должность, телефон, сотрудник (owner) |
| Детали сотрудника | `/employees/:id` | — |
| Админ: отделы, сотрудники, телефоны, пользователи | `/admin/*` | только MANAGER |

## Запуск

### Backend

```bash
cd backend
# Требуется Java 21 и Maven
```

**PowerShell (Windows):**

```powershell
cd backend
mvn spring-boot:run "-Dspring-boot.run.arguments=--populate"
```

**cmd / Git Bash / Linux:**

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--populate
```

Без тестовых данных:

```powershell
mvn spring-boot:run
```

API: http://localhost:8080/api/v1  
Swagger: http://localhost:8080/swagger-ui/index.html

Флаг `--populate` создаёт тестовые данные при первом запуске.

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
| admin | Admin123 | MANAGER (Федорова Елена, IT-отдел) |
| ivanov | User1234 | USER (Иванов Иван, IT-отдел) |
| petrova | User1234 | USER (Петрова Анна, HR) |

`backend/data/seed.sql` — описание тестовых данных для отчёта; загрузка в БД выполняется флагом `--populate`, а не этим SQL-файлом.

## Файлы данных

- База SQLite: `backend/data/phonebook.db` (создаётся при запуске)
- Справочный SQL: `backend/data/seed.sql`

## Переменные окружения

JWT-ключ задаётся в `backend/src/main/resources/application.properties` (`com.software.software-development.jwt.secret-key`).
