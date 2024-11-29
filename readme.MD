# Digital Money House 🏦

## Descripción del Proyecto

Digital Money House es una plataforma financiera digital desarrollada como parte del proyecto integrador. El sistema implementa una arquitectura de microservicios robusta utilizando Spring Boot y tecnologías modernas de la nube, permitiendo a los usuarios gestionar sus finanzas de manera segura y eficiente.

## Repositorio

```bash
https://gitlab.com/atuhome/digital-money-house
```

## Arquitectura del Sistema 🏗

El proyecto implementa una arquitectura de microservicios que consta de los siguientes componentes:

### Servicios de Infraestructura

- **Config Server**: Centraliza la gestión de configuraciones para todos los microservicios
- **Eureka Server**: Proporciona registro y descubrimiento de servicios
- **API Gateway**: Actúa como punto de entrada único y maneja el enrutamiento de solicitudes

### Servicios de Negocio

- **Auth Service**: Gestiona la autenticación y autorización utilizando Keycloak
- **User Service**: Administra la información y operaciones de usuarios
- **Account Service**: Maneja las cuentas y sus operaciones asociadas
- **Card Service**: Gestiona las tarjetas de débito y crédito
- **Transaction Service**: Procesa todas las transacciones financieras

## Tecnologías Utilizadas 🛠

- **Java 17**: Lenguaje de programación principal
- **Spring Boot 3.x**: Framework de desarrollo
- **Spring Cloud**: Para arquitectura de microservicios
- **Keycloak**: Servidor de autenticación y autorización
- **Docker**: Contenedorización de servicios
- **PostgreSQL/MySQL**: Bases de datos
- **Maven**: Gestión de dependencias y construcción
- **Swagger/OpenAPI**: Documentación de APIs

## Configuración del Entorno de Desarrollo

### Prerrequisitos

- Java JDK 17
- Maven 3.6.3 o superior
- Docker y Docker Compose
- Git
- IDE (recomendado IntelliJ IDEA)

### Clonar el Repositorio

```bash
git clone https://gitlab.com/atuhome/digital-money-house.git
cd digital-money-house
```

### Configuración de Base de Datos

El proyecto utiliza múltiples bases de datos para diferentes servicios:

```yaml
# Para User Service
mysql_user_service:
  container_name: mysql_user_service
  image: mysql:latest
  ports:
    - "3307:3306"

# Para Account Service
mysql_account_service:
  container_name: mysql_account_service
  ports:
    - "3308:3306"

# Para Keycloak
postgres_keycloak:
  container_name: postgres_keycloak
  ports:
    - "5433:5432"
```

### Iniciar los Servicios

1. Iniciar servicios de infraestructura:
```bash
docker-compose up -d
```

2. Verificar que Keycloak esté funcionando:
```bash
http://localhost:9092
```

3. Iniciar los servicios en el siguiente orden:
   - Eureka Server (Puerto 8761)
   - Config Server (Puerto 8888)
   - API Gateway (Puerto 8080)
   - Servicios de negocio

## Estructura de Puertos

- Eureka Server: 8761
- Config Server: 8888
- API Gateway: 8080
- Auth Service: 8083
- User Service: 8081
- Account Service: 8082
- Card Service: 8085
- Transaction Service: 8097

## Documentación de APIs 📚

Cada servicio cuenta con su propia documentación Swagger accesible en:

```
http://localhost:{puerto}/swagger-ui.html
```

Por ejemplo:
- Gateway: http://localhost:8080/swagger-ui.html
- User Service: http://localhost:8081/swagger-ui.html
- Account Service: http://localhost:8082/swagger-ui.html

## Seguridad 🔒

El proyecto utiliza OAuth2 con Keycloak para la autenticación y autorización:

- **Realm**: dmh
- **Client ID**: gateway-client
- **Grant Types**: Authorization Code, Client Credentials
- **Roles**: USER, ADMIN

## Monitoreo y Actuator 📊

Todos los servicios exponen endpoints de Spring Boot Actuator para monitoreo:

```
http://localhost:{puerto}/actuator
```

Endpoints disponibles:
- /health: Estado del servicio
- /metrics: Métricas del servicio
- /info: Información del servicio

## Manejo de Errores y Circuit Breaker

El proyecto implementa el patrón Circuit Breaker utilizando Resilience4j para manejar fallos en las comunicaciones entre servicios:

```java
resilience4j:
  circuitbreaker:
    instances:
      userService:
        slidingWindowSize: 10
        failureRateThreshold: 50
        waitDurationInOpenState: 10000ms
```

## Contribución al Proyecto 🤝

1. Crear un fork del repositorio
2. Crear una rama para tu feature
```bash
git checkout -b feature/nueva-funcionalidad
```
3. Realizar cambios y commits
```bash
git commit -m "Descripción detallada de los cambios"
```
4. Push a la rama
```bash
git push origin feature/nueva-funcionalidad
```
5. Crear un Merge Request

## Convenciones de Código

- Usar camelCase para nombres de variables y métodos
- Usar PascalCase para nombres de clases
- Documentar métodos públicos con Javadoc
- Seguir principios SOLID
- Escribir pruebas unitarias para nueva funcionalidad

## Contacto y Soporte 📧

Para consultas o reportes de problemas:
- Crear un Issue en GitLab
- Contactar al equipo: support@digitalmoney.house

## Estado del Proyecto 📈

El proyecto se encuentra en desarrollo activo, con actualizaciones y mejoras continuas. Se recomienda mantener actualizada la rama principal y revisar regularmente la documentación en GitLab para conocer los últimos cambios.