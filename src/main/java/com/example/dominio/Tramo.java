package com.example.dominio;

/**
 * Clase que representa un Tramo.
 * Un tramo tiene coordenadas de inicio y fin, y un peso asociado.
 */
public class Tramo {

    private Double coordXi;
    private Double coordYi;
    private Double coordXf;
    private Double coordYf;
    private int peso;

    // Constructor completo
    public Tramo(Double coordXi, Double coordYi, Double coordXf, Double coordYf, int peso) {
        this.coordXi = coordXi;
        this.coordYi = coordYi;
        this.coordXf = coordXf;
        this.coordYf = coordYf;
        this.peso = peso;
    }

    // Constructor vacío
    public Tramo() {
    }

    // Getters y setters
    public Double getCoordXi() {
        return coordXi;
    }

    public void setCoordXi(Double coordXi) {
        this.coordXi = coordXi;
    }

    public Double getCoordYi() {
        return coordYi;
    }

    public void setCoordYi(Double coordYi) {
        this.coordYi = coordYi;
    }

    public Double getCoordXf() {
        return coordXf;
    }

    public void setCoordXf(Double coordXf) {
        this.coordXf = coordXf;
    }

    public Double getCoordYf() {
        return coordYf;
    }

    public void setCoordYf(Double coordYf) {
        this.coordYf = coordYf;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
}
