package com.example.sistema;

/**
 * Interfaz para el sistema de gestión de puntos y productores.
 */
public interface ISistema {

	/**
	 * Enumeración de los posibles tipos de errores que puede retornar el sistema.
	 */
	public enum TipoError {
		OK, ERROR_1, ERROR_2, ERROR_3, ERROR_4, ERROR_5, NO_IMPLEMENTADA
	}

	/**
	 * Inicializa el sistema con la cantidad de puntos especificada.
	 *
	 * @param cantPuntos la cantidad de puntos a inicializar
	 * @return un objeto Retorno indicando el resultado de la operación
	 */
	Retorno inicializarSistema(int cantPuntos);

	/**
	 * Destruye el sistema, liberando todos los recursos utilizados.
	 *
	 * @return un objeto Retorno indicando el resultado de la operación
	 */
	Retorno destruirSistema();

	/**
	 * Registra un productor en el sistema.
	 *
	 * @param cedula el número de cédula del productor
	 * @param nombre el nombre del productor
	 * @param direccion la dirección del productor
	 * @param email el correo electrónico del productor
	 * @param celular el número de celular del productor
	 * @return un objeto Retorno indicando el resultado de la operación
	 */
	Retorno registrarProductor(String cedula, String nombre, String direccion, String email, String celular);

	/**
	 * Registra una ciudad en el sistema.
	 *
	 * @param nombre el nombre de la ciudad
	 * @param coordX la coordenada X de la ciudad
	 * @param coordY la coordenada Y de la ciudad
	 * @return un objeto Retorno indicando el resultado de la operación
	 */
	Retorno registrarCiudad(String nombre, Double coordX, Double coordY);

	/**
	 * Registra un tambo en el sistema.
	 *
	 * @param nombre el nombre del tambo
	 * @param coordX la coordenada X del tambo
	 * @param coordY la coordenada Y del tambo
	 * @param cedula_productor la cédula del productor asociado al tambo
	 * @param capacidad la capacidad del tambo
	 * @return un objeto Retorno indicando el resultado de la operación
	 */
	Retorno registrarTambo(String nombre, Double coordX, Double coordY, String cedula_productor, int capacidad);

	/**
	 * Registra un centro de pasteurización en el sistema.
	 *
	 * @param nombre el nombre del centro
	 * @param coordX la coordenada X del centro
	 * @param coordY la coordenada Y del centro
	 * @param capacidad la capacidad del centro
	 * @return un objeto Retorno indicando el resultado de la operación
	 */
	Retorno registrarCentro(String nombre, Double coordX, Double coordY, int capacidad);

	/**
	 * Registra un tramo entre dos puntos en el sistema.
	 *
	 * @param coordXi la coordenada X inicial del tramo
	 * @param coordYi la coordenada Y inicial del tramo
	 * @param coordXf la coordenada X final del tramo
	 * @param coordYf la coordenada Y final del tramo
	 * @param peso el peso del tramo
	 * @return un objeto Retorno indicando el resultado de la operación
	 */
	Retorno registrarTramo(Double coordXi, Double coordYi, Double coordXf, Double coordYf, int peso);

	/**
	 * Elimina un tramo entre dos puntos en el sistema.
	 *
	 * @param coordXi la coordenada X inicial del tramo
	 * @param coordYi la coordenada Y inicial del tramo
	 * @param coordXf la coordenada X final del tramo
	 * @param coordYf la coordenada Y final del tramo
	 * @return un objeto Retorno indicando el resultado de la operación
	 */
	Retorno eliminarTramo(Double coordXi, Double coordYi, Double coordXf, Double coordYf);

	/**
	 * Elimina un punto del sistema.
	 *
	 * @param coordX la coordenada X del punto
	 * @param coordY la coordenada Y del punto
	 * @return un objeto Retorno indicando el resultado de la operación
	 */
	Retorno eliminarPunto(Double coordX, Double coordY);

	/**
	 * Genera un mapa del estado actual del sistema.
	 *
	 * @return un objeto Retorno indicando el resultado de la operación
	 */
	Retorno mapaEstado();

	/**
	 * Calcula la ruta al centro de pasteurización más cercano.
	 *
	 * @param coordX la coordenada X del punto de origen
	 * @param coordY la coordenada Y del punto de origen
	 * @return un objeto Retorno indicando el resultado de la operación
	 */
	Retorno rutaACentroMasCercano(Double coordX, Double coordY);

	/**
	 * Lista los tambos en una ciudad específica.
	 *
	 * @param coordX la coordenada X de la ciudad
	 * @param coordY la coordenada Y de la ciudad
	 * @return un objeto Retorno indicando el resultado de la operación
	 */
	Retorno listadoDeTambosEnCiudad(Double coordX, Double coordY);

	/**
	 * Lista todos los productores registrados en el sistema.
	 *
	 * @return un objeto Retorno indicando el resultado de la operación
	 */
	Retorno listadoProductores();

	/**
	 * Lista todos los centros de pasteurización registrados en el sistema.
	 *
	 * @return un objeto Retorno indicando el resultado de la operación
	 */
	Retorno listadoDeCentros();
}
