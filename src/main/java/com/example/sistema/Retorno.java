package com.example.sistema;

import com.example.sistema.ISistema.TipoError;

/**
 * Clase que encapsula el resultado de una operación del sistema.
 */
public class Retorno {

	private int valorEntero;
	private String valorString;
	private TipoError resultado;

	/**
	 * Constructor de la clase Retorno.
	 *
	 * @param valorEntero el valor entero retornado
	 * @param valorString el valor String retornado
	 * @param resultado el tipo de error o éxito de la operación
	 */
	public Retorno(int valorEntero, String valorString, TipoError resultado) {
		this.valorEntero = valorEntero;
		this.valorString = valorString;
		this.resultado = resultado;
	}

	/**
	 * Obtiene el valor entero retornado.
	 *
	 * @return el valor entero
	 */
	public int getValorEntero() {
		return valorEntero;
	}

	/**
	 * Establece el valor entero retornado.
	 *
	 * @param valorEntero el nuevo valor entero
	 */
	public void setValorEntero(int valorEntero) {
		this.valorEntero = valorEntero;
	}

	/**
	 * Obtiene el valor String retornado.
	 *
	 * @return el valor String
	 */
	public String getValorString() {
		return valorString;
	}

	/**
	 * Establece el valor String retornado.
	 *
	 * @param valorString el nuevo valor String
	 */
	public void setValorString(String valorString) {
		this.valorString = valorString;
	}

	/**
	 * Obtiene el tipo de error o éxito de la operación.
	 *
	 * @return el tipo de error o éxito
	 */
	public TipoError getResultado() {
		return resultado;
	}

	/**
	 * Establece el tipo de error o éxito de la operación.
	 *
	 * @param resultado el nuevo tipo de error o éxito
	 */
	public void setResultado(TipoError resultado) {
		this.resultado = resultado;
	}
}
