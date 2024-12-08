CRM Store

CRM Store es una aplicación móvil diseñada para la gestión de ventas, clientes, empleados y productos en una tienda de ropa. Este proyecto fue desarrollado como parte de un trabajo universitario para aprender y aplicar conocimientos en desarrollo de aplicaciones móviles utilizando Kotlin, Jetpack Compose y Firebase.

Funcionalidades Principales

1. Gestión de Ventas

Permite registrar nuevas ventas seleccionando empleados, clientes y productos, mostrando el total calculado automáticamente.

2. Gestión de Clientes y Empleados

Funcionalidad para listar, editar, eliminar y añadir clientes y empleados desde una base de datos sincronizada con Firebase.

3. Carrito de Compras

Selección dinámica de productos con cantidades y precios para cada venta.

4. Dashboard de Ventas

Un panel que muestra estadísticas como:
	•	Total de ventas realizadas.
	•	Producto más vendido.
	•	Promedio de venta por cliente.

5. Login y Registro

Los usuarios pueden registrarse y acceder al sistema utilizando:
	•	Autenticación por correo y contraseña.
	•	Autenticación con Google.
	•	Autenticación con Apple (en dispositivos compatibles).

Tecnologías Utilizadas
	•	Lenguaje: Kotlin.
	•	Interfaz de Usuario: Jetpack Compose con Material 3.
	•	Base de Datos: Firebase Firestore para almacenamiento en la nube.
	•	Autenticación: Firebase Authentication (correo/contraseña, Google, Apple).
	•	Navegación: Jetpack Navigation para el flujo entre pantallas.

Estructura del Proyecto

El proyecto está organizado siguiendo la arquitectura MVVM (Model-View-ViewModel) y se divide en las siguientes capas:

1. Modelo (modelo/)

Contiene las clases que representan los datos principales de la aplicación. Ejemplo:
	•	Cliente.kt: Representa un cliente.
	•	Venta.kt: Representa una venta, incluyendo los detalles de los productos vendidos.

2. Repositorio (controlador/)

Maneja la lógica de negocio y la interacción con Firebase. Ejemplo:
	•	VentaRepository.kt: Realiza operaciones relacionadas con las ventas (obtener, guardar, eliminar).

3. Vista (ui/)

Se organiza en módulos según la funcionalidad de la aplicación:
	•	Pantallas (ui/screens/):
Pantallas principales como:
	•	PantallaVentas.kt: Visualización y gestión de ventas.
	•	PantallaAddVentas.kt: Formulario para añadir nuevas ventas.
	•	PantallaDashboardVentas.kt: Panel con estadísticas de ventas.
	•	Componentes (ui/componentes/):
Elementos reutilizables de la UI. Ejemplo:
	•	BotonEstandar.kt: Un botón con un diseño reutilizable.

4. ViewModel (viewmodel/)

Contiene la lógica de presentación que conecta la vista con el repositorio. Ejemplo:
	•	VentaViewModel.kt:
Maneja el estado de las ventas, incluyendo:
	•	Gestión del carrito.
	•	Cálculo de totales.
	•	Identificación de productos más vendidos.

5. Tema y Navegación
	•	theme/:
Define los colores, tipografías y formas para un diseño consistente.
	•	NavigationApp.kt:
Configura la navegación entre pantallas utilizando NavHostController.
