# TradingApp – Poseidon Capital Solutions
A secure web application for managing financial transactions for Poseidon Capital Solutions.

## 📄 CI/CD Deliverables
All deliverables are generated through the CI/CD pipeline (built at every "Pull Request -> main")
- [Docker image](https://hub.docker.com/r/redikan7/trading_app)
- [JaCoCo report](https://mr-boubakour.github.io/-BOUBAKOUR-MohamedRedha-p7-secureApp-spring/javadocs/)
- [JavaDoc](https://mr-boubakour.github.io/-BOUBAKOUR-MohamedRedha-p7-secureApp-spring/jacoco/)

## 🚀 Technologies & Stack Used
- **Java 21 / Spring Boot 3.4.4**
- **Spring Data JPA** – entity management & database access
- **Spring Security** – session-based authentication (admin/user)
- **Validation** – numeric fields, password security, custom annotations
- **Exception Handling** – centralized management via `@ControllerAdvice`
- **MapStruct** –conversion between entities and DTOs
- **MySQL** – database
- **Junit / TestContainers** - unit & integration testing
- **Thymeleaf** – front-end integration

## 🔐 Security
- Authentication via HTTP session (no JWT)
- Access rules by role (`@PreAuthorize`, `SecurityFilter`, `SecurityUtils`)
- Password validation (PasswordEncoder - Bcrypt)

## ⚙️ **Core Features & Functionality**
- **Use cases handled**:
  - Prevention of the deletion of the last admin account.
  - Logged-in user when updating or deleting his account (logged out).

  
- **Role-specific functionalities**:
  - **Admin**:
    - Full access to perform any action on any account or entity (CRUD operations).
    - Can delete users, including other admins (except the last admin).
  - **User**:
    - Can only CRUD their own account (create, read, update, delete).
    - Can create new users but cannot update of delete other users.
   

- Full CRUD functionality for 6 financial entities.
- Separation between entities, DTOs, and mappers.

## 📦 Containerization & CI/CD
- **Docker**: multi-stage image for optimized execution
- **docker-compose**: local orchestration (app + DB)
- **GitHub Actions**:
  
    - Build, tests & generation of the JaCoCo report (unit + integration)
    - JavaDoc generation
    - Deployment to GitHub Pages for documentation
    - Automatic Docker image push to DockerHub

## ✅ Run the project locally
```bash

git clone https://github.com/MR-BOUBAKOUR/-BOUBAKOUR-MohamedRedha-p7-secureApp-spring.git
cd -BOUBAKOUR-MohamedRedha-p7-secureApp-spring
./mvnw clean install
docker-compose up --build

```
