# 📚 Library System - REST API Backend

**Autor:** Tomáš Suchanek | **Předmět:** 7OPR3 | **Java:** 21 LTS | **Framework:** Spring Boot 3.5.7

---

## 🚀 Spuštění

### Prerequisites

- Java 21 LTS
- MySQL 8.0+
- Maven 3.6+

### Setup Database

```bash
# Create database
mysql -u root -p
CREATE DATABASE librarydb;
EXIT;

# Run SQL schema
mysql -u root -p librarydb < database.sql
```

### Start Backend Server

```bash
cd library
./mvnw spring-boot:run
# Server běží na http://localhost:8080
```

### Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/librarydb
spring.datasource.username=root
spring.datasource.password=rootroot
```

---

## 📖 API Dokumentace

### 🔐 Authentication

All endpoints except `/api/users/login` and `/api/users/register` require JWT token:

```
Authorization: Bearer <jwt_token>
```

### BOOKS API

| Metoda | Endpoint          | Popis              | Auth |
| ------ | ----------------- | ------------------ | ---- |
| GET    | `/api/books`      | Všechny knihy      | ❌   |
| GET    | `/api/books/{id}` | Kniha podle ID     | ❌   |
| POST   | `/api/books`      | Vytvořit knihu     | ✅   |
| POST   | `/api/books/bulk` | Vytvořit více knih | ✅   |
| PUT    | `/api/books/{id}` | Aktualizovat knihu | ✅   |
| DELETE | `/api/books/{id}` | Smazat knihu       | ✅   |

**Příklad - Vytvořit knihu:**

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "title": "1984",
    "author": "George Orwell",
    "publicationYear": 1949,
    "isbn": "978-0451524935",
    "content": "Dystopické dílo"
  }'
```

---

### USERS API

| Metoda | Endpoint              | Popis                  | Auth     |
| ------ | --------------------- | ---------------------- | -------- |
| GET    | `/api/users`          | Všichni uživatelé      | ✅ Admin |
| GET    | `/api/users/{id}`     | Uživatel podle ID      | ✅       |
| POST   | `/api/users`          | Vytvořit uživatele     | ✅ Admin |
| POST   | `/api/users/login`    | Přihlášení             | ❌       |
| POST   | `/api/users/register` | Registrace             | ❌       |
| PUT    | `/api/users/{id}`     | Aktualizovat uživatele | ✅       |
| DELETE | `/api/users/{id}`     | Smazat uživatele       | ✅ Admin |

**Příklad - Registrace:**

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jan",
    "surname": "Novák",
    "email": "jan@example.com",
    "address": "Ulice 123",
    "city": "Praha",
    "password": "heslo123"
  }'
```

**Příklad - Login:**

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jan@example.com",
    "password": "heslo123"
  }'
```

Response:

```json
{
  "id": 1,
  "name": "Jan",
  "email": "jan@example.com",
  "role": "ROLE_USER",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### LOANS API

| Metoda | Endpoint                 | Popis                 | Auth     |
| ------ | ------------------------ | --------------------- | -------- |
| GET    | `/api/loans`             | Všechny výpůjčky      | ✅ Admin |
| GET    | `/api/loans/{id}`        | Výpůjčka podle ID     | ✅       |
| POST   | `/api/loans/borrow`      | Půjčit knihu          | ✅       |
| POST   | `/api/loans/return/{id}` | Vrátit knihu          | ✅       |
| PUT    | `/api/loans/{id}`        | Aktualizovat výpůjčku | ✅       |
| DELETE | `/api/loans/{id}`        | Smazat výpůjčku       | ✅       |

**Příklad - Půjčit knihu:**

```bash
curl -X POST http://localhost:8080/api/loans/borrow \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "userId": 1,
    "bookId": 1
  }'
```

**Příklad - Vrátit knihu:**

```bash
curl -X POST http://localhost:8080/api/loans/return/1 \
  -H "Authorization: Bearer <token>"
```

---

## 🛠️ Technologie

- **Java 21 LTS** - Nejnovější LTS verze
- **Spring Boot 3.5.7** - Framework
- **Spring Data JPA** - Database access
- **Spring Security** - Authentication & Authorization
- **MySQL 8.0** - Databáze
- **JWT (jjwt)** - Token-based authentication
- **BCrypt** - Password hashing
- **Lombok** - Boilerplate reduction
- **Maven** - Build management
- **SLF4J** - Logging

---

## 🔒 Bezpečnost

✅ **Password Security**

- BCrypt hashovací algoritmus
- Hesla se nekdy neukládají v plain textu
- Password encoding při registraci a update

✅ **Authentication & Authorization**

- JWT (JSON Web Token) based authentication
- Token expiration (24 hodin)
- Role-based access control (ROLE_USER, ROLE_ADMIN)
- Protected endpoints require valid token

✅ **Input Validation**

- Jakarta validation annotations
- Email validation
- Password length validation (min 6 chars)
- All inputs validated before database save

✅ **Error Handling**

- GlobalExceptionHandler s @ExceptionHandler
- Custom ErrorResponse DTO
- Proper HTTP status codes
- Detailed error messages

---

## 📊 Datový Model

### User

```java
- id: Long (PK)
- name: String (required, 2-100 chars)
- surname: String (required, 2-100 chars)
- email: String (required, unique, valid email)
- address: String (required, 5-255 chars)
- city: String (required, 2-100 chars)
- password: String (required, 6+ chars, hashed)
- role: Enum (ROLE_USER, ROLE_ADMIN)
```

### Book

```java
- id: Long (PK)
- title: String (required)
- author: String (required)
- content: String
- publicationYear: Integer (required, >= 1000)
- isbn: String (required, max 17 chars)
- available: Boolean (default: true)
```

### Loan

```java
- id: Long (PK)
- user: User (FK)
- book: Book (FK)
- loanDate: LocalDate
- returnDate: LocalDate (nullable)
- status: String (ACTIVE, RETURNED)
```

---

## 📝 Logování

Comprehensive logging na všech úrovních:

**Konfiguraci v `application.properties`:**

```properties
logging.level.com.example.library=DEBUG
logging.level.com.example.library.controller=INFO
logging.level.com.example.library.service=INFO
logging.file.name=logs/library.log
```

**Co se loguje:**

- ✅ Všechny HTTP requests (metoda, endpoint, user)
- ✅ Úspěšné operace (create, update, delete)
- ✅ Selhání (not found, validation errors, exceptions)
- ✅ Autentizace (login success, failed attempts)
- ✅ JWT token generation a validation
- ✅ Database operations

Log soubor: `logs/library.log` (10MB rotation, 10-day history)

---

## 🧪 Testing Endpoints

### s Bruno/Postman

Import kolekce z `OPR3-Library/` složky:

- `users/Login user.bru` - Login
- `users/Create new user.bru` - Create user
- `books/Get all books.bru` - Get books
- `books/Post one book.bru` - Create book
- `loans/Borrow a book.bru` - Borrow book
- `loans/Return a loan.bru` - Return book

---

## 🏗️ Vícevrstvá Architektura

```
Controller Layer
     ↓
Service Layer
     ↓
Repository Layer
     ↓
Database Layer
```

- **Controllers** - HTTP endpoints, request handling
- **Services** - Business logic, validation
- **Repositories** - Database queries (Spring Data JPA)
- **Models** - Entity definitions with JPA annotations
- **DTOs** - Data Transfer Objects (LoginRequest, LoginResponse)
- **Utils** - JWT utility, helpers
- **Config** - Spring Security, CORS, Bean configuration

---

## ⚙️ CORS Configuration

```java
Allowed Origins: http://localhost:5173
Allowed Methods: GET, POST, PUT, DELETE, OPTIONS
Allowed Headers: *
Max Age: 3600 seconds
```

---

**Status:** ✅ Hotovo | **Datum:** 8. prosince 2025
