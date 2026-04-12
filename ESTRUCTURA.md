# Estructura de Clases GUI - BiblioTrack

## Jerarquía de Componentes

```
AppGui (Main)
    │
    └── LoginDialog
            │
            └── MainFrame
                    │
                    ├── TopBar (navegación)
                    │   ├── Logo "BIBLIOTECA"
                    │   ├── Botón INICIO
                    │   ├── Botón BÚSQUEDA
                    │   └── Menú TU CUENTA
                    │       ├── Ver cuenta
                    │       └── Cerrar sesión
                    │
                    ├── PanelInicio (CardLayout: "INICIO")
                    │   ├── Recomendaciones Semanales (6 libros)
                    │   ├── Sección Novelas (6 libros)
                    │   └── Sección Comics (6 libros)
                    │
                    ├── PanelBusqueda (CardLayout: "BUSCAR")
                    │   ├── Filtros
                    │   │   ├── Campo búsqueda
                    │   │   ├── ComboBox Género
                    │   │   └── ComboBox Tipo
                    │   ├── Tabla resultados
                    │   └── Botón Agregar Publicación
                    │       └── DialogoAgregarPublicacion
                    │           ├── ComboBox Tipo
                    │           ├── ComboBox Género
                    │           └── Campos específicos (dinámicos)
                    │
                    ├── PanelCuenta (CardLayout: "CUENTA")
                    │   ├── Info usuario
                    │   ├── Tab "Sin Leer" (PENDIENTE)
                    │   ├── Tab "En Proceso" (LEYENDO)
                    │   ├── Tab "Leídos" (LEIDO)
                    │   └── Botón Cambiar Estado
                    │
                    ├── PanelDetalleLibro (CardLayout: "DETALLE")
                    │   ├── Botón ← Volver
                    │   ├── Portada + Información
                    │   ├── Botón Cambiar Estado
                    │   ├── Lista Reseñas
                    │   ├── Formulario Nueva Reseña
                    │   └── Tabla Ranking Género
                    │
                    └── StatusBar (información de sesión)
```

## Flujos de Navegación

### 1. Flujo de Inicio de Sesión
```
AppGui → LoginDialog → MainFrame (PanelInicio)
```

### 2. Flujo de Navegación Principal
```
MainFrame
    ├─ Click INICIO → PanelInicio
    ├─ Click BÚSQUEDA → PanelBusqueda
    └─ Click TU CUENTA → Menu
        ├─ Ver cuenta → PanelCuenta
        └─ Cerrar sesión → LoginDialog
```

### 3. Flujo de Agregar Publicación
```
PanelBusqueda 
    → Botón "Agregar Publicación" 
    → DialogoAgregarPublicacion
    → Guardar
    → Actualiza PanelBusqueda
```

### 4. Flujo de Ver Detalles
```
PanelInicio/PanelBusqueda/PanelCuenta
    → Click/Doble Click en libro
    → PanelDetalleLibro
    → Botón "← Volver"
    → PanelInicio
```

### 5. Flujo de Cambiar Estado
```
PanelCuenta/PanelDetalleLibro
    → Botón "Cambiar Estado"
    → Diálogo selección
    → Actualiza estado
    → Guarda automáticamente
```

## Responsabilidades de Cada Clase

### AppGui
- Punto de entrada de la aplicación
- Carga datos iniciales
- Muestra LoginDialog

### LoginDialog
- Autenticación de usuario
- Creación automática de usuario si no existe
- Lanza MainFrame

### MainFrame
- Gestión de navegación (CardLayout)
- Barra superior con menús
- Coordina todos los paneles
- Método público: `abrirDetalle(Publicacion)`
- Método público: `volverInicio()`

### PanelInicio
- Muestra recomendaciones (top 6 por calificación)
- Muestra novelas (primeras 6)
- Muestra comics (primeros 6)
- Tarjetas clickeables

### PanelBusqueda
- Búsqueda por texto (título/autor)
- Filtrado por género
- Filtrado por tipo
- Tabla de resultados
- Acceso a DialogoAgregarPublicacion

### DialogoAgregarPublicacion
- Formulario de nueva publicación
- Géneros predefinidos (ComboBox)
- Campos dinámicos según tipo
- Validación de datos
- Guardado automático

### PanelCuenta
- Información de usuario
- Tres tabs por estado de lectura
- Tablas de publicaciones
- Cambio de estado de lectura
- Actualización de datos

### PanelDetalleLibro
- Información completa de publicación
- Lista de todas las reseñas
- Formulario para agregar reseña
- Tabla de ranking del género
- Cambio de estado
- Botón volver

## Constantes Compartidas

### Géneros (Array)
```java
String[] GENEROS = {
    "Thriller", "Romance", "Ficción", "Ciencia/Letras", 
    "Terror", "Clásicos", "Comic", "Manga"
};
```

### Tipos de Publicación (Array)
```java
String[] TIPOS = {
    "Novela", "Comic", "Libro Técnico"
};
```

### Niveles (para Libro Técnico)
```java
String[] NIVELES = {
    "Básico", "Intermedio", "Avanzado", "Experto"
};
```

### Estados de Lectura (Enum)
```java
enum EstadoLectura {
    PENDIENTE,  // Mostrado como "-"
    LEYENDO,    // Mostrado como "En Proceso"
    LEIDO       // Mostrado como "Leído"
}
```

### Colores del Sistema
```java
Color NARANJA_PRINCIPAL = new Color(0xE87722);
Color BLANCO = Color.WHITE;
Color GRIS_CLARO = Color.LIGHT_GRAY;
Color GRIS = Color.GRAY;
```

## Métodos de Guardado

Todos los cambios se guardan automáticamente usando:
```java
GestorArchivos.guardarDatos(
    biblioteca,
    "data/biblioteca/biblioteca.csv",
    "data/usuarios/usuarios.csv",
    "data/biblioteca/biblioteca.json"
);
```

Se ejecuta automáticamente en:
- Agregar publicación
- Agregar reseña
- Cambiar estado de lectura
- Cerrar sesión

## Listeners y Eventos

### MainFrame
- ActionListener en botones INICIO, BÚSQUEDA
- ActionListener en menú TU CUENTA

### PanelInicio
- MouseListener en tarjetas de libro (click → detalle)
- MouseListener hover (cambio de borde)

### PanelBusqueda
- ActionListener en botón Buscar
- ActionListener en botón Limpiar
- ActionListener en botón Agregar Publicación
- MouseListener doble click en tabla (→ detalle)

### PanelCuenta
- MouseListener doble click en tablas (→ detalle)
- ActionListener en botón Cambiar Estado
- ActionListener en botón Actualizar

### PanelDetalleLibro
- ActionListener en botón Volver
- ActionListener en botón Cambiar Estado
- ActionListener en botón Guardar Reseña

### DialogoAgregarPublicacion
- ActionListener en ComboBox Tipo (→ actualiza campos)
- ActionListener en botón Guardar
- ActionListener en botón Cancelar
```
