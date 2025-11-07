# 📚 Library Book Management System

A Spring Boot REST API to manage a small library’s books, borrowers, and borrowing lifecycle — designed with clean architecture, realistic constraints, and relational behavior.

---

## 🧩 Tech Stack
- **Java 17**
- **Spring Boot 3.5.8-SNAPSHOT**
- **Spring Data JPA**
- **H2 Database (In-Memory)**
- **Lombok**
- **Spring Validation (Jakarta)**

---

## 🏗️ Architecture Overview
```
controller → service → repository → entity → dto → exception
```

**Layers:**
- **Controller:** Exposes REST APIs
- **Service:** Business logic and transaction handling
- **Repository:** Database CRUD and queries
- **Entity:** JPA models and relationships
- **DTO:** Data transfer objects for clean responses
- **Exception:** Centralized error handling using `@ControllerAdvice`

---

## 📘 Core Entities

### **Book**
- id (UUID)
- title
- author
- category
- totalCopies
- availableCopies
- isAvailable

### **Borrower**
- id (UUID)
- name
- email
- membershipType (BASIC / PREMIUM)
- maxBorrowLimit

### **BorrowRecord**
- id (UUID)
- bookId
- borrowerId
- borrowDate
- dueDate (borrowDate + 14 days)
- returnDate
- fineAmount


---

## ⚙️ Functional APIs

### **Book Management**
| Method | Endpoint | Description |
|---------|-----------|--------------|
| POST | `/api/books` | Add new book or increase total copies |
| GET | `/api/books` | List books with optional filters & pagination |
| PUT | `/api/books/{id}` | Update metadata or available copies |
| DELETE | `/api/books/{id}` | Soft delete if no active borrow |

### **Borrower Management**
| Method | Endpoint | Description |
|---------|-----------|--------------|
| POST | `/api/borrowers` | Register new borrower |
| GET | `/api/borrowers/{id}/records` | Get borrow history |
| GET | `/api/borrowers/overdue` | List overdue borrowers |

### **Borrowing Workflow**
| Method | Endpoint | Description |
|---------|-----------|--------------|
| POST | `/api/borrow` | Borrow a book |
| POST | `/api/return` | Return a borrowed book |
| GET | `/api/records/active` | List currently borrowed books |

---

## ✅ Validation Rules
- Borrower cannot exceed `maxBorrowLimit`.
- Book must have `availableCopies > 0` before borrowing.
- Fine = `(days_late * finePerDay)`.
- Borrow and return operations handled atomically with `@Transactional`.

---


## ⚡ Getting Started
### 1️⃣ Clone the repository
```
git clone https://github.com/Riyaaaaa22/library-book-management-system.git
```

### 2️⃣ Import into IntelliJ / Eclipse
- Open as **Maven project**
- Ensure Lombok plugin is enabled

### 3️⃣ Run the application
```
mvn spring-boot:run
```

### 4️⃣ H2 Database Console
```
http://localhost:8080/h2-console
```
JDBC URL: `jdbc:h2:mem:testdb`

---

## 📄 API Examples

### Add a Book
```json
POST /api/books
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "category": "Tech",
  "totalCopies": 3
}
```

### Borrow a Book
```json
POST /api/borrow
{
  "bookId": "uuid-book",
  "borrowerId": "uuid-borrower"
}
```

### Return a Book
```json
POST /api/return
{
  "bookId": "uuid-book",
  "borrowerId": "uuid-borrower"
}
```

---
## 🧭 My Approach, Design Decisions, and Thought Process


### 1) Problem Framing → Entities → Relationships
- Mapped **Book ⇄ BorrowRecord ⇄ Borrower** using `@OneToMany`/`@ManyToOne` to reflect the real borrowing lifecycle.
- Kept **`availableCopies`** derived/guarded by service logic so it never exceeds `totalCopies`.

### 2) Layered Architecture & Responsibilities
- **Controller:** thin, validates input and delegates to services.
- **Service:** encapsulates domain rules (borrow limit, availability, due date, fine calculation) and marks **borrow/return** as `@Transactional` (isolation raised for concurrency safety).
- **Repository:** JPA interfaces and a couple of JPQL queries for filtering/analytics.
- **DTOs:** decoupled external API from internal entities to keep responses stable.

### 3) API Design & Usability
- **Filtering & pagination** on `/books` with category/availability + page/size.
- Predictable error shape via `@ControllerAdvice` so clients can reliably parse failures.

### 4) Validation & Error Handling
- Request DTOs annotated with **Jakarta Validation** (`@NotBlank`, `@NotNull`, `@Min`).
- Domain errors use explicit exception types (`NotFound`, `IllegalState`) → handled centrally.
- Consistent JSON error body with `timestamp`, `status`, `error`, `message`.
- 
### 5) Dev Experience
- **H2 in-memory** for instant startup and repeatable tests; sample data in `data.sql` for quick manual verification.
- **Lombok** to reduce boilerplate.

---


## 🧪 Key Design Trade-offs
- **H2 vs. MySQL/Postgres:** chose H2 for speed in evaluation; RDBMS can be plugged by changing the driver & URL.
- **DTO mapping:** simple manual mapping for now; MapStruct can be added for scale.
- **Transactions:** coarse-grained at service level to keep rules atomic and readable.
---


## 🧱 Challenges Faced & How I Resolved Them

1) **Consistency of `availableCopies` under concurrency**
**Approach:** Wrapped borrow/return in `@Transactional`; updated counters only through services; optional isolation bump to prevent race conditions in high contention.


2) **Clean error responses for validation vs. domain errors**
**Approach:** Central `@ControllerAdvice` with handlers for `MethodArgumentNotValidException`, `NotFoundException`, and `IllegalStateException` to keep API errors uniform and debuggable.


3) **Search + pagination without overcomplicating**
**Approach:** Spring Data `Pageable` + a lightweight JPQL query that conditionally filters by `category` and `availability` while supporting page/size.


5) **Maintaining clarity in DTO vs. Entity**
**Approach:** Explicit DTOs for requests/responses, keeping entities persistence-focused and preventing accidental overexposure of internal fields.

## 👏 Author
**Riya Agarwal**  
Java Developer | Spring Boot | Microservices | REST API Enthusiast

GitHub: [@Riyaaaaa22](https://github.com/Riyaaaaa22)

---
