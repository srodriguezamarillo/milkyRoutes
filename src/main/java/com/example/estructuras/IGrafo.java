package com.example.estructuras;

/**
 * Interfaz que define las operaciones para un grafo con matriz de adyacencia.
 */
public interface IGrafo {

    /**
     * Obtiene el arreglo de predecesores.
     *
     * @return Arreglo de enteros que representa los predecesores.
     */
    int[] getPrevio();

    /**
     * Establece el arreglo de predecesores.
     *
     * @param previo Arreglo de enteros que representa los predecesores.
     */
    void setPrevio(int[] previo);

    /**
     * Obtiene el mapa de puntos del grafo.
     *
     * @return Objeto de tipo HashPuntos que representa el mapa de puntos.
     */
    HashPuntos getPuntosMapa();

    /**
     * Calcula el camino más corto entre dos nodos.
     *
     * @param origen Índice del nodo origen.
     * @param destino Índice del nodo destino.
     * @return El costo del camino más corto entre los nodos.
     */
    int caminoMasCorto(int origen, int destino);

    /**
     * Encuentra la distancia más corta no visitada.
     *
     * @param visitado Arreglo de booleanos que indica los nodos visitados.
     * @param costo Arreglo de enteros que indica el costo de los nodos.
     * @return El índice del nodo con la distancia más corta.
     */
    int distanciaMasCorta(boolean[] visitado, int[] costo);

    /**
     * Agrega un tramo entre dos nodos.
     *
     * @param nodoI Nodo inicial.
     * @param nodoF Nodo final.
     * @param peso Peso del tramo.
     */
    void agregarTramo(NodoPuntoMapa nodoI, NodoPuntoMapa nodoF, int peso);

    /**
     * Verifica si existe un tramo entre dos nodos.
     *
     * @param nodoI Nodo inicial.
     * @param nodoF Nodo final.
     * @return true si el tramo existe, false en caso contrario.
     */
    boolean existeTramo(NodoPuntoMapa nodoI, NodoPuntoMapa nodoF);

    /**
     * Elimina un tramo entre dos nodos.
     *
     * @param nodoI Nodo inicial.
     * @param nodoF Nodo final.
     */
    void eliminarTramo(NodoPuntoMapa nodoI, NodoPuntoMapa nodoF);

    /**
     * Borra un punto del grafo.
     *
     * @param nodo Nodo a borrar.
     */
    void borrarPunto(NodoPuntoMapa nodo);

    /**
     * Verifica si dos nodos son adyacentes.
     *
     * @param a Índice del primer nodo.
     * @param b Índice del segundo nodo.
     * @return true si los nodos son adyacentes, false en caso contrario.
     */
    boolean sonAdyacentes(int a, int b);

    /**
     * Imprime el camino entre dos nodos.
     *
     * @param previo Arreglo de predecesores.
     * @param origen Índice del nodo origen.
     * @param destino Índice del nodo destino.
     * @return Cadena que representa el camino.
     */
    String imprimirCamino(int[] previo, int origen, int destino);

    /**
     * Obtiene la matriz de adyacencia del grafo.
     *
     * @return Matriz de adyacencia del grafo.
     */
    NodoTramo[][] getMatrizAdyacencia();

    /**
     * Carga el camino en una variable.
     *
     * @param valorx Valor a cargar.
     */
    void cargarCamino(String valorx);

    /**
     * Obtiene el valor actual del camino cargado.
     *
     * @return Cadena que representa el valor del camino cargado.
     */
    String getMiValor();

    /**
     * Establece el valor del camino cargado.
     *
     * @param pVal Valor a establecer.
     */
    void setMiValor(String pVal);
}
