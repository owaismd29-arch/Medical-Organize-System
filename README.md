[![Java CI with Maven in Linux](https://github.com/owaismd29-arch/Medical-Organize-System/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/owaismd29-arch/Medical-Organize-System/actions/workflows/maven.yml)
[![Java CI with Maven, Docker and SonarCloud in Linux](https://github.com/owaismd29-arch/Medical-Organize-System/actions/workflows/sonar.yml/badge.svg?branch=master)](https://github.com/owaismd29-arch/Medical-Organize-System/actions/workflows/sonar.yml)
[![Coverage Status](https://coveralls.io/repos/github/owaismd29-arch/Medical-Organize-System/badge.svg?branch=master&v=2)](https://coveralls.io/github/owaismd29-arch/Medical-Organize-System?branch=master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=coverage)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=bugs)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)

# Medical Organize System

A CRUD-based Medical Patient Management System built with Java, following the MVC architecture pattern, backed by MongoDB.

## Architecture

The project follows the **MVC (Model-View-Controller)** pattern, mirroring a proven blueprint:

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
