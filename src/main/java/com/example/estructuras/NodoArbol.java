package com.example.estructuras;

/**
 * Clase que representa un nodo en un árbol binario.
 */
public class NodoArbol {

    private Object dato; // Dato almacenado en el nodo
    private NodoArbol der; // Nodo derecho
    private NodoArbol izq; // Nodo izquierdo

    /**
     * Constructor que inicializa un nodo con un dato.
     *
     * @param pDato Dato a almacenar en el nodo.
     */
    public NodoArbol(Object pDato) {
        this.dato = pDato;
        this.izq = null;
        this.der = null;
    }

    /**
     * Constructor que inicializa un nodo con un dato y sus nodos hijos.
     *
     * @param pDato Dato a almacenar en el nodo.
     * @param pIzq Nodo izquierdo.
     * @param pDer Nodo derecho.
     */
    public NodoArbol(Object pDato, NodoArbol pIzq, NodoArbol pDer) {
        this.dato = pDato;
        this.izq = pIzq;
        this.der = pDer;
    }

    /**
     * Obtiene el dato almacenado en el nodo.
     *
     * @return Dato del nodo.
     */
    public Object getDato() {
        return dato;
    }

    /**
     * Establece el dato del nodo.
     *
     * @param dato Dato a establecer.
     */
    public void setDato(Object dato) {
        this.dato = dato;
    }

    /**
     * Obtiene el nodo derecho.
     *
     * @return Nodo derecho.
     */
    public NodoArbol getDer() {
        return der;
    }

    /**
     * Establece el nodo derecho.
     *
     * @param der Nodo derecho a establecer.
     */
    public void setDer(NodoArbol der) {
        this.der = der;
    }

    /**
     * Obtiene el nodo izquierdo.
     *
     * @return Nodo izquierdo.
     */
    public NodoArbol getIzq() {
        return izq;
    }

    /**
     * Establece el nodo izquierdo.
     *
     * @param izq Nodo izquierdo a establecer.
     */
    public void setIzq(NodoArbol izq) {
        this.izq = izq;
    }
}
