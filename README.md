# Desafío Profesional: Digital Money House

## Introducción

El Desafío Profesional en Digital House consiste en la creación de una Rest API para una billetera virtual llamada Digital Money House. Este proyecto es parte integral de la formación y evaluación en la especialización de backend.

## Índice

1. [Descripción del Proyecto](#descripción-del-proyecto)
2. [Funcionalidades Esperadas](#funcionalidades-esperadas)
3. [Requisitos del Proyecto por Área](#requisitos-del-proyecto-por-área)
4. [Resultados Esperados](#resultados-esperados)
5. [Sprints](#sprints)
  - [Sprint 1](#sprint-1)
  - [Sprint 2](#sprint-2)
  - [Sprint 3](#sprint-3)
  - [Sprint 4](#sprint-4)
6. [Diseño de la Infraestructura](#diseño-de-la-infraestructura)

## Descripción del Proyecto

El proyecto consiste en desarrollar una Rest API para una billetera virtual llamada Digital Money House. Los usuarios podrán registrarse, iniciar sesión, gestionar sus medios de pago, consultar su saldo, realizar transferencias de dinero y revisar la actividad de su cuenta.

## Funcionalidades Esperadas

- Servicio de registro de usuario.
- Servicios de inicio y cierre de sesión.
- Consulta de saldo e información de la cuenta.
- Consulta y actualización de datos del usuario.
- Gestión de medios de pago.
- Ingreso de dinero desde tarjetas a la billetera.
- Actividad del usuario (movimientos de cuenta).
- Transferencia de dinero entre usuarios y cuentas externas.

## Requisitos del Proyecto por Área

### Backend Developer

- Creación de microservicios con Java y Spring Boot.
- Construcción de API Rest utilizando Spring Boot y JSON.
- Documentación de APIs con Swagger.
- Implementación de inicio de sesión con JWT.
- Conexión a una base de datos MySQL.
- Integración con el frontend proporcionado por Digital House.

### Analista de Base de Datos

- Modelado de la base de datos con un diagrama DER.
- Creación e implementación de la base de datos MySQL.
- Documentación opcional de la creación de la BD (DUMP).

### QA / Analista de Testing

- Identificación y notificación de defectos (errores).
- Creación de casos de prueba y pruebas de humo/regresión.
- Automatización de pruebas con RestAssured y Java.

### Analista de Infraestructura

- Despliegue del frontend utilizando Vercel.
- Implementación de instancia Bucket S3 en AWS para almacenamiento.
- Conexión a una base de datos proporcionada.
- Creación opcional de integración continua con pipelines.

## Resultados Esperados

El objetivo final del proyecto es crear un repositorio GitLab con todo el código, documentación y diseño de infraestructura. Además, se debe entregar un documento de proyecto que incluya:

- Objetivos del proyecto.
- Planificación y descripción del backlog con plazos estimados.
- Informes de entrega.
- Informes de retro personal.
- Lecciones aprendidas.

## Sprints

### Sprint 1

- Crear servicios para registro, inicio y cierre de sesión.
- Desarrollar testing unitario y plan de pruebas.
- Implementar manejo de errores.

### Sprint 2

- Implementar endpoints para consulta y actualización de datos de usuario.
- Desarrollar servicios para gestión de medios de pago y ingreso de dinero.

### Sprint 3

- Crear endpoints para transferencia de dinero entre usuarios.
- Desarrollar dashboard de usuario con información de cuenta y movimientos.

### Sprint 4

- Implementar servicio de actividad del usuario con filtros de búsqueda.

## Diseño de la Infraestructura

### Infraestructura Propuesta

- **Servidores:** Utilización de servidores cloud para despliegue de microservicios.
- **Almacenamiento:** Base de datos MySQL para almacenamiento de datos de usuario y transacciones.
- **Red Interna:** Conexión segura entre servicios mediante redes privadas virtuales (VPNs).
- **Base de Datos:** MySQL para persistencia de datos.
- **Despliegue:** Utilización de Docker y Kubernetes para contenerización y orquestación de servicios.

### Diagrama de Arquitectura

![Diagrama de Arquitectura](./img/DiagramaArquitectura.png)

