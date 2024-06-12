# MilkyRoutes

MilkyRoutes es un proyecto desarrollado en Java que utiliza algoritmos avanzados y estructuras de datos para optimizar rutas en la distribución de productos lácteos. Utiliza un grafo para representar las conexiones y aplica algoritmos de caminos mínimos para encontrar las rutas óptimas.

## Descripción del Proyecto

El proyecto MilkyRoutes está diseñado para gestionar y optimizar la distribución de productos lácteos mediante el uso de algoritmos avanzados y estructuras de datos eficientes. El sistema permite registrar productores, tambos (establecimientos de ordeñe), ciudades y centros de pasteurización, así como definir tramos entre ellos utilizando un grafo.

### Funcionalidades Principales

- **Registro de Productores:** Permite registrar productores con su información personal y de contacto.
- **Registro de Ciudades:** Permite registrar ciudades con sus coordenadas geográficas.
- **Registro de Tambos:** Permite registrar tambos asociados a productores específicos, con su ubicación y capacidad.
- **Registro de Centros de Pasteurización:** Permite registrar centros de pasteurización con su ubicación y capacidad.
- **Definición de Tramos:** Permite definir tramos entre puntos en el mapa, especificando la distancia entre ellos.
- **Optimización de Rutas:** Utiliza el algoritmo de Dijkstra para encontrar la ruta más corta entre un tambo y el centro de pasteurización más cercano.

## Estructuras de Datos y Algoritmos Utilizados

- **Grafo:** Representa las conexiones entre ciudades, tambos y centros de pasteurización.
- **Árbol AVL:** Utilizado para almacenar y gestionar la información de los productores de manera eficiente.
- **Hashing:** Empleado para la rápida búsqueda y almacenamiento de puntos en el mapa.
- **Algoritmo de Dijkstra:** Utilizado para encontrar el camino más corto en el grafo, optimizando las rutas de distribución.

## Requisitos

- **Java 11 o superior**
- **Maven**

## Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/srodriguezamarillo/milkRoutes.git
   cd milkRoutes

2. Compila el proyecto con Maven:
   ```bash
   mvn clean install

## Ejecución de Pruebas

1. Para ejecutar las pruebas del proyecto, utiliza el siguiente comando:
   ```bash
   mvn test

## Estructura del Proyecto

El proyecto está estructurado de la siguiente manera:

- **src/main/java:** Contiene el código fuente principal del proyecto.
- **src/test/java:** Contiene las pruebas unitarias del proyecto.
- **pom.xml:** Archivo de configuración de Maven.

## Contribución
Si deseas contribuir a este proyecto, por favor sigue los siguientes pasos:

1. Haz un fork del repositorio.
2. Crea una nueva rama para tus cambios:
   ```bash
   git checkout -b feature-nueva-funcionalidad

3. Realiza tus cambios y haz commit de los mismos:
   ```bash
   git commit -m 'Agregar nueva funcionalidad'

4. Sube tus cambios a tu repositorio fork:
   ```bash
   git push origin feature-nueva-funcionalidad

5. Abre un Pull Request en el repositorio original.

## Licencia
Para más detalles, consulta el archivo LICENSE.

## Contacto
Si tienes alguna pregunta o sugerencia, no dudes en ponerte en contacto:

- **Email:**  srodriguezamarillo@gmail.com
- **LinkedIn:** www.linkedin.com/in/sebastianrodriguezamarillo