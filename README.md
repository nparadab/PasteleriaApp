Pastelería Mil Sabores – Aplicación Móvil
Aplicación móvil desarrollada para la Evaluación Final Transversal (EFT) de la asignatura DSY 1105 – Desarrollo de Aplicaciones Móviles. El proyecto integra backend propio, API externa, recursos nativos, animaciones, roles de usuario y pruebas unitarias.


Integrantes
Nicolás Parada 
Ana Pasarín


Descripción General
Mil Sabores es una aplicación móvil diseñada para la gestión de una pastelería. Incluye distintos roles de usuario, cada uno con permisos diferenciados:

Administrador: gestión completa de usuarios y productos
Vendedor: gestión de productos
Pastelero: consulta de recetas mediante API externa
Cliente: visualización de productos con filtros

La app se conecta a un backend propio desarrollado en Spring Boot y a una API externa para recetas.
Funcionalidades Principales

 Autenticación
Inicio de sesión con roles
Recuperación de contraseña
Persistencia local del token (SharedPreferences)
 Gestión de Productos
CRUD completo (Admin/Vendedor)
Listado con filtros por categoría y stock
Vista especial para clientes
 Gestión de Usuarios
Listado de usuarios (solo Admin)
API Externa – TheMealDB
Búsqueda de recetas por nombre
Integración visual en pantalla de Pastelero
 Recursos Nativos
Vibración en botones clave
Persistencia local con SharedPreferences
 UI/UX
Paleta pastel personalizada
Animaciones (bounce)
Navegación fluida
Pantallas coherentes con el contexto
Pruebas Unitarias
Incluye pruebas unitarias para:
API externa (TheMealDB)
API externa (OpenMeteo)
Validación de respuestas
Comportamiento esperado
Todas las pruebas pasan correctamente.
Arquitectura
MVVM
Retrofit + Coroutines
RecyclerView + Adapters
SharedPreferences
Spring Boot (backend)
Render (hosting backend)
URL Base del Backend
Código
https://milsabores-api.onrender.com/api

Endpoints del Backend
Autenticación
Código
POST /auth/register
POST /auth/login
POST /auth/recuperar

Usuarios (ADMIN)
Código
GET    /usuarios
POST   /usuarios
PUT    /usuarios/{id}
DELETE /usuarios/{id}

Productos
Código
GET    /productos
GET    /productos/{id}
POST   /productos
PUT    /productos/{id}
DELETE /productos/{id}

Categorías
Código
GET    /categorias
GET    /categorias/{id}
POST   /categorias
PUT    /categorias/{id}
DELETE /categorias/{id}

Cómo Ejecutar el Proyecto
Backend
Abrir proyecto Spring Boot
Configurar base de datos
Ejecutar con:
Código
mvn spring-boot:run

App Móvil
Abrir en Android Studio
Sincronizar Gradle
Ejecutar en emulador o dispositivo real
Iniciar sesión con un usuario válido del backend
APK Firmado
Ubicación: /app/release/app-release.apk
Archivo .jks incluido en /keystore/
Configuración en build.gradle (signingConfigs)
Pruebas Unitarias
Ejecutar con:
Código
./gradlew testDebugUnitTest
