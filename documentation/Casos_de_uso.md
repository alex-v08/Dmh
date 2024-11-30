# Diagramas de Casos de Uso - Digital Money House

## 1. Sistema de Autenticación y Usuarios

```mermaid
graph TD
    subgraph "Sistema de Autenticación"
        A[Usuario No Registrado] -->|Registrarse| B[Registro de Usuario]
        A -->|Iniciar Sesión| C[Login]
        D[Usuario Registrado] -->|Cerrar Sesión| E[Logout]
        D -->|Actualizar Perfil| F[Gestión de Perfil]
        D -->|Cambiar Contraseña| G[Gestión de Contraseña]
        
        B --> H[Validar Datos]
        B --> I[Crear Cuenta]
        C --> J[Validar Credenciales]
        C --> K[Generar Token]
        
        F --> L[Actualizar Información Personal]
        G --> M[Validar Contraseña Actual]
        G --> N[Actualizar Contraseña]
    end
```

## 2. Sistema de Cuentas

```mermaid
graph TD
    subgraph "Gestión de Cuentas"
        A[Usuario] -->|Consultar Saldo| B[Ver Balance]
        A -->|Ver Historial| C[Consultar Movimientos]
        A -->|Obtener CVU| D[Consultar CVU]
        A -->|Obtener Alias| E[Consultar Alias]
        
        B --> F[Mostrar Saldo Actual]
        C --> G[Filtrar Transacciones]
        C --> H[Exportar Movimientos]
        D --> I[Mostrar CVU]
        E --> J[Mostrar/Editar Alias]
        
        K[Sistema] -->|Actualizar Saldo| L[Procesar Transacción]
        K -->|Generar CVU| M[Crear CVU Único]
        K -->|Generar Alias| N[Crear Alias Único]
    end
```

## 3. Sistema de Tarjetas

```mermaid
graph TD
    subgraph "Gestión de Tarjetas"
        A[Usuario] -->|Agregar Tarjeta| B[Alta de Tarjeta]
        A -->|Eliminar Tarjeta| C[Baja de Tarjeta]
        A -->|Ver Tarjetas| D[Listar Tarjetas]
        A -->|Validar Tarjeta| E[Verificación de Tarjeta]
        
        B --> F[Validar Datos]
        B --> G[Registrar Tarjeta]
        C --> H[Confirmar Eliminación]
        D --> I[Mostrar Tarjetas Activas]
        E --> J[Verificar Titular]
        E --> K[Verificar Vencimiento]
        E --> L[Verificar CVV]
    end
```

## 4. Sistema de Transacciones

```mermaid
graph TD
    subgraph "Gestión de Transacciones"
        A[Usuario] -->|Realizar Transferencia| B[Transferir Dinero]
        A -->|Ingresar Dinero| C[Cargar Saldo]
        A -->|Ver Historial| D[Consultar Transacciones]
        
        B --> E[Validar Fondos]
        B --> F[Verificar Destino]
        B --> G[Ejecutar Transferencia]
        B --> H[Generar Comprobante]
        
        C --> I[Seleccionar Método]
        C --> J[Procesar Pago]
        C --> K[Actualizar Saldo]
        
        D --> L[Filtrar por Fecha]
        D --> M[Filtrar por Tipo]
        D --> N[Filtrar por Estado]
    end
```

## 5. Sistema de Notificaciones

```mermaid
graph TD
    subgraph "Gestión de Notificaciones"
        A[Sistema] -->|Transferencia Exitosa| B[Notificar Operación]
        A -->|Ingreso de Dinero| C[Notificar Depósito]
        A -->|Cambios de Seguridad| D[Notificar Seguridad]
        
        B --> E[Enviar Email]
        B --> F[Notificación Push]
        C --> E
        C --> F
        D --> E
        D --> F
        
        G[Usuario] -->|Configurar Preferencias| H[Gestionar Notificaciones]
        H --> I[Activar/Desactivar Email]
        H --> J[Activar/Desactivar Push]
    end
```

## 6. Flujo General del Sistema

```mermaid
graph TD
    subgraph "Flujo Principal"
        A[Usuario] -->|Registro| B[Crear Cuenta]
        B -->|Éxito| C[Cuenta Digital]
        C -->|Activación| D[Cuenta Activa]
        
        D -->|Carga Inicial| E[Ingreso de Dinero]
        D -->|Agregar Método de Pago| F[Gestión de Tarjetas]
        D -->|Realizar Operación| G[Transacciones]
        
        G -->|Transferir| H[Transferencias]
        G -->|Recibir| I[Depósitos]
        G -->|Consultar| J[Movimientos]
        
        K[Sistema] -->|Monitoreo| L[Seguridad]
        K -->|Actualización| M[Estados de Cuenta]
        K -->|Comunicación| N[Notificaciones]
    end
```

## 7. Sistema de Reportes y Analíticas

```mermaid
graph TD
    subgraph "Reportes y Analíticas"
        A[Usuario] -->|Ver Estadísticas| B[Dashboard Personal]
        A -->|Descargar Reportes| C[Generación de Informes]
        A -->|Analizar Gastos| D[Análisis de Transacciones]
        
        B --> E[Resumen de Cuenta]
        B --> F[Gráficos de Movimientos]
        B --> G[Indicadores Principales]
        
        C --> H[Reporte Mensual]
        C --> I[Reporte Anual]
        C --> J[Comprobantes]
        
        D --> K[Categorización]
        D --> L[Tendencias]
        D --> M[Recomendaciones]
    end
```

## 8. Sistema de Administración

```mermaid
graph TD
    subgraph "Panel Administrativo"
        A[Administrador] -->|Gestionar Usuarios| B[Administración de Usuarios]
        A -->|Monitorear Sistema| C[Dashboard Administrativo]
        A -->|Gestionar Parámetros| D[Configuración del Sistema]
        
        B --> E[Alta/Baja Usuarios]
        B --> F[Bloqueo de Cuentas]
        B --> G[Reseteo de Contraseñas]
        
        C --> H[Métricas del Sistema]
        C --> I[Logs de Operaciones]
        C --> J[Alertas de Seguridad]
        
        D --> K[Límites Operativos]
        D --> L[Parámetros de Seguridad]
        D --> M[Configuración de Servicios]
    end
```

Cada diagrama representa un subsistema específico de Digital Money House, mostrando las interacciones principales entre los usuarios y el sistema. Los diagramas están organizados de manera jerárquica, comenzando con las funcionalidades básicas y progresando hacia operaciones más complejas y específicas.

Las relaciones entre los diferentes casos de uso están representadas mediante flechas, indicando el flujo de las operaciones y las dependencias entre las diferentes funcionalidades. Los actores principales (Usuario, Sistema, Administrador) están claramente identificados, así como las acciones que pueden realizar dentro del sistema.

Estos diagramas proporcionan una vista completa de las funcionalidades del sistema y pueden servir como guía para el desarrollo y la documentación del proyecto. Se pueden expandir o modificar según las necesidades específicas del proyecto y los requerimientos adicionales que surjan durante el desarrollo.