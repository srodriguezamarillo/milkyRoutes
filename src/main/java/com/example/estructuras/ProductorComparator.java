package com.example.estructuras;

import java.util.Comparator;
import com.example.dominio.Productor;

/**
 * Comparator para comparar objetos de tipo Productor basado en la cédula.
 */
public class ProductorComparator implements Comparator<Object> {

    /**
     * Compara dos objetos Productor por su cédula.
     *
     * @param o1 el primer objeto a comparar
     * @param o2 el segundo objeto a comparar
     * @return un valor negativo, cero o positivo si la cédula del primer objeto es menor, igual o mayor que la del segundo objeto
     */
    @Override
    public int compare(Object o1, Object o2) {
        return ((Productor) o1).getCedula().compareTo(((Productor) o2).getCedula());
    }
}
