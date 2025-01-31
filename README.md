# Patient Service

## Overview
The **Patient Service** manages patient records, including their personal details, observations, and medical history.

## Features
- CRUD operations for patient records
- Secure API endpoints
- Integration with **Observation & Condition Service**
- Role-based access control (Doctors & Staff can modify, Patients can only view)

## Technologies Used
- **Spring Boot**
- **JPA & Hibernate**
- **PostgreSQL/MySQL**
- **Docker & Kubernetes**

## Installation & Setup
```sh
git clone https://github.com/yourusername/patient-service.git
cd patient-service
mvn clean install
docker build -t patient-service .
docker run -p 8082:8082 patient-service
```

## Other Services
- Works with User Service for authentication and authorization.
- Fetches Observations & Conditions from the respective service.
