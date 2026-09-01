# Employee Data Management & Batch Processing System

A production-style Spring Boot application designed to manage employee data through Excel file uploads. The system provides secure OAuth2 authentication, role-based authorization, asynchronous file processing using RabbitMQ, chunk-based processing using Spring Batch, audit logging using Spring AOP, and administrative monitoring capabilities.

---

## 🚀 Project Overview

The Employee Data Management & Batch Processing System allows authenticated users to upload employee data in Excel (`.xlsx`) format.

The uploaded file is validated and stored, after which an event is published to RabbitMQ. A RabbitMQ consumer receives the event and triggers a Spring Batch job to process employee records asynchronously.

The system supports two user roles:

- **USER** – Upload employee files and monitor their own processing results.
- **ADMIN** – Monitor users, uploads, batch jobs, failed records, and audit logs.

---

## 🏗️ Architecture

```text
                           ┌─────────────────┐
                           │      User       │
                           └────────┬────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │    OAuth2 / Google   │
                         │         SSO          │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │   Spring Security    │
                         │ Authentication/Role  │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │   REST Controllers   │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │      Services        │
                         └──────────┬───────────┘
                                    │
                    ┌───────────────┼────────────────┐
                    │               │                │
                    ▼               ▼                ▼
              ┌──────────┐    ┌──────────┐    ┌──────────┐
              │PostgreSQL│    │ RabbitMQ │    │Spring AOP│
              └──────────┘    └────┬─────┘    └──────────┘
                                   │
                                   ▼
                          ┌────────────────┐
                          │ Spring Batch   │
                          │ Excel Process  │
                          └────────┬───────┘
                                   │
                                   ▼
                          ┌────────────────┐
                          │ Employee Data  │
                          │ Failed Records │
                          └────────────────┘
```

---

# ✨ Features

## 🔐 Authentication & Authorization

- Google OAuth2 / SSO login
- Spring Security integration
- Role-based authorization
- `ROLE_USER`
- `ROLE_ADMIN`
- Protected REST APIs
- User account activation/deactivation
- Current authenticated user retrieval

---

## 📤 Excel File Upload

Authenticated users can upload employee data using Excel files.

Supported format:

```text
.xlsx
```

Required columns:

| Employee ID | Name | Email | Department | Salary |
|---|---|---|---|---|
| 101 | Rahul | rahul@gmail.com | IT | 50000 |
| 102 | Priya | priya@gmail.com | HR | 45000 |
| 103 | Arun | arun@gmail.com | FINANCE | 55000 |

---

## ✅ Employee Validation

The application validates employee records based on the following rules:

- Employee ID is mandatory
- Employee ID must be unique
- Name is mandatory
- Email must have a valid format
- Department must be valid
- Salary must be greater than zero

Supported departments:

```text
IT
HR
FINANCE
SALES
MARKETING
```

---

## 🐇 RabbitMQ Event Processing

RabbitMQ is used to decouple file upload from batch processing.

### Processing Flow

```text
Excel Upload
     │
     ▼
File Validation
     │
     ▼
File Metadata Stored
     │
     ▼
EMPLOYEE_FILE_UPLOADED Event
     │
     ▼
RabbitMQ Exchange
     │
     ▼
employee.upload.queue
     │
     ▼
Event Consumer
     │
     ▼
Spring Batch Job
```

Example event:

```json
{
  "uploadId": 1001,
  "fileName": "employees.xlsx",
  "uploadedBy": "user@example.com",
  "eventType": "EMPLOYEE_FILE_UPLOADED"
}
```

---

## ⚙️ Spring Batch Processing

Employee Excel files are processed asynchronously using Spring Batch.

### Batch Pipeline

```text
Excel File
    │
    ▼
EmployeeExcelReader
    │
    ▼
EmployeeProcessor
    │
    ▼
Validation
    │
    ├───────────────┐
    │               │
    ▼               ▼
Valid Record    Invalid Record
    │               │
    ▼               ▼
EmployeeWriter  FailedEmployeeRecord
    │               │
    ▼               ▼
PostgreSQL      Error Storage
```

### Batch Statuses

```text
UPLOADED
PROCESSING
COMPLETED
COMPLETED_WITH_ERRORS
FAILED
```

---

## 📊 Admin Monitoring

Administrators can:

- View all users
- Activate users
- Deactivate users
- View all file uploads
- Monitor batch processing
- View failed employee records
- View audit logs
- Monitor processing statistics

---

## 🔍 Spring AOP

The application uses Aspect-Oriented Programming for cross-cutting concerns.

### Logging Aspect

Logs important service method execution.

### Execution Time Aspect

Measures method execution time.

Example:

```text
Method: uploadEmployeeFile
Execution Time: 245 ms
```

### Audit Aspect

Stores audit information such as:

- User
- Action
- Method
- Execution time
- Status
- Timestamp

---

# 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming Language |
| Spring Boot | Application Framework |
| Spring Web | REST APIs |
| Spring Data JPA | Database Persistence |
| Hibernate | ORM |
| PostgreSQL | Database |
| Spring Security | Authentication & Authorization |
| OAuth2 | Google SSO Login |
| Spring Batch | Batch Processing |
| RabbitMQ | Asynchronous Messaging |
| Apache POI | Excel Processing |
| Spring AOP | Logging & Auditing |
| Swagger/OpenAPI | API Documentation |
| Maven | Build Tool |
| Lombok | Boilerplate Reduction |
| JUnit 5 | Unit Testing |
| Mockito | Mocking Framework |

---

# 📁 Project Structure

```text
employee-management
│
├── src
│   ├── main
│   │
│   │── java
│   │   └── com.bridgelabz.employeemanagement
│   │
│   │       ├── EmployeeManagementApplication.java
│   │
│   │       ├── config
│   │       │   ├── OpenApiConfig.java
│   │       │   └── AppConfig.java
│   │
│   │       ├── controller
│   │       │   ├── AuthController.java
│   │       │   ├── EmployeeController.java
│   │       │   ├── UserController.java
│   │       │   └── AdminController.java
│   │
│   │       ├── service
│   │       │   ├── UserService.java
│   │       │   ├── EmployeeService.java
│   │       │   ├── FileUploadService.java
│   │       │   ├── BatchService.java
│   │       │   └── AuditService.java
│   │
│   │       ├── service.impl
│   │       │   ├── UserServiceImpl.java
│   │       │   ├── EmployeeServiceImpl.java
│   │       │   ├── FileUploadServiceImpl.java
│   │       │   ├── BatchServiceImpl.java
│   │       │   └── AuditServiceImpl.java
│   │
│   │       ├── entity
│   │       │   ├── User.java
│   │       │   ├── Employee.java
│   │       │   ├── FileUpload.java
│   │       │   ├── FailedEmployeeRecord.java
│   │       │   └── AuditLog.java
│   │
│   │       ├── enums
│   │       │   ├── Role.java
│   │       │   ├── UserStatus.java
│   │       │   ├── BatchStatus.java
│   │       │   └── Department.java
│   │
│   │       ├── repository
│   │       │   ├── UserRepository.java
│   │       │   ├── EmployeeRepository.java
│   │       │   ├── FileUploadRepository.java
│   │       │   ├── FailedEmployeeRecordRepository.java
│   │       │   └── AuditLogRepository.java
│   │
│   │       ├── dto
│   │       │   ├── request
│   │       │   │   ├── EmployeeRequestDTO.java
│   │       │   │   └── FileUploadRequestDTO.java
│   │       │   │
│   │       │   └── response
│   │       │       ├── UserResponseDTO.java
│   │       │       ├── EmployeeResponseDTO.java
│   │       │       ├── FileUploadResponseDTO.java
│   │       │       ├── FailedRecordResponseDTO.java
│   │       │       └── BatchStatusResponseDTO.java
│   │
│   │       ├── batch
│   │       │   ├── EmployeeBatchConfig.java
│   │       │   ├── EmployeeExcelReader.java
│   │       │   ├── EmployeeProcessor.java
│   │       │   ├── EmployeeWriter.java
│   │       │   ├── BatchJobListener.java
│   │       │   └── BatchFailureListener.java
│   │
│   │       ├── rabbitmq
│   │       │   ├── RabbitMQConfig.java
│   │       │   ├── EmployeeFileUploadedEvent.java
│   │       │   ├── EmployeeEventProducer.java
│   │       │   └── EmployeeEventConsumer.java
│   │
│   │       ├── security
│   │       │   ├── SecurityConfig.java
│   │       │   ├── CustomOAuth2UserService.java
│   │       │   └── OAuth2LoginSuccessHandler.java
│   │
│   │       ├── aspect
│   │       │   ├── LoggingAspect.java
│   │       │   ├── ExecutionTimeAspect.java
│   │       │   └── AuditAspect.java
│   │
│   │       ├── exception
│   │       │   ├── GlobalExceptionHandler.java
│   │       │   ├── ResourceNotFoundException.java
│   │       │   ├── FileProcessingException.java
│   │       │   └── UnauthorizedException.java
│   │
│   │       └── util
│   │           ├── ExcelValidator.java
│   │           └── SecurityUtil.java
│   │
│   └── resources
│       └── application.properties
│
├── uploads
│
├── pom.xml
│
└── README.md
```

---

# 🗄️ Database Design

## Users

```text
users
```

| Field | Description |
|---|---|
| id | Primary Key |
| name | User Name |
| email | User Email |
| provider | OAuth Provider |
| role | USER / ADMIN |
| status | ACTIVE / INACTIVE |
| created_at | Creation Timestamp |
| updated_at | Update Timestamp |

---

## File Upload

```text
file_upload
```

| Field | Description |
|---|---|
| id | Primary Key |
| file_name | Uploaded File Name |
| file_path | File Storage Path |
| uploaded_by | User |
| total_records | Total Records |
| success_records | Successfully Processed |
| failed_records | Failed Records |
| status | Batch Status |
| created_at | Upload Time |
| completed_at | Completion Time |

---

## Employee

```text
employee
```

| Field | Description |
|---|---|
| id | Primary Key |
| employee_id | Employee Identifier |
| name | Employee Name |
| email | Employee Email |
| department | Department |
| salary | Salary |
| upload_id | File Upload Reference |
| created_at | Creation Time |

---

## Failed Employee Records

```text
failed_employee_records
```

| Field | Description |
|---|---|
| id | Primary Key |
| upload_id | Upload Reference |
| row_number | Excel Row Number |
| employee_data | Invalid Employee Data |
| error_message | Validation Error |
| created_at | Creation Time |

---

## Audit Log

```text
audit_log
```

| Field | Description |
|---|---|
| id | Primary Key |
| user_id | User Reference |
| action | Performed Action |
| method | Method Name |
| request_time | Request Start Time |
| response_time | Response End Time |
| execution_time | Execution Duration |
| status | SUCCESS / FAILED |
| created_at | Creation Time |

---

# 🔌 API Endpoints

## Authentication

| Method | Endpoint | Description |
|---|---|---|
| GET | `/oauth2/authorization/google` | Login using Google OAuth2 |
| GET | `/logout` | Logout current user |

---

## User APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/users/me` | Get current user |
| GET | `/api/users/uploads` | Get user's uploads |
| GET | `/api/users/uploads/{id}/status` | Get batch status |
| GET | `/api/users/uploads/{id}/errors` | Get failed records |

---

## Employee APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/employees/upload` | Upload Employee Excel File |

---

## Admin APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/admin/users` | Get all users |
| PUT | `/api/admin/users/{id}/activate` | Activate user |
| PUT | `/api/admin/users/{id}/deactivate` | Deactivate user |
| GET | `/api/admin/uploads` | Get all uploads |
| GET | `/api/admin/batches` | Monitor batch jobs |
| GET | `/api/admin/audit-logs` | View audit logs |

---

# ⚙️ Setup Instructions

## 1. Clone Repository

```bash
git clone <repository-url>
cd employee-management
```

---

## 2. Create PostgreSQL Database

```sql
CREATE DATABASE employee_management;
```

---

## 3. Configure PostgreSQL

Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/employee_management
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## 4. Configure RabbitMQ

Start RabbitMQ using Docker:

```bash
docker run -d --name rabbitmq ^
-p 5672:5672 ^
-p 15672:15672 ^
rabbitmq:management
```

RabbitMQ Management Dashboard:

:contentReference[oaicite:0]{index=0}

Default credentials:

```text
Username: guest
Password: guest
```

RabbitMQ configuration:

```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

---

## 5. Configure Google OAuth2

Add your Google OAuth credentials to `application.properties`:

```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=openid,profile,email
```

OAuth login URL:

```text
http://localhost:8080/oauth2/authorization/google
```

---

## 6. Run the Application

Using Maven:

```bash
mvn clean install
mvn spring-boot:run
```

Or run:

```text
EmployeeManagementApplication.java
```

from IntelliJ IDEA.

---

# 📖 Swagger API Documentation

After starting the application, open:

:contentReference[oaicite:1]{index=1}

Swagger provides interactive API documentation and API testing capabilities.

---

# 🔄 Complete Application Flow

```text
1. User Opens Application
          │
          ▼
2. Google OAuth2 Login
          │
          ▼
3. User Created / Retrieved from PostgreSQL
          │
          ▼
4. Role Assigned (USER / ADMIN)
          │
          ▼
5. User Uploads Excel File
          │
          ▼
6. Excel File Validation
          │
          ▼
7. File Stored in uploads/
          │
          ▼
8. FileUpload Metadata Stored
          │
          ▼
9. RabbitMQ Event Published
          │
          ▼
10. RabbitMQ Consumer Receives Event
          │
          ▼
11. Spring Batch Job Starts
          │
          ▼
12. Excel Reader Reads Records
          │
          ▼
13. Processor Validates Records
          │
          ├───────────────┐
          │               │
          ▼               ▼
     Valid Record     Invalid Record
          │               │
          ▼               ▼
     Employee DB     Failed Records DB
          │               │
          └───────┬───────┘
                  ▼
          Update Batch Status
                  │
                  ▼
         Audit Logs Generated
```

---

# 🔐 Role-Based Access

| Feature | USER | ADMIN |
|---|---|---|
| OAuth2 Login | ✅ | ✅ |
| Upload Excel | ✅ | ✅ |
| View Own Uploads | ✅ | ✅ |
| View Own Errors | ✅ | ✅ |
| View All Users | ❌ | ✅ |
| Activate User | ❌ | ✅ |
| Deactivate User | ❌ | ✅ |
| View All Uploads | ❌ | ✅ |
| Monitor Batches | ❌ | ✅ |
| View Audit Logs | ❌ | ✅ |

---

# 🧪 Testing

The project supports testing using:

- JUnit 5
- Mockito
- Spring Boot Test
- Controller Tests
- Service Tests
- Batch Tests
- RabbitMQ Integration Tests

Test structure:

```text
src/test/java
│
└── com.bridgelabz.employeemanagement
    ├── service
    ├── controller
    ├── batch
    └── rabbitmq
```

Run tests:

```bash
mvn test
```

---

# 📌 Future Enhancements

- JWT support for REST API clients
- Email notification after batch completion
- Download processing reports
- Dead Letter Queue support
- Retry mechanism for failed RabbitMQ messages
- Batch dashboard with statistics
- Pagination and filtering
- File size restrictions
- Redis caching
- Kubernetes deployment

---

# 👨‍💻 Author

**Saravanan**

Java Full Stack Developer

---

# 📄 License

This project is developed for educational and learning purposes.

---

## ⭐ Key Learning Outcomes

This project demonstrates practical implementation of:

- Spring Boot layered architecture
- REST API development
- Spring Security
- OAuth2 / SSO authentication
- Role-based authorization
- PostgreSQL with Spring Data JPA
- RabbitMQ messaging
- Asynchronous processing
- Spring Batch
- Apache POI Excel processing
- Spring AOP
- Audit logging
- Swagger/OpenAPI documentation
- Exception handling
- DTO pattern
- Unit testing with JUnit and Mockito