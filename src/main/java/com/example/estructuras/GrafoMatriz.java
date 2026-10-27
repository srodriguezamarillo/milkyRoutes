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
        int n = this.puntosMapa.getPuntos().length;
        int costo[] = new int[n];
        int camino[] = new int[n];
        boolean[] visitado = new boolean[n];

        // Every vertex starts unreachable and without predecessor; only the
        // origin starts with cost zero so Dijkstra settles it first.
        for (int i = 0; i < n; i++) {
            costo[i] = Integer.MAX_VALUE;
            camino[i] = -1;
        }
        costo[origen] = 0;
        camino[origen] = origen;

        for (int i = 1; i < n; i++) {
            int w = distanciaMasCorta(visitado, costo);
            // Stop as soon as the cheapest pending vertex is still unreachable:
            // relaxing from Integer.MAX_VALUE would wrap around to negative costs.
            if (w < 0 || costo[w] == Integer.MAX_VALUE) {
                break;
            }
            visitado[w] = true;
            for (int j = 0; j < n; j++) {
                if (!visitado[j]) {
                    // Bidirectional behavior is preserved: an edge registered in
                    // either direction may be used to move between w and j.
                    if (this.getMatrizAdyacencia()[w][j] != null) {
                        // Candidate cost is computed in long so sentinel plus
                        // weight can never wrap around.
                        long alterna = (long) this.getMatrizAdyacencia()[w][j].getPeso() + costo[w];
                        if (alterna < costo[j]) {
                            costo[j] = (int) alterna;
                            camino[j] = w;
                        }
                    }
                    if (this.getMatrizAdyacencia()[j][w] != null) {
                        long alterna = (long) this.getMatrizAdyacencia()[j][w].getPeso() + costo[w];
                        if (alterna < costo[j]) {
                            costo[j] = (int) alterna;
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
        // Hash indices are sparse, so the cheapest unvisited vertex can sit on
        // any slot: the scan must cover the whole array, including 0 and
        // length - 1. Returns -1 once every vertex was visited.
        int indiceMin = -1;
        for (int j = 0; j < visitado.length; j++) {
            if (!visitado[j] && (indiceMin == -1 || costo[j] < costo[indiceMin])) {
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
        int I = puntosMapa.indicePuntoMapa(nodo);
        // Points are only removed from their actual table slot: nulling the
        // dispersion bucket instead would destroy another colliding point and
        // leave the victim searchable.
        if (I < 0) {
            return;
        }
        // The matrix side equals the hash table size, so every tramo tied to
        // the slot is cleared, including rows/columns beyond cantPuntos.
        for (int i = 0; i < matrizAdyacencia.length; i++) {
            matrizAdyacencia[i][I] = null;
            matrizAdyacencia[I][i] = null;
        }
        // The hash performs the removal; the slot becomes a tombstone so the
        // probe chain stays intact and every other point keeps its index.
        puntosMapa.quitarPunto(nodo);
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
        int previoDestino = previo[destino];
        // Only walk while the predecessor is usable: inside bounds, a real
        // point (not a deleted tombstone), and not the destination itself.
        // This keeps the walk total for absent or broken paths (e.g. stale
        // predecessors after deleting a point) instead of crashing or
        // printing placeholder coordinates.
        boolean predecesorValido = previoDestino >= 0 && previoDestino < previo.length
                && previoDestino != destino
                && this.puntosMapa.getPuntos()[previoDestino] != null
                && !HashPuntos.esEliminado(this.puntosMapa.getPuntos()[previoDestino]);
        if (predecesorValido && previoDestino != origen) {
            NodoPuntoMapa nodo = this.puntosMapa.getPuntos()[previoDestino];
            resultado += nodo.listarCoordenadas() + "|";
            cargarCamino(resultado);
            imprimirCamino(previo, origen, previoDestino);
            return x;
        }
        return x;
    }

    @Override
    public void setPrevio(int[] previo) {
        this.previo = previo;
    }
}
