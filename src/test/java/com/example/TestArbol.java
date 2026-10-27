package com.example;

import com.example.dominio.Productor;
import com.example.estructuras.Arbol;
import com.example.estructuras.ProductorComparator;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestArbol {

    @Test
    public void testContainsProducer() {
        Arbol arbol = new Arbol(new ProductorComparator());

        assertFalse(arbol.contieneElemento(new Productor("3")));

        arbol.insertarElemento(new Productor("3"));
        arbol.insertarElemento(new Productor("1"));
        arbol.insertarElemento(new Productor("5"));

        assertTrue(arbol.contieneElemento(new Productor("3")));
        assertTrue(arbol.contieneElemento(new Productor("1")));
        assertTrue(arbol.contieneElemento(new Productor("5")));
        assertFalse(arbol.contieneElemento(new Productor("4")));
    }
}