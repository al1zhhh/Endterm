# Endterm Project – Spring Boot REST API  
Design Patterns • Component Principles • SOLID • RESTful Architecture

---

## 1. Project Overview

This project is a Spring Boot RESTful API developed as a continuation of previous assignments (JDBC, Exception Handling, SOLID, Advanced OOP).

The system demonstrates:

- Creational Design Patterns (Singleton, Factory, Builder)
- Component Principles (REP, CCP, CRP)
- SOLID principles
- Layered architecture (Controller → Service → Repository → Database)
- RESTful CRUD endpoints
- Database integration
- Global exception handling
- UML documentation
- Postman API testing

The project represents a professional backend architecture integrating OOP theory with real REST API implementation.

---

## 2. REST API Documentation

### Base URL
http://localhost:8080/api

---

### Create Warrior
POST /api/warriors

Request:
{
  "name": "Thor",
  "level": 5,
  "strength": 80,
  "armor": 40,
  "weaponType": "Hammer"
}

Response:
{
  "id": 1,
  "name": "Thor",
  "level": 5,
  "strength": 80,
  "armor": 40,
  "weaponType": "Hammer"
}

---

### Get All Warriors
GET /api/warriors

---

### Get Warrior by ID
GET /api/warriors/{id}

---

### Update Warrior
PUT /api/warriors/{id}

---

### Delete Warrior
DELETE /api/warriors/{id}

---

### Fight Simulation
POST /api/fight

Request:
{
  "attackerId": 1,
  "defenderId": 2
}

Response:
{
  "winner": "Thor",
  "damageDealt": 35,
  "rounds": 3
}

---

## 3. Design Patterns Implementation

### Singleton Pattern

Used for:
- Database configuration
- Logging service
- Application configuration manager

Purpose:
Ensures a single shared instance across the application.

Example:
DatabaseConfig config = DatabaseConfig.getInstance();

Benefits:
- Controlled resource usage
- Centralized configuration

---

### Factory Pattern

Used to create subclasses of a base entity.

Structure:
GameEntity (abstract)
  ├── Warrior
  ├── Mage
  └── Archer

Example:
GameEntity entity = EntityFactory.createEntity("warrior");

Benefits:
- Decouples object creation
- Easy extension
- Supports Open/Closed Principle

---

### Builder Pattern

Used for constructing complex objects with optional parameters.

Example:
Warrior warrior = Warrior.builder()
    .name("Thor")
    .level(5)
    .strength(80)
    .armor(40)
    .weaponType("Hammer")
    .build();

Benefits:
- Fluent API
- Clean object construction
- Avoids constructor overloading

---

## 4. Component Principles

### REP – Reuse/Release Equivalence Principle
Reusable modules:
- repository
- service
- patterns
- utils

Each package has clear responsibility.

### CCP – Common Closure Principle
Classes that change together are grouped together:
- Controller → REST logic
- Service → Business logic
- Repository → Database logic

### CRP – Common Reuse Principle
Modules do not depend on unnecessary classes.
- Controller does not access database directly.
- Service depends on repository abstraction.

---

## 5. SOLID Principles in the Project

Single Responsibility Principle:
Each class has one responsibility.

Open/Closed Principle:
Factory allows extension without modifying existing code.

Liskov Substitution Principle:
Subclasses (Warrior, Mage) can replace GameEntity.

Interface Segregation Principle:
Separate interfaces like Combatant and Progressable.

Dependency Inversion Principle:
Service depends on repository interface, not concrete implementation.

---

## 6. Database Schema

Example table:

warriors
--------
id INT PRIMARY KEY
name VARCHAR(50)
level INT
experience INT
strength INT
armor INT
weapon_type VARCHAR(50)
created_date TIMESTAMP

Database used:
PostgreSQL / MySQL / SQLite

---

## 7. System Architecture

Layered Architecture:

Client (Postman)
        ↓
Controller
        ↓
Service
        ↓
Repository
        ↓
Database

Project Structure:

controller/
service/
repository/
model/
dto/
exception/
patterns/
utils/

This structure reflects SOLID and component principles.

---

## 8. UML Class Structure (Text Representation)

GameEntity
  ├── id
  ├── name
  ├── level
  ├── experience
  └── createdDate
        ▲
        │
     Warrior
  ├── strength
  ├── armor
  └── weaponType

Interfaces:

Combatant
  ├── attack()
  └── defend()

Progressable
  └── levelUp()

Patterns:

EntityFactory → creates → GameEntity  
WarriorBuilder → builds → Warrior  
DatabaseConfig → Singleton  

---

## 9. Global Exception Handling

Implemented using:
@ControllerAdvice
@ExceptionHandler

Example error response:

{
  "timestamp": "2026-02-12T09:00:00",
  "status": 404,
  "message": "Warrior not found",
  "path": "/api/warriors/999"
}

---

## 10. How to Run the Application

Requirements:
- Java 17+
- Maven
- PostgreSQL (recommended)

application.properties example:

spring.datasource.url=jdbc:postgresql://localhost:5432/endterm_db
spring.datasource.username=postgres
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update

Run:

mvn spring-boot:run

Or run Application.java from IDE.

Test endpoints using Postman.

---

## 11. Reflection

During this project:

- I transformed a layered Java application into a professional REST API.
- I implemented Singleton, Factory, and Builder patterns in a real backend system.
- I structured the project using REP, CCP, and CRP principles.
- I maintained SOLID architecture.
- I integrated database operations with RESTful services.

This project demonstrates full integration of:

Design Patterns + SOLID + Component Principles + RESTful API + Database.
