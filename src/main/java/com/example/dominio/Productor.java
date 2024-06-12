package com.example.dominio;

import java.util.Objects;

/**
 * Clase que representa un Productor.
 * Contiene información básica del productor como cédula, nombre, dirección, email y celular.
 */
public class Productor {

    private String cedula;
    private String nombre;
    private String direccion;
    private String email;
    private String celular;

    // Constructor completo
    public Productor(String cedula, String nombre, String direccion, String email, String celular) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.direccion = direccion;
        this.email = email;
        this.celular = celular;
    }

    // Constructor solo con cédula
    public Productor(String cedula) {
        this.cedula = cedula;
    }

    // Getters y setters
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    /**
     * Calcula el hashCode de un productor basado en su cédula.
     * @return el hashCode calculado
     */
    @Override
    public int hashCode() {
        return Objects.hash(cedula);
    }

    /**
     * Compara este productor con otro objeto para verificar si son iguales.
     * Dos productores se consideran iguales si tienen la misma cédula.
     * @param obj el objeto a comparar
     * @return true si son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Productor other = (Productor) obj;
        return Objects.equals(cedula, other.cedula);
    }

    /**
     * Devuelve una representación en String del productor en el formato "cedula;nombre;celular|".
     * @return la representación en String del productor
     */
    @Override
    public String toString() {
        return cedula + ";" + nombre + ";" + celular + '|';
    }
}
