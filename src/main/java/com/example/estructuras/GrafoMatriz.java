package com.example.estructuras;

public class GrafoMatriz implements IGrafo {

    NodoTramo[][] matrizAdyacencia; // Matriz de adyacencia para representar los tramos
    HashPuntos puntosMapa; // Mapa de puntos
    int cantPuntos; // Cantidad de puntos
    private int[] previo; // Arreglo para almacenar el camino previo
    public String miVar = ""; // Variable auxiliar para concatenar resultados

    @Override
    public HashPuntos getPuntosMapa() {
        return puntosMapa;
    }

    @Override
    public NodoTramo[][] getMatrizAdyacencia() {
        return matrizAdyacencia;
    }

    // Crea el grafo vacío (sin nodos ni aristas) con capacidad de almacenamiento de n vertices
    public GrafoMatriz(int cantPuntos) {
        int sigPrimo = siguientePrimo(cantPuntos + 1);
        this.matrizAdyacencia = new NodoTramo[sigPrimo][sigPrimo];
        this.puntosMapa = new HashPuntos(cantPuntos);
        this.cantPuntos = cantPuntos;
        this.previo = new int[cantPuntos];
    }

    // Verifica si un número es primo
    public static boolean esPrimo(int numero) {
        int contador = 2;
        boolean primo = true;
        while ((primo) && (contador != numero)) {
            if (numero % contador == 0) {
                primo = false;
            }
            contador++;
        }
        return primo;
    }

    // Encuentra el siguiente número primo
    public static int siguientePrimo(int cantPuntos) {
        if (esPrimo(cantPuntos)) {
            return cantPuntos;
        }
        return siguientePrimo(cantPuntos + 1);
    }

    @Override
    public void agregarTramo(NodoPuntoMapa nodoI, NodoPuntoMapa nodoF, int peso) {
        int I = puntosMapa.indicePuntoMapa(nodoI);
        int F = puntosMapa.indicePuntoMapa(nodoF);
        this.matrizAdyacencia[I][F] = new NodoTramo(peso, nodoI.getCoordX(), nodoI.getCoordY(),
                nodoF.getCoordX(), nodoF.getCoordY());
    }

    @Override
    public int caminoMasCorto(int origen, int destino) {
        int costo[] = new int[this.puntosMapa.getPuntos().length];
        int camino[] = new int[this.puntosMapa.getPuntos().length];
        boolean[] visitado = new boolean[this.puntosMapa.getPuntos().length];

        for (int i = 0; i < this.puntosMapa.getPuntos().length; i++) {
            if (i != origen) {
                if (sonAdyacentes(origen, i) || sonAdyacentes(i, origen)) { // Verifica si los nodos son adyacentes
                    if (sonAdyacentes(origen, i)) {
                        costo[i] = this.getMatrizAdyacencia()[origen][i].getPeso();
                        camino[i] = origen;
                    }
                    if (sonAdyacentes(i, origen)) {
                        costo[i] = this.getMatrizAdyacencia()[i][origen].getPeso();
                        camino[i] = origen;
                    }
                } else {
                    costo[i] = Integer.MAX_VALUE;
                }
            } else if (i == origen) {
                costo[i] = Integer.MAX_VALUE;
            }
        }
        for (int i = 1; i < this.puntosMapa.getPuntos().length; i++) {
            int w = distanciaMasCorta(visitado, costo);
            visitado[w] = true;
            for (int j = 1; j < this.puntosMapa.getPuntos().length; j++) {
                if ((this.getMatrizAdyacencia()[w][j] != null || this.getMatrizAdyacencia()[j][w] != null) && !visitado[j]) {
                    if (this.getMatrizAdyacencia()[w][j] != null) {
                        if (this.getMatrizAdyacencia()[w][j].getPeso() + costo[w] < costo[j]) {
                            costo[j] = this.getMatrizAdyacencia()[w][j].getPeso() + costo[w];
                            camino[j] = w;
                        }
                    }
                    if (this.getMatrizAdyacencia()[j][w] != null) {
                        if (this.getMatrizAdyacencia()[j][w].getPeso() + costo[w] < costo[j]) {
                            costo[j] = this.getMatrizAdyacencia()[j][w].getPeso() + costo[w];
                            camino[j] = w;
                        }
                    }
                }
            }
        }
        this.setPrevio(camino);
        return costo[destino];
    }

    @Override
    public int distanciaMasCorta(boolean[] visitado, int[] costo) {
        int i = 0;
        while (visitado[i] && i + 1 < visitado.length - 1) {
            i++;
        }
        int indiceMin = i;
        for (int j = i; j < visitado.length - 1; j++) {
            if (costo[j] < costo[indiceMin] && !visitado[j]) {
                indiceMin = j;
            }
        }
        return indiceMin;
    }

    @Override
    public boolean existeTramo(NodoPuntoMapa nodoI, NodoPuntoMapa nodoF) {
        int I = puntosMapa.indicePuntoMapa(nodoI);
        int F = puntosMapa.indicePuntoMapa(nodoF);
        return this.getMatrizAdyacencia()[I][F] != null || this.getMatrizAdyacencia()[F][I] != null;
    }

    @Override
    public void eliminarTramo(NodoPuntoMapa nodoI, NodoPuntoMapa nodoF) {
        int I = puntosMapa.indicePuntoMapa(nodoI);
        int F = puntosMapa.indicePuntoMapa(nodoF);
        this.matrizAdyacencia[I][F] = null;
    }

    @Override
    public void borrarPunto(NodoPuntoMapa nodo) {
        int indice = puntosMapa.funcionDisp(nodo.getCoordX(), nodo.getCoordY());
        int I = puntosMapa.indicePuntoMapa(nodo);
        for (int i = 0; i < cantPuntos; i++) {
            matrizAdyacencia[i][I] = null;
            matrizAdyacencia[I][i] = null;
        }
        puntosMapa.getPuntos()[indice] = null;
        puntosMapa.decrementarPuntosUsados();
    }

    @Override
    public boolean sonAdyacentes(int a, int b) {
        return this.getMatrizAdyacencia()[a][b] != null;
    }

    @Override
    public int[] getPrevio() {
        return previo;
    }

    public void cargarCamino(String valorx) {
        String aux = "";
        aux = miVar;
        miVar = valorx + aux;
    }

    @Override
    public String getMiValor() {
        return miVar;
    }

    @Override
    public void setMiValor(String pVal) {
        this.miVar = "";
    }

    @Override
    public String imprimirCamino(int[] previo, int origen, int destino) {
        String x = "";
        String resultado = "";
        if (previo[destino] != origen) {
            NodoPuntoMapa nodo = this.puntosMapa.getPuntos()[previo[destino]];
            resultado += nodo.listarCoordenadas() + "|";
            cargarCamino(resultado);
            imprimirCamino(previo, origen, previo[destino]);
            return x;
        }
        return x;
    }

    @Override
    public void setPrevio(int[] previo) {
        this.previo = previo;
    }
}
