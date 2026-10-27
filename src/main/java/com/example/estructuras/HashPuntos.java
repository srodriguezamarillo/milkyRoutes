package com.example.estructuras;

public class HashPuntos {

    private int cantPuntos; // Cantidad de puntos permitidos
    private NodoPuntoMapa[] puntos; // Arreglo de puntos en el mapa
    private int puntosUsados; // Cantidad de puntos actualmente usados

    /**
     * Marker for a removed slot. A tombstone keeps the probe chain intact and
     * the remaining slot indexes stable (the adjacency matrix references them
     * directly), while lookups skip it and inserts may reuse it. Identity
     * comparison is used everywhere, so its coordinates are never read.
     */
    private static final NodoPuntoMapa ELIMINADO = new NodoPuntoMapa(null, null);

    /**
     * Indicates whether a point instance is the tombstone used to mark
     * deleted slots, so external traversals of the table can ignore them.
     */
    public static boolean esEliminado(NodoPuntoMapa nodo) {
        return nodo == ELIMINADO;
    }

    public HashPuntos(int pCantPuntos) {
        this.cantPuntos = pCantPuntos;
        this.puntos = new NodoPuntoMapa[GrafoMatriz.siguientePrimo(pCantPuntos+1)];
        this.puntosUsados = 0;
    }

    /**
     * Dispersion function that determines the position in the hash. The
     * coordinates are scaled before truncating, so decimals keep their weight
     * in the bucket and two points that differ only after the decimal point
     * no longer share a slot. Integer coordinates keep exactly the bucket of
     * the original formula.
     */
    public int funcionDisp(Double coordX, Double coordY) {
        int X = Math.abs((int) (coordX * 10000));
        int Y = Math.abs((int) (coordY * 10000));
        return (X + Y) % cantPuntos;
    }

    // Verifica si quedan puntos disponibles
    public boolean quedanPuntos() {
        return puntosUsados < cantPuntos;
    }

    /**
     * Adds a point to the hash. Fails when the point already exists (its
     * coordinates match a stored point anywhere in its probe chain), when the
     * allowed capacity is full, or when the table has no free slot. The
     * duplicate scan matters most when the bucket slot is an empty or deleted
     * place: inserting there directly would hide a same-coordinates point
     * living deeper in the same chain (e.g. stored before the deletion that
     * opened the tombstone).
     */
    public boolean agregarPunto(NodoPuntoMapa nodo) {
        if (puntosUsados >= cantPuntos) {
            return false;
        }
        int indice = funcionDisp(nodo.getCoordX(), nodo.getCoordY());
        return encontrarLugarVacio(indice, nodo);
    }

    /**
     * Finds a free slot for a point starting at the given index, probing
     * forward circularly across the whole table. Callers passing a dispersion
     * bucket get the regular insert semantics. The insert is self-contained:
     * it checks the allowed-points quota itself (a direct call cannot push
     * the count beyond it), rejects coordinates found anywhere in the probe
     * chain (even when the first slot is free or a tombstone), and only then
     * writes into the first empty or deleted slot. Both scans are bounded by
     * the table size, so a full table returns false instead of recursing
     * forever.
     */
    public boolean encontrarLugarVacio(int indice, NodoPuntoMapa nodo) {
        if (puntosUsados >= cantPuntos) {
            return false;
        }
        int actual = Math.floorMod(indice, puntos.length);
        // The chain scan stops at empty slots and skips tombstones, exactly
        // like a lookup: a duplicate behind the insertion place is detected
        // before any slot is reused.
        if (buscarPuntoContinuacion(actual, nodo) != null) {
            return false;
        }
        for (int paso = 0; paso < puntos.length; paso++) {
            if (puntos[actual] == null || puntos[actual] == ELIMINADO) {
                puntos[actual] = nodo;
                puntosUsados++;
                return true;
            }
            actual = (actual + 1) % puntos.length;
        }
        return false;
    }

    /**
     * Searches for a point with the same coordinates, starting from its
     * dispersion bucket. The actual probe is delegated to
     * {@link #buscarPuntoContinuacion}, so displaced points are found and the
     * scan is bounded.
     */
    public NodoPuntoMapa buscarPuntoMapa(NodoPuntoMapa nodo) {
        int indice = funcionDisp(nodo.getCoordX(), nodo.getCoordY());
        return buscarPuntoContinuacion(indice, nodo);
    }

    /**
     * Continues the search for a point from the given slot, probing forward
     * circularly across the whole table. An empty slot means the point is not
     * stored, while deleted slots are skipped so the rest of the chain stays
     * reachable. The scan is bounded by the table size, so a full table
     * without a match returns null instead of recursing forever.
     */
    public NodoPuntoMapa buscarPuntoContinuacion(int indice, NodoPuntoMapa nodo) {
        int actual = Math.floorMod(indice, puntos.length);
        for (int paso = 0; paso < puntos.length; paso++) {
            if (puntos[actual] == null) {
                return null;
            }
            if (puntos[actual] != ELIMINADO && puntos[actual].esIgual(nodo)) {
                return puntos[actual];
            }
            actual = (actual + 1) % puntos.length;
        }
        return null;
    }

    /**
     * Removes a point with the same coordinates from the table. The slot is
     * marked with a tombstone instead of being nulled: neighboring probe
     * chains stay traversable and every remaining point keeps its slot
     * index, which the adjacency matrix depends on. Later inserts may reuse
     * the tombstone slot.
     */
    public boolean quitarPunto(NodoPuntoMapa nodo) {
        int indice = indicePuntoMapa(nodo);
        if (indice == -1) {
            return false;
        }
        puntos[indice] = ELIMINADO;
        puntosUsados--;
        return true;
    }

    /**
     * Returns the table slot where a point with the same coordinates is
     * stored. Every slot is scanned and deleted markers are skipped, so
     * points lodging anywhere in the table are found. Returns -1 when the
     * point is not stored; callers can react to a miss instead of silently
     * reading or writing the last slot.
     */
    public int indicePuntoMapa(NodoPuntoMapa nodo) {
        for (int i = 0; i < puntos.length; i++) {
            if (puntos[i] != null && puntos[i] != ELIMINADO && nodo.esIgual(puntos[i])) {
                return i;
            }
        }
        return -1;
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
