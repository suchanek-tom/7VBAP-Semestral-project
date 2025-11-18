# 📚 Library System - REST API

**Author:** Tomáš Suchanek | **Subject:** 7OPR3 | **Java:** 21 LTS | **Framework:** Spring Boot 3.5.7

---

## 🚀 Getting Started

```bash
./mvnw spring-boot:run
# Server runs on http://localhost:8080
```

---

## 📖 API Requests

### BOOKS API

| Method | Endpoint          | Description           |
| ------ | ----------------- | --------------------- |
| GET    | `/api/books`      | Get all books         |
| GET    | `/api/books/{id}` | Get book by ID        |
| POST   | `/api/books`      | Create a book         |
| POST   | `/api/books/bulk` | Create multiple books |
| PUT    | `/api/books/{id}` | Update a book         |
| DELETE | `/api/books/{id}` | Delete a book         |

**Example - Create a book:**

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "1984",
    "author": "George Orwell",
    "publicationYear": 1949,
    "isbn": "978-0451524935",
    "content": "Dystopian work",
    "available": true
  }'
```

---

### USERS API

| Method | Endpoint           | Description    |
| ------ | ------------------ | -------------- |
| GET    | `/api/users`       | Get all users  |
| GET    | `/api/users/{id}`  | Get user by ID |
| POST   | `/api/users`       | Create a user  |
| POST   | `/api/users/login` | User login     |
| PUT    | `/api/users/{id}`  | Update a user  |
| DELETE | `/api/users/{id}`  | Delete a user  |

**Example - Create a user:**

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John",
    "surname": "Smith",
    "email": "john@example.com",
    "address": "Street 123",
    "city": "Prague",
    "password": "password123",
    "role": "ROLE_USER"
  }'
```

**Example - Login:**

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jan@example.com",
    "password": "heslo123"
  }'
```

---

### LOANS API

| Method | Endpoint                 | Description    |
| ------ | ------------------------ | -------------- |
| GET    | `/api/loans`             | Get all loans  |
| GET    | `/api/loans/{id}`        | Get loan by ID |
| POST   | `/api/loans/borrow`      | Borrow a book  |
| POST   | `/api/loans/return/{id}` | Return a book  |
| PUT    | `/api/loans/{id}`        | Update a loan  |
| DELETE | `/api/loans/{id}`        | Delete a loan  |

**Example - Borrow a book:**

```bash
curl -X POST http://localhost:8080/api/loans/borrow \
  -H "Content-Type: application/json" \
  -d '{
    "user": { "id": 1 },
    "book": { "id": 1 }
  }'
```

**Example - Return a book:**

```bash
curl -X POST http://localhost:8080/api/loans/return/1
```

---

## 💾 H2 Database Console

**URL:** http://localhost:8080/h2-console  
**JDBC URL:** `jdbc:h2:mem:librarydb`  
**Username:** `sa`

---

## 📊 Technologies

- Java 21 LTS
- Spring Boot 3.5.7
- Spring Data JPA
- H2 Database
- Lombok
- Maven

---

**Status:** ✅ Complete | **Date:** November 8, 2025
