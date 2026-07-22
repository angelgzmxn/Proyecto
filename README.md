# Propuesta de Arquitectura — Sistema de Recordatorios (API REST)

## Objetivo del proyecto

Desarrollar una API REST en Spring Boot (Kotlin) que permita a los usuarios registrar tareas o eventos y recibir un recordatorio automático (notificación push, correo, SMS o WhatsApp) en el momento que ellos definan. El sistema está dividido en 10 módulos independientes siguiendo una arquitectura por capas.

---

## 1. Módulo de Autenticación y Autorización

**Propósito / Alcance**
Gestiona el acceso seguro al sistema, validando la identidad de los usuarios y controlando qué acciones puede ejecutar cada uno según su rol.

**Entidades Principales**
- Usuario (referencia)
- Rol
- Token / Sesión

**Operaciones / Funcionalidades Clave**
- Iniciar sesión (login) y generar token JWT
- Registrar un nuevo usuario
- Renovar/invalidar token (refresh / logout)

**Endpoints Estimados**
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/register`
- `POST /api/v1/auth/refresh`

---

## 2. Módulo de Gestión de Usuarios

**Propósito / Alcance**
Administra la información personal y de contacto de cada usuario, necesaria para poder enviarle los recordatorios por el canal correcto.

**Entidades Principales**
- Usuario
- Perfil de Usuario

**Operaciones / Funcionalidades Clave**
- Consultar y actualizar datos del perfil (nombre, correo, teléfono)
- Cambiar contraseña
- Activar / desactivar cuenta

**Endpoints Estimados**
- `GET /api/v1/users/{id}`
- `PUT /api/v1/users/{id}`
- `PATCH /api/v1/users/{id}/password`

---

## 3. Módulo de Recordatorios (Núcleo del sistema)

**Propósito / Alcance**
Es el módulo central: permite al usuario crear, consultar, modificar y eliminar los recordatorios que desea recibir, incluyendo fecha y hora de disparo.

**Entidades Principales**
- Recordatorio (Reminder)
- Estado del Recordatorio (pendiente, notificado, completado, vencido)

**Operaciones / Funcionalidades Clave**
- Crear un recordatorio (título, descripción, fecha/hora)
- Editar o reprogramar un recordatorio existente
- Eliminar un recordatorio
- Listar recordatorios por usuario y por estado

**Endpoints Estimados**
- `POST /api/v1/reminders`
- `GET /api/v1/reminders`
- `PUT /api/v1/reminders/{id}`

---

## 4. Módulo de Notificaciones

**Propósito / Alcance**
Se encarga de enviar el mensaje real al usuario en el momento programado, integrándose con servicios externos (WhatsApp/SMS vía Twilio, correo electrónico).

**Entidades Principales**
- Notificación
- Canal de Notificación (WhatsApp, SMS, Email, Push)

**Operaciones / Funcionalidades Clave**
- Enviar notificación por el canal configurado por el usuario
- Registrar el resultado del envío (enviado / fallido)
- Reintentar envíos fallidos

**Endpoints Estimados**
- `GET /api/v1/notifications/{userId}`
- `POST /api/v1/notifications/send` *(uso interno/pruebas)*
- `GET /api/v1/notifications/{id}/status`

---

## 5. Módulo de Programación (Scheduler)

**Propósito / Alcance**
Revisa periódicamente qué recordatorios están próximos a vencer o ya vencieron, y dispara el proceso de notificación en el momento exacto definido por el usuario.

**Entidades Principales**
- Tarea Programada (Job)
- Recurrencia (diaria, semanal, mensual)

**Operaciones / Funcionalidades Clave**
- Revisar recordatorios pendientes cada cierto intervalo de tiempo
- Disparar la notificación cuando se cumple la hora programada
- Generar la siguiente ocurrencia de un recordatorio recurrente

**Endpoints Estimados**
- `GET /api/v1/scheduler/status`
- `POST /api/v1/scheduler/run-now` *(forzar ejecución manual, útil para pruebas/demo)*

---

## 6. Módulo de Categorías / Etiquetas

**Propósito / Alcance**
Permite clasificar los recordatorios por tipo (trabajo, salud, estudio, personal) para que el usuario organice y filtre mejor su información.

**Entidades Principales**
- Categoría
- Recordatorio_Categoría (relación)

**Operaciones / Funcionalidades Clave**
- Crear y administrar categorías
- Asignar una o varias categorías a un recordatorio
- Filtrar recordatorios por categoría

**Endpoints Estimados**
- `GET /api/v1/categories`
- `POST /api/v1/categories`
- `GET /api/v1/reminders?categoryId={id}`

---

## 7. Módulo de Preferencias de Usuario

**Propósito / Alcance**
Guarda la configuración personal de cada usuario sobre cómo y cuándo quiere ser notificado (canal preferido, zona horaria, horario de silencio).

**Entidades Principales**
- Preferencia de Notificación
- Zona Horaria

**Operaciones / Funcionalidades Clave**
- Configurar el canal preferido de notificación (WhatsApp, SMS, correo)
- Definir zona horaria del usuario
- Activar/desactivar tipos de notificación

**Endpoints Estimados**
- `GET /api/v1/preferences/{userId}`
- `PUT /api/v1/preferences/{userId}`

---

## 8. Módulo de Historial y Auditoría

**Propósito / Alcance**
Mantiene un registro histórico de los recordatorios ya notificados o completados, permitiendo trazabilidad de lo que el sistema envió.

**Entidades Principales**
- Historial de Recordatorio
- Log de Auditoría

**Operaciones / Funcionalidades Clave**
- Consultar historial de recordatorios pasados de un usuario
- Registrar cada cambio de estado de un recordatorio
- Exportar historial (ej. a CSV)

**Endpoints Estimados**
- `GET /api/v1/history/{userId}`
- `GET /api/v1/history/{userId}/export`

---

## 9. Módulo de Reportes y Estadísticas

**Propósito / Alcance**
Genera métricas de uso que ayudan al usuario a entender su comportamiento (cuántos recordatorios cumple, cuántos posterga, etc.).

**Entidades Principales**
- Reporte (agregación calculada, no necesariamente tabla propia)

**Operaciones / Funcionalidades Clave**
- Calcular porcentaje de recordatorios completados vs. vencidos
- Mostrar recordatorios por categoría en un rango de fechas
- Generar resumen semanal/mensual de actividad

**Endpoints Estimados**
- `GET /api/v1/reports/summary`
- `GET /api/v1/reports/by-category`

---

## 10. Módulo de Administración

**Propósito / Alcance**
Ofrece funciones exclusivas para el rol ADMIN, permitiendo la gestión general de usuarios y la supervisión del sistema.

**Entidades Principales**
- Usuario (con rol ADMIN)
- Panel de Control / Métricas del Sistema

**Operaciones / Funcionalidades Clave**
- Listar y gestionar todos los usuarios del sistema
- Activar / desactivar cuentas de usuario
- Consultar métricas generales (usuarios activos, notificaciones enviadas)

**Endpoints Estimados**
- `GET /api/v1/admin/users`
- `PATCH /api/v1/admin/users/{id}/status`
- `GET /api/v1/admin/metrics`

---

## Resumen de Arquitectura

| # | Módulo | Entidad Principal |
|---|--------|-------------------|
| 1 | Autenticación y Autorización | Usuario, Rol |
| 2 | Gestión de Usuarios | Usuario, Perfil |
| 3 | Recordatorios | Recordatorio |
| 4 | Notificaciones | Notificación |
| 5 | Programación (Scheduler) | Job, Recurrencia |
| 6 | Categorías / Etiquetas | Categoría |
| 7 | Preferencias de Usuario | Preferencia |
| 8 | Historial y Auditoría | Historial, Log |
| 9 | Reportes y Estadísticas | Reporte |
| 10 | Administración | Usuario (ADMIN) |
