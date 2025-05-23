Spring Boot Card Application (Hiring Assessment)
This is a simple Spring Boot application designed to simulate card creation and listing functionalities for hiring assessments. It provides two REST API endpoints with enhanced business rules and a simulated authentication process.

Table of Contents
Project Overview

Requirements

Getting Started

Clone the Repository

Build the Project

Run the Application

API Endpoints

1. Create Card (POST /api/cards/create)

2. List All Cards (GET /api/cards)

Authentication

Simulated Tokens

How to Authenticate

Business Rules & Validation

For Candidates

For Junior QA Engineers

For Junior Backend Developers

For Junior Frontend Developers

Support & Environment Note

Project Overview
This application serves as a mock backend for a card management system. It's built with Spring Boot and Gradle, making it easy to set up and run. It now includes more complex business validations and a simulated authentication layer to test candidates' understanding of security and access control.

Requirements
Java Development Kit (JDK) 21 or higher

Gradle (usually bundled with Spring Boot, so not strictly needed to install separately if using ./gradlew)

Getting Started
Clone the Repository
git clone <YOUR_GITHUB_REPO_URL_HERE>
cd spring-boot-card-app

Build the Project
Navigate to the project root directory in your terminal and run:

./gradlew clean build

This command will compile the source code, run tests, and package the application into a JAR file.

Run the Application
After building, you can run the application using the generated JAR file:

java -jar build/libs/spring-boot-app-0.0.1-SNAPSHOT.jar

The application will start on http://localhost:8080 by default.

API Endpoints
1. Create Card (POST /api/cards/create)
   This endpoint validates incoming card creation requests based on specific business rules and user permissions. It does not persist the card but validates the input and authorization.

Method: POST

URL: http://localhost:8080/api/cards/create

Content-Type: application/json

Headers: Authorization: Bearer <token> (See Authentication section)

Request Body Parameters:

| Parameter           | Type      | Required | Description