# 🎮 Let's Play API
Hey I'm Timothée Sylla and I made Let's Play : 

Let's Play is a robust, secure, and production-ready **RESTful CRUD API** built using **Spring Boot** and **MongoDB**. The application provides full user management, product catalogs, and state-of-the-art security mechanisms implementing JSON Web Tokens (JWT) along with Role-Based Access Control (RBAC) and data-ownership validation layers.

---

## 🚀 Key Features

- **Full CRUD Support**: Complete lifecycle management for `User` and `Product` entities.
- **JWT-Based Authentication**: Secure stateless authentication using custom filter pipelines (`JwtFilter`).
- **Granular Authorization (RBAC)**: Secure routes distinguished by user roles (`USER`, `ADMIN`).
- **Strict Data Ownership Validation**: Users can only modify or delete products they created (`@PreAuthorize` validation via `ProductService#isOwner`). Administrative accounts bypass ownership limits.
- **Automated User Linking**: Products are automatically mapped to the authenticated session context's User ID during creation.
- **Secure Password Hashing**: Passwords stored safely using cryptographic hashing via `PasswordEncoder`.

---

## 📂 Project Architecture

Based on the implemented structure, the project follows clean MVC/DDD layered decoupling design principles:

```text
lets-play/
├── gradle/
├── src/
│   ├── main/
│   │   ├── java/com/lets_play/lets_play/
│   │   │   ├── config/
│   │   │   │   ├── JwtFilter.java         # Intercepts requests & validates JWT signatures
│   │   │   │   ├── JwtUtil.java           # Generates, parses, and reads claims from tokens
│   │   │   │   └── MongoConfig.java       # Database setup and custom configurations
│   │   │   ├── controllers/
│   │   │   │   ├── ProductController.java # Endpoints for product catalog actions
│   │   │   │   └── UserController.java    # Endpoints for Auth, Registration & Management
│   │   │   ├── models/
│   │   │   │   ├── Product.java           # Product schema/document template
│   │   │   │   └── User.java              # User credentials, profiles, and roles schema
│   │   │   ├── repositories/
│   │   │   │   ├── ProductRepository.java # Product MongoDB Data Access Layer
│   │   │   │   └── UserRepository.java    # User MongoDB Data Access Layer
│   │   │   ├── security/
│   │   │   │   └── SecurityConfig.java    # Spring Security URL filters and permission setup
│   │   │   ├── services/
│   │   │   │   └── ProductService.java    # Ownership verification business logic
│   │   │   └── LetsPlayApplication.java   # Spring Boot Application bootstrap entrypoint
│   │   └── resources/
│   │       └── application.properties     # App configuration profiles & connection strings
└── build.gradle
```

---

## 🛠️ Prerequisites & Installation

Before spinning up the application locally, ensure you have the following software installed:
- **Java Development Kit (JDK)**: Version 17 or higher
- **MongoDB**: An active local instance running on port `27017` (or a remote MongoDB Atlas connection URI)
- **Build Tool**: Gradle (Wrapper included)

### 1. Configuration Setup
Modify or create the configuration file located at `src/main/resources/application.properties`:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/lets_play
spring.data.mongodb.database=lets_play
server.port=8080

# JWT Custom Settings (Configure inside JwtUtil)
jwt.secret=your_super_secret_high_entropy_key_at_least_256_bits_long
jwt.expiration=86400000
```

### 2. Building and Launching the Server
Navigate to the root directory of your project using your favorite CLI shell and run:

```bash
# Clean project and compile binaries
./gradlew clean build

# Start up the Spring Boot server
./gradlew bootRun
```
The application will boot up natively at: `http://localhost:8080`

---

## 📡 API Documentation & Endpoints

### 🔐 Authentication & User Routing (`/api/users`)

| Method | Endpoint | Access Level | Description | Payload Requirements |
| :--- | :--- | :--- | :--- | :--- |
| **POST** | `/api/users/register` | 🔓 Public | Registers a new account. Roles fallback to `USER` if left blank. Passwords auto-encrypted. | User JSON (`email`, `password`, `role`) |
| **POST** | `/api/users/login` | 🔓 Public | Authenticates user, yields access metadata + active Bearer JWT token. | User login credentials JSON |
| **GET** | `/api/users` | 🛡️ Admin Only | Returns a flat array of all registered accounts inside the DB. | *None (Requires Admin Token)* |

#### 📝 Register Payload Sample (`POST /api/users/register`)
```json
{
  "email": "percy.jackson@camphalfblood.com",
  "password": "PoseidonChild123",
  "role": "USER"
}
```

#### 📥 Login Response Sample (`POST /api/users/login`)
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwZXJjeS5qYWNrc29uQGNhbXBoYWxmYmxvb2QuY29tIiwicm9sZSI6IlVTRVIifQ...",
  "email": "percy.jackson@camphalfblood.com",
  "role": "USER"
}
```

---

### ⚔️ Product Routing (`/api/products`)

| Method | Endpoint | Access Level | Description | Payload Requirements |
| :--- | :--- | :--- | :--- | :--- |
| **GET** | `/api/products` | 🔓 Public | Fetches all products currently active in the database store. | *None* |
| **GET** | `/api/products/{id}` | 🔓 Public | Finds and displays a specific item by its unique MongoDB Identifier. | *None* |
| **POST** | `/api/products` | 🔑 Authenticated | Creates an item and binds it natively to the caller's unique User ID. | Product JSON (Do **not** send an `id`) |
| **PUT** | `/api/products/{id}` | 👤 Owner / Admin | Updates details of a product. Monitored strictly by context protection. | Product Fields JSON update |
| **DELETE**| `/api/products/{id}` | 👤 Owner / Admin | Safely purges a product out of the database collection forever. | *None (Requires Owner/Admin Token)* |

#### 📝 Create Product Payload Sample (`POST /api/products`)
> ⚠️ **Important Architecture Notice**: Do *not* send an explicit `"id"` field inside the body block when firing a `POST` request. MongoDB will calculate a unique string hash automatically. Including a static ID forces an item upsert/overwrite instead of an appending action.

```json
{
  "name": "Anaklusmos",
  "description": "Épée magique et mythique (Riptide). Elle se transforme en stylo-bille et revient toujours dans la poche de son propriétaire.",
  "price": 10.0
}
```

---

## 🔒 Security Operations & Testing Workflow

This section outlines how to perform verification flows within tools such as **Postman** to confirm ownership structures are operating smoothly:

1. **Sign-up User A**: Execute a registration command to create account `userA@test.com`.
2. **Authorize User A Session**: Post credentials to `/api/users/login`. Capture the generated `"token"` string literal payload response text.
3. **Configure Headers**: Copy the token string value. Inside Postman, select the **Authorization** tab, choose **Bearer Token** type, and paste it directly.
4. **Publish Object (POST)**: Broadcast an items payload via `POST /api/products`. The server automatically grabs User A's identity out of the contextual authentication container string and locks it inside the product document tracking field (`userId`).
5. **Simulate Hostile Ingestion (User B)**: Sign in or register as a separate client profile entity (`userB@test.com`). Grab their specific token context and perform an alteration attempt or a purge call target on User A's generated product asset ID:
   - `DELETE /api/products/{target_id}`
   - **Expected Behavior Outcome**: Server drops request, logging explicit **403 Forbidden** security boundaries blocks.
6. **Self-Ownership Access Validation**: Return to User A or Admin authority token context layouts and resend the request. The application registers validated rights authorization context clearings and releases a **204 No Content** success tracking footprint flag.

---

## 🛠️ Built With

- **Spring Boot 3.x** - Backend Java core application architecture framework context.
- **Spring Security** - Security layer controlling access control filtering.
- **Spring Data MongoDB** - Abstracted document mapping structures handling entity lifecycles.
- **JSON Web Tokens (JWT)** - Compact stateless identity transmission payload standard tracking.
