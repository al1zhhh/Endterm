Endterm Project – Spring Boot REST API

Design Patterns, Component Principles & SOLID Architecture

A. Project Overview

This project is a Spring Boot RESTful API developed as a continuation of previous assignments (JDBC + SOLID + Advanced OOP).

The system demonstrates:

Implementation of Design Patterns (Singleton, Factory, Builder)

Application of Component Principles (REP, CCP, CRP)

Usage of SOLID principles

Layered architecture (Controller – Service – Repository – Database)

CRUD operations via REST API

Database integration (PostgreSQL / MySQL / SQLite)

Global exception handling

UML documentation

The system represents a structured backend application that supports entity management and simulated game/fighting logic.

B. REST API Documentation
Base URL
http://localhost:8080/api
1. Create Entity
POST /api/warriors

Request Body (JSON)

{
  "name": "Thor",
  "level": 5,
  "strength": 80,
  "armor": 40,
  "weaponType": "Hammer"
}

Response

{
  "id": 1,
  "name": "Thor",
  "level": 5,
  "strength": 80,
  "armor": 40,
  "weaponType": "Hammer"
}
2. Get All Warriors
GET /api/warriors

Response

[
  {
    "id": 1,
    "name": "Thor",
    "level": 5,
    "strength": 80,
    "armor": 40,
    "weaponType": "Hammer"
  }
]
3. Get Warrior by ID
GET /api/warriors/{id}
4. Update Warrior
PUT /api/warriors/{id}
5. Delete Warrior
DELETE /api/warriors/{id}
6. Simulate Fight
POST /api/fight
{
  "attackerId": 1,
  "defenderId": 2
}

Response:

{
  "winner": "Thor",
  "damageDealt": 35
}
C. Design Patterns Section
1. Singleton Pattern

Used for:

DatabaseConfig

LoggingService

Purpose:
Ensures a single instance across the application.

Example usage:

Shared database connection manager

Centralized logging

Why used:

Prevents multiple configurations

Improves resource control

2. Factory Pattern

Used to create subclasses of base entity:

Example:

GameEntity

Warrior

Mage

Archer

Factory returns base type:

GameEntity entity = EntityFactory.createEntity("warrior");

Why used:

Decouples object creation from business logic

Makes system extensible

3. Builder Pattern

Used for creating complex objects:

Example:

Warrior warrior = Warrior.builder()
        .name("Thor")
        .level(5)
        .strength(80)
        .armor(40)
        .weaponType("Hammer")
        .build();

Why used:

Supports optional parameters

Improves readability

Avoids constructor overloads

D. Component Principles
REP – Reuse/Release Equivalence Principle

Reusable modules:

repository

service

patterns

utils

Each module has clear responsibility.

CCP – Common Closure Principle

Classes that change together are grouped together:

All database logic → repository

All business logic → service

All REST endpoints → controller

CRP – Common Reuse Principle

Modules do not depend on unnecessary classes.

For example:

Controller does not access repository directly.

Service does not depend on controller.

E. SOLID & OOP Summary
S – Single Responsibility

Each class has one responsibility:

Controller handles HTTP

Service handles business logic

Repository handles database

O – Open/Closed

Factory allows extension without modifying base class.

L – Liskov Substitution

All subclasses (Warrior, Mage) can replace GameEntity.

I – Interface Segregation

Combatant, Progressable interfaces are separated.

D – Dependency Inversion

Service depends on repository interface, not concrete implementation.

F. Database Schema

Example table:

Warrior
-------
id (PK)
name
level
strength
armor
weapon_type
created_date

Relationships handled via foreign keys if needed.

G. System Architecture Diagram

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

Includes:

DTO layer

Exception handler

Patterns package

UML Diagram provided in /docs/uml.png

H. How to Run the Application

Clone repository

Configure application.properties

Example:

spring.datasource.url=jdbc:postgresql://localhost:5432/endterm_db
spring.datasource.username=postgres
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update

Run:

mvn spring-boot:run

or run Application.java.

Test endpoints using Postman.

I. Global Exception Handling

Implemented using:

@ControllerAdvice

@ExceptionHandler

Example response:

{
  "timestamp": "2026-02-10T12:00:00",
  "message": "Warrior not found",
  "status": 404
}
J. Reflection

During this project:

I learned how to transform a layered Java application into a REST API.

I applied real design patterns in a practical system.

I structured the project using component principles.

I maintained SOLID architecture in Spring Boot.

I integrated database operations with RESTful services.

I improved understanding of professional backend development.

This project demonstrates a complete backend architecture combining:
Design Patterns + SOLID + Component Principles + REST API.


📌 UML Class Diagram (Endterm Project)
1️⃣ Main classes
+--------------------+
|     GameEntity     |
+--------------------+
| - id: int          |
| - name: String     |
| - level: int       |
| - experience: int  |
| - createdDate      |
+--------------------+
| + levelUp()        |
+--------------------+
           ▲
           |
+--------------------+
|      Warrior       |
+--------------------+
| - strength: int    |
| - armor: int       |
| - weaponType: String|
+--------------------+
| + attack()         |
| + defend()         |
+--------------------+
2️⃣ Interfaces (ISP – Interface Segregation)
+----------------+
|  Combatant     |
+----------------+
| + attack()     |
| + defend()     |
+----------------+


+----------------+
| Progressable   |
+----------------+
| + levelUp()    |
+----------------+

Warrior implements:

Warrior → Combatant
Warrior → Progressable
3️⃣ Factory Pattern
+-----------------------+
|     EntityFactory     |
+-----------------------+
| + createEntity(type)  |
+-----------------------+

Returns:

GameEntity
   ▲
   |
Warrior / Mage / Archer

📌 Связь:
Factory → создает → GameEntity

4️⃣ Builder Pattern
+-----------------------+
|      WarriorBuilder   |
+-----------------------+
| - name                |
| - level               |
| - strength            |
| - armor               |
| - weaponType          |
+-----------------------+
| + name()              |
| + level()             |
| + strength()          |
| + armor()             |
| + weaponType()        |
| + build()             |
+-----------------------+



WarriorBuilder → builds → Warrior
5️⃣ Singleton Pattern
+-----------------------+
|   DatabaseConfig      |
+-----------------------+
| - instance            |
+-----------------------+
| + getInstance()       |
| + getConnection()     |
+-----------------------+



6️⃣ Layered Architecture (Component Principles)
+----------------+
|   Controller   |
+----------------+
        ↓
+----------------+
|    Service     |
+----------------+
        ↓
+----------------+
|   Repository   |
+----------------+
        ↓
+----------------+
|    Database    |
+----------------+
7️⃣ REST Structure UML
WarriorController
    ↓
WarriorService
    ↓
WarriorRepository
    ↓
Database

 Package Structure (REP / CCP / CRP)
controller/
service/
repository/
model/
dto/
exception/
patterns/
utils/
