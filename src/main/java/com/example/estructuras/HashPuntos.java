package com.example.estructuras;

public class HashPuntos {

    private int cantPuntos; // Cantidad de puntos permitidos
    private NodoPuntoMapa[] puntos; // Arreglo de puntos en el mapa
    private int puntosUsados; // Cantidad de puntos actualmente usados

    public HashPuntos(int pCantPuntos) {
        this.cantPuntos = pCantPuntos;
        this.puntos = new NodoPuntoMapa[GrafoMatriz.siguientePrimo(pCantPuntos+1)];
        this.puntosUsados = 0;
    }

    // Función de dispersión para determinar la posición en el hash
    public int funcionDisp(Double coordX, Double coordY) {
        int X = coordX.intValue();
        int Y = coordY.intValue();
        if (X < 0) {
            X = X * -1;
        }
        if (Y < 0) {
            Y = Y * -1;
        }
        return 10000*(X + Y) % cantPuntos;
    }

    // Verifica si quedan puntos disponibles
    public boolean quedanPuntos() {
        return puntosUsados < cantPuntos;
    }

    // Agrega un punto al hash
    public boolean agregarPunto(NodoPuntoMapa nodo) {
        int indice = funcionDisp(nodo.getCoordX(), nodo.getCoordY());
        if (getPuntos()[indice] != null) {
            if (getPuntos()[indice].getCoordX().equals(nodo.getCoordX()) && getPuntos()[indice].getCoordY().equals(nodo.getCoordY())) {
                return false;
            }
        }
        if (getPuntos()[indice] == null) {
            puntos[indice] = nodo;
            puntosUsados++;
            return true;
        } else {
            return encontrarLugarVacio(indice + 1, nodo);
        }
    }

    // Encuentra un lugar vacío en el hash
    public boolean encontrarLugarVacio(int indice, NodoPuntoMapa nodo) {
        if (indice < puntos.length) {
            if (getPuntos()[indice] == null) {
                puntos[indice] = nodo;
                puntosUsados++;
                return true;
            } else if (getPuntos()[indice].getCoordX().equals(nodo.getCoordX())
                    && getPuntos()[indice].getCoordY().equals(nodo.getCoordY())) {
                return false;
            } else {
                return encontrarLugarVacio((indice + 1) % cantPuntos, nodo);
            }
        }
        return false;
    }

    // Busca un punto en el hash
    public NodoPuntoMapa buscarPuntoMapa(NodoPuntoMapa nodo) {
        int indice = funcionDisp(nodo.getCoordX(), nodo.getCoordY());
        if (getPuntos()[indice] != null) {
            Double CoordXM = getPuntos()[indice].getCoordX();
            Double CoordYM = getPuntos()[indice].getCoordY();

            Double CoordXnodo = nodo.getCoordX();
            Double CoordYnodo = nodo.getCoordY();

            if (CoordXM.equals(CoordXnodo) && CoordYM.equals(CoordYnodo)) {
                return getPuntos()[indice];
            } else {
                return buscarPuntoContinuacion((indice + 1) % cantPuntos, nodo);
            }
        }
        return null;
    }

    // Continua buscando un punto en el hash
    public NodoPuntoMapa buscarPuntoContinuacion(int indice, NodoPuntoMapa nodo) {
        if (getPuntos()[indice] == null) {
            return null;
        } else {
            Double CoordXM = getPuntos()[indice].getCoordX();
            Double CoordYM = getPuntos()[indice].getCoordY();

            Double CoordXnodo = nodo.getCoordX();
            Double CoordYnodo = nodo.getCoordY();

            if (CoordXM.equals(CoordXnodo) && CoordYM.equals(CoordYnodo)) {
                return getPuntos()[indice];
            } else {
                return buscarPuntoContinuacion((indice + 1) % cantPuntos, nodo);
            }
        }
    }

    // Obtiene el índice de un punto en el hash
    public int indicePuntoMapa(NodoPuntoMapa nodo) {
        int i = funcionDisp(nodo.getCoordX(), nodo.getCoordY());
        for (i = 0; i < getPuntos().length - 1; i++) {
            if (getPuntos()[i] != null) {
                if (nodo.esIgual(getPuntos()[i])) {
                    return i;
                }
            }
        }
        return i;
    }

    public NodoPuntoMapa[] getPuntos() {
        return puntos;
    }

    public int getPuntosUsados() {
        return puntosUsados;
    }

    public void decrementarPuntosUsados() {
        puntosUsados--;
    }
}
