# Spring Boot Card Application (Hiring Assessment)

This is a simple Spring Boot application designed to simulate card creation and listing functionalities for hiring assessments. It provides two REST API endpoints with enhanced business rules and a simulated authentication process.

## Table of Contents

1. [Project Overview](#project-overview)

2. [Requirements](#requirements)

3. [Getting Started](#getting-started)

   * [Clone the Repository](#clone-the-repository)

   * [Build the Project](#build-the-project)

   * [Run the Application](#run-the-application)

4. [API Endpoints](#api-endpoints)

   * [1. Authenticate and Get Token (POST /api/auth/login)](#1-authenticate-and-get-token-post-apiauthlogin)

   * [2. Create Card (POST /api/cards/create)](#2-create-card-post-apicardscreate)

   * [3. List All Cards (GET /api/cards)](#3-list-all-cards-get-apicards)

5. [Authentication & Authorization](#authentication--authorization)

   * [Simulated Tokens](#simulated-tokens)

   * [Simulated Credentials for Token Generation](#simulated-credentials-for-token-generation)

   * [How to Authenticate with Bearer Token](#how-to-authenticate-with-bearer-token)

6. [Business Rules & Validation](#business-rules--validation)

7. [For Candidates](#for-candidates)

   * [For Junior QA Engineers](#for-junior-qa-engineers)

   * [For Junior Backend Developers](#for-junior-backend-developers)

   * [For Junior Frontend Developers](#for-junior-frontend-developers)

8. [Support & Environment Note](#support--environment-note)

## 1. Project Overview

This application serves as a mock backend for a card management system. It's built with Spring Boot and Gradle, making it easy to set up and run locally. It includes realistic business validations and a simulated authentication layer to test candidates' understanding of API interaction, data handling, and access control.

## 2. Requirements

* Java Development Kit (JDK) **21** or higher

* Gradle (usually bundled with Spring Boot, so not strictly needed to install separately if using `./gradlew`)

## 3. Getting Started

### Clone the Repository

git clone <YOUR_GITHUB_REPO_URL_HERE>
cd spring-boot-card-app


*(Replace `<YOUR_GITHUB_REPO_URL_HERE>` with the actual HTTPS or SSH URL of your GitHub repository).*

### Build the Project

Navigate to the project root directory in your terminal and run:

./gradlew clean build


This command will compile the source code, run tests, and package the application into a JAR file.

### Run the Application

After building, you can run the application using the generated JAR file:

java -jar build/libs/spring-boot-card-app-0.0.1-SNAPSHOT.jar


The application will start on `http://localhost:8080` by default.

## 4. API Endpoints

### 1. Authenticate and Get Token (POST /api/auth/login)

This endpoint allows users to log in with hardcoded credentials and receive a time-based Bearer token. This token is valid for 1 hour.

* **Method:** `POST`

* **URL:** `http://localhost:8080/api/auth/login`

* **Content-Type:** `application/json`

* **Headers:** `None required`

**Request Body Example (for Prepaid User):**

{
"userId": "prepaiduser",
"password": "prepaidpass"
}


**Request Body Example (for Limited Use User):**

{
"userId": "limiteduser",
"password": "limitedpass"
}


**Successful Response Example (200 OK):**

{
"token": "example_generated_token_hash_here_will_change_hourly"
}


**Invalid Login Response Example (401 Unauthorized):**

{
"timestamp": "2025-05-23T14:30:00.000000",
"status": 401,
"error": "Unauthorized",
"message": "Invalid userId or password.",
"path": "/api/auth/login"
}


### 2. Create Card (POST /api/cards/create)

This endpoint validates incoming card creation requests based on specific business rules and **user permissions**. It does not persist the card but validates the input and authorization.

* **Method:** `POST`

* **URL:** `http://localhost:8080/api/cards/create`

* **Content-Type:** `application/json`

* **Headers:** `Authorization: Bearer <token>` (See [Authentication & Authorization](#authentication--authorization) section)

**Valid Request Body Example (for PREPAID Card):**
*(Requires a token from a **Prepaid** user or the static `auth.token.static.prepaid-user` token)*

{
"cardTitle": "My General Prepaid Card",
"cardDescription": "A versatile prepaid card for everyday expenses, comes with full protection.",
"activationDate": "2025-06-01",
"cardProduct": "PREPAID",
"loadAmount": 150.00,
"protectionRequired": true
}


**Valid Request Body Example (for LIMITED_USE Card):**
*(Requires a token from a **Limited Use** user or the static `auth.token.static.limited-user` token)*

{
"cardTitle": "Event Access Pass",
"cardDescription": "A special limited-use card for event entry and a few transactions within the event.",
"activationDate": "2025-05-26",
"cardProduct": "LIMITED_USE",
"loadAmount": 200.00,
"protectionRequired": false,
"restrictions": {
"expiryDate": "2025-06-18",
"maxSwipes": 5,
"perTransactionLimit": 50.00
}
}


**Error Response Example (400 Bad Request - Business Rule Violation):**

{
"timestamp": "2025-05-23T14:30:00.000000",
"status": 400,
"error": "Bad Request",
"message": "For Prepaid cards, 'protectionRequired' must be true.",
"path": "/api/cards/create"
}


**Error Response Example (403 Forbidden - Authorization Violation):**
*(e.g., Prepaid user trying to create a LIMITED_USE card)*

{
"timestamp": "2025-05-23T14:30:00.000000",
"status": 403,
"error": "Forbidden",
"message": "Forbidden: Prepaid users cannot create Limited Use cards.",
"path": "/api/cards/create"
}


### 3. List All Cards (GET /api/cards)

This endpoint returns a hardcoded list of card information, filtered by card product type. **User permissions** apply here.

* **Method:** `GET`

* **URL:** `http://localhost:8080/api/cards`

* **Query Parameters:**

   * `cardProduct`: **Required**. Must be `PREPAID` or `LIMITED_USE`.

* **Headers:** `Authorization: Bearer <token>` (See [Authentication & Authorization](#authentication--authorization) section)

**Example Request:**
`GET http://localhost:8080/api/cards?cardProduct=PREPAID`
*(Requires a token from a **Prepaid** user or the static `auth.token.static.prepaid-user` token)*

**Successful Response Example (200 OK):**

[
{
"cardNumber": "ABCD EFGJ KLMN OPQR",
"cardTitle": "Travel Buddy Card",
"cardDescription": "A versatile card for all your travel needs.",
"activationDate": "2024-01-15",
"cardProduct": "PREPAID",
"loadAmount": 100.00,
"protectionRequired": true,
"restrictions": null,
"cardCreator": "Alice Smith",
"creationDate": "2023-12-10T10:00:00",
"currentLoad": 85.50,
"currentNumberOfSwipes": null
},
{
"cardNumber": "STUV WXYZ 1234 5678",
"cardTitle": "Gift Voucher Card",
"cardDescription": "A simple prepaid gift card.",
"activationDate": "2025-05-28",
"cardProduct": "PREPAID",
"loadAmount": 20.00,
"protectionRequired": true,
"restrictions": null,
"cardCreator": "Frank Green",
"creationDate": "2025-05-21T14:00:00",
"currentLoad": 20.00,
"currentNumberOfSwipes": null
}
]


**Error Response Example (400 Bad Request - Invalid Query Param):**

{
"timestamp": "2025-05-23T14:30:00.000000",
"status": 400,
"error": "Bad Request",
"message": "Invalid cardProduct parameter. Must be 'PREPAID' or 'LIMITED_USE'.",
"path": "/api/cards"
}


**Error Response Example (404 Not Found - Authorization Violation):**
*(e.g., Prepaid user trying to list LIMITED_USE cards)*

{
"timestamp": "2025-05-23T14:30:00.000000",
"status": 404,
"error": "Not Found",
"message": "Not Found: Prepaid users cannot view Limited Use cards.",
"path": "/api/cards"
}
## 5. Authentication & Authorization

All endpoints under `/api/**` (except `/api/auth/login`) require a Bearer token in the `Authorization` header. This application simulates an Auth0-like authentication process.

### Simulated Tokens

There are two types of tokens:

1. **Static Hardcoded Tokens (Always Valid):**
   These tokens are always valid, regardless of time. They are useful for quick testing.

   * **For Prepaid-only user:**
     `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJwcmVwYWlkX3VzZXIiLCJyb2xlIjoiUFJFUEFJRFNfT05MWSIsImlhdCI6MTUxNjIzOTAyMn0.S0m3H4shT0k3nF0rPrepaidOnly`

   * **For Limited-Use-only user:**
     `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJsaW1pdGVkX3VzZXIiLCJyb2xlIjoiTElNSVRFRF9VU0VfT05MWSIsImlhdCI6MTUxNjIzOTAyMn0.L1m1t3dUs3Only`

2. **Time-Based Generated Tokens (Valid for 1 Hour):**
   These tokens are generated via the `/api/auth/login` endpoint and are valid for the hour in which they were generated, plus the subsequent hour.

### Simulated Credentials for Token Generation

Use these credentials with the `/api/auth/login` endpoint to obtain time-based tokens:

* **For Prepaid-only user:**

   * **userId:** `prepaiduser`

   * **password:** `prepaidpass`

* **For Limited-Use-only user:**

   * **userId:** `limiteduser`

   * **password:** `limitedpass`

### How to Authenticate with Bearer Token (e.g., in Postman)

1. Open your API client (e.g., Postman).

2. Go to the **"Headers"** tab for your request.

3. Add a new header with the key `Authorization`.

4. For the value, enter `Bearer <YOUR_TOKEN_HERE>`, replacing `<YOUR_TOKEN_HERE>` with either a static token or a newly generated token.

## 6. Business Rules & Validation

The `/api/cards/create` endpoint enforces several business rules:

* **General Rules:**

   * `cardTitle`: Cannot be empty, must be between 3 and 100 characters.

   * `cardDescription`: Cannot be empty, must be between 10 and 500 characters.

   * `activationDate`: Cannot be null, **must be in the future**.

   * `loadAmount`: Cannot be null, must be a positive number.

* **Rules for `PREPAID` Cards:**

   * `protectionRequired` **must be `true`**.

   * `restrictions` object **must NOT be present**.

   * `loadAmount` **must be a multiple of 5**.

* **Rules for `LIMITED_USE` Cards:**

   * `restrictions` object **must be provided**.

   * `restrictions.expiryDate`: Must be present, **must be in the future**, and **at maximum 32 days from the current date**.

   * `restrictions.maxSwipes`: Must be present and a positive integer.

   * `restrictions.perTransactionLimit`: Must be present and a positive number.

* **Authorization Rules (Applies to `create` and `list` endpoints):**

   * **Prepaid-only users:**

      * Allowed to create `PREPAID` cards.

      * **Forbidden (403)** from creating `LIMITED_USE` cards.

      * Allowed to list `PREPAID` cards.

      * **Not Found (404)** when trying to list `LIMITED_USE` cards.

   * **Limited-Use-only users:**

      * Allowed to create `LIMITED_USE` cards.

      * **Forbidden (403)** from creating `PREPAID` cards.

      * Allowed to list `LIMITED_USE` cards.

      * **Not Found (404)** when trying to list `PREPAID` cards.

## 7. For Candidates

This project is designed to assess various skills depending on the role.

### For Junior QA Engineers

Your task is to thoroughly **assess and build comprehensive test cases for the "Card Creation" flow** (using `POST /api/cards/create`). Focus on understanding all the business rules (both general and product-type specific, including date validations), testing authorization behaviors, and identifying edge cases. You may also add ideas for improvements to the feature or API.

**Deliverables:** A structured document (e.g., spreadsheet, Markdown file) detailing your test cases, including input, expected output, and actual results (if tested). Also, a brief summary of improvement ideas.

### For Junior Backend Developers

Your task is to build a **separate application** (e.g., another Spring Boot app, a simple Java console app) that interacts with this API. You will need to:

1. Authenticate using the `/api/auth/login` endpoint to obtain a token.

2. Consume the `GET /api/cards` endpoint (with appropriate `cardProduct` query parameter and token) to retrieve card data.

3. **Design a database schema** (e.g., using an H2 in-memory database) and **save the retrieved cards** into your database.

4. Implement basic **unit tests** for your data saving logic.

**Deliverables:** Your source code, database schema (if applicable), unit tests, and a README explaining how to run and test your application.

### For Junior Frontend Developers

Your task is to build a **simple user interface (UI)** (e.g., a basic HTML/JavaScript page, or a React/Vue/Angular app) that interacts with this API. You will need to:

1. Authenticate using the `/api/auth/login` endpoint to obtain a token.

2. Consume the `GET /api/cards` endpoint (with appropriate `cardProduct` query parameter and token) to retrieve card data.

3. **Display the card information** in a user-friendly way.

4. Allow users to **order the displayed cards** in some way (e.g., by `cardTitle` alphabetically, `activationDate`, `currentLoad`).

5. Implement basic **unit tests** for your UI components or ordering logic.

**Deliverables:** Your source code, unit tests, and a README explaining how to run and test your application.

## 8. Support & Environment Note

* **Questions Welcome:** We encourage you to reach out to us with any questions or clarifications you may need during the project. Please understand that due to our heavy work schedules, replies might be a bit delayed.

* **Presentation Session:** Upon completion, we will schedule a **30-45 minute session** for you to present your work. You are free to present your code, documents, or demonstrate your feature in any way you feel best showcases your efforts. **Documentation is heavily incentivized**, and **unit tests are important** (especially for developers) for our evaluation.

* **Development Environment:** Please be aware that this application is a simulated environment for assessment.