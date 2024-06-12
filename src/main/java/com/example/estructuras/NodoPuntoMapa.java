package com.example.estructuras;

import com.example.sistema.Sistema.TipoPunto;

/**
 * Clase que representa un punto en el mapa, que puede ser una ciudad, un tambo o un centro de pasteurización.
 */
public class NodoPuntoMapa {

    private TipoPunto tipoPunto; // Tipo de punto en el mapa
    private String nombre; // Nombre del punto
    private int capacidad; // Capacidad del tambo o centro
    private int capacidadRemanente; // Capacidad remanente del centro de pasteurización
    private String cedulaProductor; // Cédula del productor (para tambos)
    private Double coordX; // Coordenada X
    private Double coordY; // Coordenada Y

    // Constructor para una ciudad
    public NodoPuntoMapa(TipoPunto pTipoPunto, String pNombre, Double pCoordX, Double pCoordY) {
        this.tipoPunto = pTipoPunto;
        this.nombre = pNombre;
        this.coordX = pCoordX;
        this.coordY = pCoordY;
    }

    // Constructor para un tambo
    public NodoPuntoMapa(TipoPunto pTipoPunto, String pNombre, Double pCoordX, Double pCoordY, String pCedulaProductor, int pCapacidad) {
        this.tipoPunto = pTipoPunto;
        this.nombre = pNombre;
        this.coordX = pCoordX;
        this.coordY = pCoordY;
        this.cedulaProductor = pCedulaProductor;
        this.capacidad = pCapacidad;
    }

    // Constructor para un centro de pasteurización
    public NodoPuntoMapa(TipoPunto pTipoPunto, String pNombre, Double pCoordX, Double pCoordY, int pCapacidad) {
        this.tipoPunto = pTipoPunto;
        this.nombre = pNombre;
        this.coordX = pCoordX;
        this.coordY = pCoordY;
        this.capacidad = pCapacidad;
        this.capacidadRemanente = pCapacidad;
    }

    // Constructor para un nodo sin tipo específico
    public NodoPuntoMapa(Double pCoordX, Double pCoordY) {
        this.coordX = pCoordX;
        this.coordY = pCoordY;
    }

    // Getters y setters
    public Double getCoordX() {
        return coordX;
    }

    public void setCoordX(Double coordX) {
        this.coordX = coordX;
    }

    public Double getCoordY() {
        return coordY;
    }

    public void setCoordY(Double coordY) {
        this.coordY = coordY;
    }

    public TipoPunto getTipoPunto() {
        return tipoPunto;
    }

    public void setTipoPunto(TipoPunto tipoPunto) {
        this.tipoPunto = tipoPunto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getCapacidadRemanente() {
        return capacidadRemanente;
    }

    public void setCapacidadRemanente(int capacidadRemanente) {
        this.capacidadRemanente = capacidadRemanente;
    }

    public String getCedulaProductor() {
        return cedulaProductor;
    }

    public void setCedulaProductor(String cedulaProductor) {
        this.cedulaProductor = cedulaProductor;
    }

    /**
     * Verifica si este nodo es igual a otro nodo basado en las coordenadas.
     *
     * @param otro Nodo a comparar.
     * @return true si las coordenadas son iguales, false en caso contrario.
     */
    public boolean esIgual(NodoPuntoMapa otro) {
        return this.getCoordX().equals(otro.getCoordX()) && this.getCoordY().equals(otro.getCoordY());
    }

    /**
     * Genera una representación en cadena del centro de pasteurización.
     *
     * @return Cadena con las coordenadas, capacidad y capacidad remanente.
     */
    public String listarCentro() {
        return coordX + ";" + coordY + ";" + capacidad + ";" + capacidadRemanente + "|";
    }

    /**
     * Genera una representación en cadena del tambo.
     *
     * @return Cadena con las coordenadas del tambo.
     */
    public String listarTambo() {
        return coordX + ";" + coordY + "|";
    }

    /**
     * Genera una representación en cadena de las coordenadas del nodo.
     *
     * @return Cadena con las coordenadas del nodo.
     */
    public String listarCoordenadas() {
        return coordX + ";" + coordY;
    }

    /**
     * Genera una cadena de texto para la URL del mapa que representa el nodo.
     *
     * @return Cadena de texto para la URL del mapa.
     */
    public String stringMapa() {
        String x = "&markers=color:";

        switch (this.tipoPunto) {
            case CIUDAD:
                x += "red";
                break;
            case TAMBO:
                x += "yellow";
                break;
            case CENTRO_PASTEURIZADO:
                x += "green";
                break;
        }

        x += "%7Clabel:" + "" + "%7C";
        x += this.coordX + "," + this.coordY;

        return x;
    }
}
