package com.example.estructuras;

/**
 * Interfaz que define las operaciones para un árbol binario.
 */
public interface IArbol extends Iterable<Object> {

    /**
     * Verifica si el árbol está vacío.
     *
     * @return true si el árbol está vacío, false en caso contrario.
     */
    boolean esArbolVacio();

    /**
     * Obtiene la raíz del árbol.
     *
     * @return Nodo que representa la raíz del árbol.
     */
    NodoArbol getRaiz();

    /**
     * Muestra el contenido del árbol en orden.
     *
     * @return Cadena con el contenido del árbol en orden.
     */
    String mostrarInOrder();

    /**
     * Muestra el contenido del subárbol a partir de un nodo dado en orden.
     *
     * @param nodo Nodo a partir del cual se muestra el contenido en orden.
     * @return Cadena con el contenido del subárbol en orden.
     */
    String mostrarInOrder(NodoArbol nodo);

    /**
     * Inserta un elemento en el árbol.
     *
     * @param dato Objeto a insertar en el árbol.
     * @return true si el elemento se inserta con éxito, false en caso contrario.
     */
    boolean insertarElemento(Object dato);

    /**
     * Inserta un elemento en el árbol continuando desde un nodo dado.
     *
     * @param dato Objeto a insertar en el árbol.
     * @param nodo Nodo a partir del cual se inserta el elemento.
     * @return true si el elemento se inserta con éxito, false en caso contrario.
     */
    boolean insertarElementoContinuacion(Object dato, NodoArbol nodo);
}
