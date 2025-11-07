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

## 👏 Author
**Riya Agarwal**  
Java Developer | Spring Boot | Microservices | REST API Enthusiast

GitHub: [@Riyaaaaa22](https://github.com/Riyaaaaa22)

---
