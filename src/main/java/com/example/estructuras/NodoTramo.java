package com.example.estructuras;

/**
 * Clase que representa un tramo en el grafo, con coordenadas iniciales y finales y un peso asociado.
 */
public class NodoTramo {

    private int peso; // Peso del tramo
    private Double coordXI; // Coordenada X inicial
    private Double coordYI; // Coordenada Y inicial
    private Double coordXF; // Coordenada X final
    private Double coordYF; // Coordenada Y final

    // Constructor
    public NodoTramo(int peso, Double coordXI, Double coordYI, Double coordXF, Double coordYF) {
        this.peso = peso;
        this.coordXI = coordXI;
        this.coordYI = coordYI;
        this.coordXF = coordXF;
        this.coordYF = coordYF;
    }

    // Getters y setters
    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public Double getCoordXI() {
        return coordXI;
    }

    public void setCoordXI(Double coordXI) {
        this.coordXI = coordXI;
    }

    public Double getCoordYI() {
        return coordYI;
    }

    public void setCoordYI(Double coordYI) {
        this.coordYI = coordYI;
    }

    public Double getCoordXF() {
        return coordXF;
    }

    public void setCoordXF(Double coordXF) {
        this.coordXF = coordXF;
    }

    public Double getCoordYF() {
        return coordYF;
    }

    public void setCoordYF(Double coordYF) {
        this.coordYF = coordYF;
    }

    /**
     * Genera una cadena de texto para la URL del mapa que representa el trayecto.
     *
     * @return Cadena de texto para la URL del trayecto en el mapa.
     */
    public String stringMapaTrayecto() {
        return this.coordXI + "," + this.coordYI + "%7C" + this.coordXF + "," + this.coordYF;
    }
}
