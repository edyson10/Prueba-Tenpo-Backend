# Prueba-Tenpo-Backend MVC

Este proyecto implementa un servicio backend REST desarrollado en Java con Spring Boot, como parte de una prueba técnica, cuyo objetivo principal es:

* Realizar un cálculo matemático a partir de dos números.
* Aplicar un porcentaje obtenido desde un servicio externo.
* Registrar historial de llamadas a los endpoints.
* Implementar rate limiting, retry, manejo global de errores y observabilidad básica.
* Exponer documentación mediante Swagger / OpenAPI.
* Ejecutarse de forma local o mediante Docker.

El proyecto está construido usando arquitectura MVC, con separación clara de responsabilidades y buenas prácticas de desarrollo backend.

# 🚀 Tecnologías utilizadas

1. Java 21
2. Spring Boot 3.x
3. Spring MVC
4. Spring Data JPA
5. PostgreSQL
6. Spring Retry
7. Swagger / OpenAPI (springdoc)
8. Docker & Docker Compose
9. Maven

# 📦 Arquitectura

### El proyecto sigue una arquitectura en capas:
```bash
Controller → Service → Repository → Database
                ↓
             Client (servicios externos)
```
### Separación clara de responsabilidades:

* Controller: expone endpoints REST
* Service: lógica de negocio
* Repository: acceso a base de datos
* Client: consumo de servicios externos
* Filter: logging y rate limit
* DTO / Mapper: transferencia de datos
* Exception: manejo global de errores

# 🧬 Estructura del proyecto

```bash
tenpo-backend-challenge/
├─ pom.xml
├─ Dockerfile
├─ docker-compose.yml
├─ README.md
└─ src/
   ├─ main/
   │  ├─ java/com/tenpo/challenge/
   │  │  ├─ TenpoBackendChallengeApplication.java
   │  │  ├─ config/
   │  │  │  ├─ AsyncConfig.java
   │  │  │  ├─ OpenApiConfig.java
   │  │  │  ├─ RetryConfig.java
   │  │  │  └─ RateLimitFilter.java
   │  │  ├─ controller/
   │  │  │  ├─ CalculationController.java
   │  │  │  ├─ HistoryController.java
   │  │  │  └─ MockPercentageController.java
   │  │  ├─ dto/
   │  │  │  ├─ CalculationRequestDto.java
   │  │  │  ├─ CalculationResponseDto.java
   │  │  │  ├─ CallHistoryDto.java
   │  │  │  └─ ErrorResponseDto.java
   │  │  ├─ entity/
   │  │  │  └─ CallHistoryEntity.java
   │  │  ├─ exception/
   │  │  │  ├─ ExternalServiceUnavailableException.java
   │  │  │  ├─ RateLimitExceededException.java
   │  │  │  └─ RestExceptionHandler.java
   │  │  ├─ filter/
   │  │  │  └─ CallHistoryLoggingFilter.java
   │  │  ├─ mapper/
   │  │  │  └─ CallHistoryMapper.java
   │  │  ├─ repository/
   │  │  │  └─ CallHistoryRepository.java
   │  │  └─ service/
   │  │     ├─ CalculationService.java
   │  │     ├─ CallHistoryService.java
   │  │     ├─ PercentageClient.java
   │  │     └─ HttpPercentageClient.java
   │  └─ resources/
   │     └─ application.yml
   └─ test/java/com/tenpo/challenge/
      └─ service/CalculationServiceTest.java
```

# 🧪 Endpoints disponibles

## 1️⃣ Calcular resultado

### Aplica la suma de dos números más un porcentaje obtenido desde un servicio externo.

### **Url**

```bash
POST /api/v1/calculate
```
### **Header**

```bash
Content-Type: application/json
```

### **Body de ejemplo**

```bash
{
  "num1": 10,
  "num2": 5
}
```

### **Respuesta**

```bash
{
  "num1": 10.0,
  "num2": 5.0,
  "sum": 15.0,
  "percentageApplied": 10.0,
  "result": 16.5
}
```

## 2️⃣ Historial de llamadas

### Retorna el historial paginado de todas las llamadas realizadas al sistema.

### **Url**

```bash
GET /api/v1/history?page=0&size=10&sort=timestamp,desc
```

### **Respuesta**

```bash
{
  "content": [
    {
      "id": 1,
      "timestamp": "2026-01-29T19:18:09Z",
      "method": "POST",
      "endpoint": "/api/v1/calculate",
      "httpStatus": 200
    }
  ],
  "totalElements": 1,
  "totalPages": 1
}
```

## 3️⃣ Mock servicio externo de porcentaje

### Simula el servicio externo requerido por la prueba técnica.

### **Url**

```bash
GET /mock/percentage
```

### **Respuesta**

```bash
{
  "percentage": 10.0
}
```

## ⏱ Rate Limit

### El sistema implementa un **rate limit de 3 requests por minuto** para los endpoints públicos.

### Swagger, recursos estáticos y endpoints internos están excluidos del rate limit.

### Cuando se supera el límite:

```bash
{
  "timestamp": "2026-01-30T09:58:43",
  "message": "Rate limit exceeded: max 3 requests per minute",
  "path": "/api/v1/calculate"
}
```

## 🔁 Retry ante fallos externos

### El consumo del servicio de porcentaje implementa retry automático:

* Hasta 3 intentos
* Backoff exponencial
* Manejo de error controlado si el servicio externo no responde

## 📘 Swagger / OpenAPI

### La documentación de la API está disponible en:

* Swagger UI

```bash
http://localhost:8080/swagger-ui/index.html
```

* OpenAPI JSON

```bash
http://localhost:8080/v3/api-docs
```

## 🐳 Ejecución con Docker

### Levantar aplicación + PostgreSQL

```bash
docker compose up --build
```

## 🐳 Docker Hub

La imagen del proyecto está publicada en Docker Hub:

https://hub.docker.com/r/edysonleal03/prueba-tenpo-backend

### Ejecutar la aplicación

```bash
docker pull edysonleal03/prueba-tenpo-backend:latest
docker run -p 8080:8080 edysonleal03/prueba-tenpo-backend:latest
```

### Servicios:

API: http://localhost:8080

PostgreSQL: localhost:5432

## ⚙️ Manejo de errores

### El proyecto implementa un handler global de excepciones, retornando respuestas uniformes:

```bash
{
  "timestamp": "2026-01-30T09:58:43",
  "message": "Descripción del error",
  "path": "/api/v1/calculate"
}
```

## 🧪 Pruebas

### Incluye pruebas unitarias sobre la capa de servicio:

```bash
mvn test
```

# 👨‍💻 Autor

Edyson Fabian Leal  
Backend Developer – Java / Spring Boot  
📧 edysonleal3@gmail.com  
🔗 https://www.linkedin.com/in/edyson-leal/
