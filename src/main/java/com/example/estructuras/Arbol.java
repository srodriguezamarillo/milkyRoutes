package com.example.estructuras;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Clase que representa un árbol binario de búsqueda.
 * Permite insertar elementos y realizar recorridos en inorden.
 */
public class Arbol implements IArbol {

    private NodoArbol raiz;
    private Comparator<Object> comp;

    /**
     * Constructor del árbol que recibe un comparador.
     *
     * @param comp Comparador para los elementos del árbol.
     */
    public Arbol(Comparator<Object> comp) {
        this.comp = comp;
    }

    @Override
    public NodoArbol getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoArbol raiz) {
        this.raiz = raiz;
    }

    @Override
    public boolean esArbolVacio() {
        return raiz == null;
    }

    @Override
    public boolean insertarElemento(Object nuevo) {
        if (esArbolVacio()) {
            raiz = new NodoArbol(nuevo);
            return true;
        } else if (raiz.getDato().equals(nuevo)) {
            return false;
        } else {
            return insertarElementoContinuacion(nuevo, raiz);
        }
    }

    @Override
    public boolean insertarElementoContinuacion(Object nuevo, NodoArbol nodo) {
        if (nodo.getDato().equals(nuevo)) {
            return false;
        }

        if (comp.compare(nodo.getDato(), nuevo) > 0) {
            if (nodo.getIzq() == null) {
                nodo.setIzq(new NodoArbol(nuevo));
                return true;
            } else {
                return insertarElementoContinuacion(nuevo, nodo.getIzq());
            }
        } else {
            if (nodo.getDer() == null) {
                nodo.setDer(new NodoArbol(nuevo));
                return true;
            } else {
                return insertarElementoContinuacion(nuevo, nodo.getDer());
            }
        }
    }

    @Override
    public String mostrarInOrder() {
        if (esArbolVacio()) {
            return "";
        }
        return mostrarInOrder(raiz);
    }

    @Override
    public String mostrarInOrder(NodoArbol a) {
        StringBuilder resultado = new StringBuilder();
        if (a != null) {
            resultado.append(mostrarInOrder(a.getIzq()));
            resultado.append(a.getDato().toString());
            resultado.append(mostrarInOrder(a.getDer()));
        }
        return resultado.toString();
    }

    @Override
    public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            private NodoArbol aux = raiz;

            @Override
            public boolean hasNext() {
                return aux != null;
            }

            @Override
            public Object next() {
                Object actual = aux.getDato();
                aux = aux.getDer();
                return actual;
            }

            @Override
            public void remove() {
                // No se implementa el método remove
            }
        };
    }
}
