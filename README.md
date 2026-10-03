# User-Management-System

Spring Boot project using Jakarta Persistence (`jakarta.persistence.*`).

## Project
User-Management-System

## Base Package
com.application.demo

## Java
21

## Module
Course & Batch Foundation

## Relationship
One Course -> Many Batches

## Layers
CourseController -> CourseService -> CourseRepository -> Course
BatchController -> BatchService -> BatchRepository -> Batch

## Main Packages
com.application.demo.controller
com.application.demo.service
com.application.demo.repository
com.application.demo.entity

## Course Fields
id
courseName
description
durationMonths
fee
active

## Batch Fields
id
batchName
startDate
timing
maxStudents
active
course

## Run
mvn spring-boot:run

## Course APIs
GET    /api/courses
GET    /api/courses/{id}
POST   /api/courses
PUT    /api/courses/{id}
DELETE /api/courses/{id}

## Batch APIs
GET    /api/batches
GET    /api/batches/{id}
GET    /api/batches/course/{courseId}
POST   /api/batches/course/{courseId}
PUT    /api/batches/{id}/course/{courseId}
DELETE /api/batches/{id}

## Example Course JSON
{
  "courseName": "Java Full Stack",
  "description": "Java, Spring Boot, SQL and frontend",
  "durationMonths": 6,
  "fee": 25000,
  "active": true
}

## Example Batch JSON
{
  "batchName": "Java Full Stack Morning",
  "startDate": "2026-10-01",
  "timing": "08:00 AM - 10:00 AM",
  "maxStudents": 30,
  "active": true
}

Note: The batch-course relationship is assigned through the courseId in the API URL.
