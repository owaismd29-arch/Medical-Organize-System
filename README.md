[![Java CI with Maven in Linux](https://github.com/owaismd29-arch/Medical-Organize-System/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/owaismd29-arch/Medical-Organize-System/actions/workflows/maven.yml)
[![Java CI with Maven, Docker and SonarCloud in Linux](https://github.com/owaismd29-arch/Medical-Organize-System/actions/workflows/sonar.yml/badge.svg?branch=master)](https://github.com/owaismd29-arch/Medical-Organize-System/actions/workflows/sonar.yml)
## Coveralls
[![Coverage Status](https://coveralls.io/repos/github/owaismd29-arch/Medical-Organize-System/badge.svg?branch=master&v=2)](https://coveralls.io/github/owaismd29-arch/Medical-Organize-System?branch=master)
## Sonar
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=coverage)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=bugs)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=owaismd29-arch_Medical-Organize-System&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=owaismd29-arch_Medical-Organize-System)

## CI/CD Pipeline
- **Linux Build**: Java, JaCoCo coverage, Coveralls report
- **SonarCloud**: Code quality, coverage, bugs, code smells analysis  
- **Mac/Windows**: Cross-platform build verification

# Medical Organize System

A CRUD-based Medical Organize System built in Java with MongoDB integration, following the MVC architecture pattern. The project applies Test-Driven Development (TDD) and includes comprehensive testing with CI/CD workflows.


## Entity: Patient

| Field       | Type   | Description             |
|-------------|--------|-------------------------|
| id          | String | Unique Patient ID       |
| name        | String | Patient Name            |
| age         | int    | Patient Age             |
| diagnosis   | String | Medical Diagnosis       |
| doctorName  | String | Assigned Doctor Name    |


## Features
- **CRUD operations** for managing patient medical records
- **Java Swing graphical user interface** following MVC architecture
- **MongoDB repository** integration for flexible data storage
- **Test-Driven Development (TDD)** for reliability and maintainability
- **Unit, Integration, End-to-End, and BDD testing support**
- **Code coverage analysis** using SonarCloud and Coveralls
- **Continuous Integration** with GitHub Actions across multiple environments
- **Race Condition Tests** for thread-safe CRUD operations

## Acknowledgments
Thanks to **Professor Lorenzo BETTINI**.

