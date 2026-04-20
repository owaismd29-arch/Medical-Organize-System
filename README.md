[![Java CI with Maven in Linux](../../actions/workflows/maven.yml/badge.svg)](../../actions/workflows/maven.yml)
[![Java CI with Maven, Docker and SonarCloud in Linux](../../actions/workflows/sonar.yml/badge.svg)](../../actions/workflows/sonar.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=coverage)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=bugs)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=YOUR_SONAR_PROJECT_KEY&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=YOUR_SONAR_PROJECT_KEY)

# Medical Organize System

A CRUD-based Medical Patient Management System built with Java, following the MVC architecture pattern, backed by MongoDB.

## Architecture

The project follows the **MVC (Model-View-Controller)**:

```
src/
├── main/java/com/example/medicalorganizer/
│   ├── model/
│   │   └── Patient.java              # Entity
│   ├── repository/
│   │   ├── PatientRepository.java    # Repository Interface
│   │   └── mongo/
│   │       └── PatientMongoRepository.java  # MongoDB Implementation
│   ├── controller/
│   │   └── PatientController.java    # Business Logic (CRUD)
│   ├── view/
│   │   ├── MedicalView.java          # View Interface
│   │   └── swing/
│   │       └── MedicalSwingView.java # Swing GUI
│   └── app/swing/
│       └── MedicalSwingApp.java      # Entry Point
└── test/
    ├── unit/        (PatientControllerTest, PatientMongoRepositoryTest)
    ├── it/          (Integration Tests)
    ├── e2e/         (End-to-End Tests)
    └── bdd/         (BDD Cucumber Tests)
```

## Entity: Patient

| Field       | Type   | Description             |
|-------------|--------|-------------------------|
| id          | String | Unique Patient ID       |
| name        | String | Patient Name            |
| age         | int    | Patient Age             |
| diagnosis   | String | Medical Diagnosis       |
| doctorName  | String | Assigned Doctor Name    |

## CRUD Operations

- **Create** — Add a new patient record
- **Read** — View all patients / find by ID
- **Update** — Modify existing patient record
- **Delete** — Remove a patient record

## Features
- **CRUD Operations** for Patient records (Create, Read, Update, Delete)
- **MongoDB repository integration**
- **Test-Driven Development (TDD)** for code reliability and quality
- **100% Code Coverage** using **SonarCloud** and **Coveralls**
- **Continuous Integration** with **GitHub Actions** — tested on Linux, Mac, and Windows
- **Mutation Testing** via PIT with 80%+ mutation threshold
- **BDD Tests** with Cucumber feature files
- **Race Condition Tests** for thread-safe CRUD operations

- Java 8+
- Maven
- MongoDB (or Docker)

## Run with Docker

```bash
docker-compose up -d
mvn clean package
java -jar target/medicalorganizer-0.0.1-SNAPSHOT.jar
```

## Run Tests

```bash
# Unit Tests
mvn test

# Integration Tests
mvn verify

# With Docker (for IT tests)
mvn verify -Pdocker
```
