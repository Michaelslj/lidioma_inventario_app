# Inventario Pro - Sistema de Gestión de Inventario

## 1. Descripción de la Aplicación
**Inventario Pro** es una aplicación móvil avanzada diseñada para el control y gestión de stock empresarial. Desarrollada con **Kotlin** y **Jetpack Compose**, ofrece una experiencia de usuario fluida con un diseño "Dark Pro". La aplicación permite a las empresas gestionar productos, categorías, proveedores y procesos de compra, con un sistema de roles integrado que protege las acciones administrativas.

**Características principales:**
*   **Gestión Multicanal:** CRUD completo para productos, categorías y proveedores.
*   **Control de Stock en Tiempo Real:** Actualización automática de existencias basada en registros de movimientos.
*   **Inteligencia de Compras:** Creación de órdenes de compra con cálculo automático de totales y selección masiva de productos.
*   **Seguridad:** Autenticación JWT con renovación automática de tokens y sistema de roles (Admin/Estándar).

---

## 2. Requisitos de Instalación
*   **Android Studio:** Jellyfish | 2023.3.1 o superior.
*   **JDK:** Java 17.
*   **SDK de Android:** API 26 (Android 8.0) como mínimo.
*   **Gradle:** Versión 8.0 o superior.
*   **Dispositivo:** Emulador o dispositivo físico con conexión a internet para sincronización con el backend.

---

## 3. Configuración del Backend
La aplicación se comunica con un backend desarrollado en Django. La URL base está configurada estáticamente en el módulo de red de la aplicación.

*   **URL Base:** `http://lidioma-inventario.uaeftt-ute.site/api/`
*   **Formato de datos:** JSON (UTF-8).
*   **Seguridad:** Todas las rutas, excepto login y registro, requieren una cabecera `Authorization: Bearer <token>`.

---

## 4. Credenciales de Prueba
Para probar todas las funcionalidades administrativas (crear, editar, borrar):

| Rol | Usuario | Contraseña |
| :--- | :--- | :--- |
| **Administrador** | `maicol` | *(Usa la contraseña de tu superusuario)* |
| **Usuario Estándar** | *(Cualquiera)* | *(Registrarse directamente en la app)* |

---

## 5. Capturas de Pantalla
> *Las imágenes se encuentran en la carpeta `/docs/screenshots/` del repositorio.*
1.  **Login & Registro:** Pantalla de acceso con validación de credenciales.
2.  **Lista de Stock:** Visualización de productos con alertas de stock bajo.
3.  **Movimientos:** Historial de entradas y salidas de bodega.
4.  **Órdenes de Compra:** Formulario de creación con cálculo de precios totales.
5.  **Perfil Pro:** Identificación de rango (Admin/Usuario) y cierre de sesión seguro.

---

## 6. Explicación de las 7 Entidades
1.  **User (Usuario):** Almacena la información del perfil (username, email, nombre) y define los permisos de acceso.
2.  **Auth (Autenticación):** Gestiona el flujo de tokens JWT (Access y Refresh) para mantener la sesión activa.
3.  **Producto:** Contiene el nombre, precio, descripción, stock actual y relación con categoría.
4.  **Categoría:** Agrupación lógica de productos con generación automática de Slugs para URLs.
5.  **Proveedor:** Registro de entidades externas con RUC, teléfono, email y dirección física.
6.  **MovimientoInventario:** Entidad que registra transacciones de ENTRADA o SALIDA, afectando directamente el stock del producto vinculado.
7.  **OrdenCompra:** Documento que agrupa varios productos y cantidades para solicitar a un proveedor, calculando el costo total estimado.

---

## 7. Listado de Pantallas
*   **Pantalla de Login:** Acceso seguro.
*   **Pantalla de Registro:** Creación de nuevos usuarios (rol estándar).
*   **Dashboard de Productos (Stock):** Lista principal con búsqueda y filtros.
*   **Gestión de Categorías:** Listado y CRUD de clasificaciones.
*   **Historial de Movimientos:** Registro detallado de quién y cuándo alteró el stock.
*   **Módulo de Proveedores:** Gestión de contactos comerciales (Solo Admin).
*   **Generador de Órdenes:** Creación de solicitudes de compra (Solo Admin).
*   **Pantalla de Perfil:** Visualización de datos de cuenta y gestión de sesión.

---

## 8. Ejemplos de Consumo de la API con Token
La aplicación utiliza un Interceptor para inyectar el token automáticamente en cada petición.

**Ejemplo de Petición (Obtener Productos):**
```http
GET /api/productos/
Host: lidioma-inventario.uaeftt-ute.site
Authorization: Bearer <TU_TOKEN_AQUÍ>
Accept: application/json
```

**Ejemplo de Petición (Crear Movimiento):**
```http
POST /api/movimientos/
Authorization: Bearer <TU_TOKEN_AQUÍ>
Content-Type: application/json

{
    "producto": 1,
    "tipo": "ENTRADA",
    "cantidad": 10,
    "motivo": "Repaso de stock mensual"
}
```

---

## 9. Instrucciones para Ejecutar la App
1.  Descargar el código fuente o clonar el repositorio.
2.  Abrir **Android Studio** y seleccionar "Open Project".
3.  Esperar a que Gradle descargue las dependencias (Sync).
4.  En la parte superior, seleccionar un dispositivo (Emulador o Físico).
5.  Hacer clic en el icono verde de **Run (Play)**.
6.  La app se instalará e iniciará en la pantalla de Login.

---
**Desarrollado para el Proyecto de Inventario - 2024**
