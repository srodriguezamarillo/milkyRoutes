package com.example.sistema;

import com.example.dominio.*;
import com.example.estructuras.*;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class Sistema implements ISistema {

    public IArbol arbolProductores; // Árbol de productores
    public IGrafo GrafoMatriz; // Grafo de puntos del mapa
    int cantidadPuntos = 0; // Cantidad de puntos en el sistema

    public enum TipoPunto {
        CIUDAD, TAMBO, CENTRO_PASTEURIZADO
    }

    @Override
    public Retorno inicializarSistema(int cantPuntos) {
        if (cantPuntos <= 0) {
            return new Retorno(0, "", TipoError.ERROR_1);
        }
        GrafoMatriz = new GrafoMatriz(cantPuntos); // Inicializa el grafo con la cantidad de puntos especificada
        arbolProductores = new Arbol(new ProductorComparator()); // Inicializa el árbol de productores
        return new Retorno(0, "", TipoError.OK);
    }

    @Override
    public Retorno destruirSistema() {
        arbolProductores = null; // Destruye el árbol de productores
        GrafoMatriz = null; // Destruye el grafo
        cantidadPuntos = 0;
        return new Retorno(0, "", TipoError.OK);
    }

    @Override
    public Retorno registrarProductor(String cedula, String nombre, String direccion, String email, String celular) {
        Productor nuevoProductor = new Productor(cedula, nombre, direccion, email, celular);
        if (!validarCedula(cedula)) {
            return new Retorno(0, "", TipoError.ERROR_1);
        }
        if (!validarCelular(celular)) {
            return new Retorno(0, "", TipoError.ERROR_2);
        }
        if (!validarEmail(email)) {
            return new Retorno(0, "", TipoError.ERROR_3);
        }
        if (!arbolProductores.insertarElemento(nuevoProductor)) {
            return new Retorno(0, "", TipoError.ERROR_4);
        }
        return new Retorno(0, "", TipoError.OK);
    }

    @Override
    public Retorno registrarCiudad(String nombre, Double coordX, Double coordY) {
        if (!GrafoMatriz.getPuntosMapa().quedanPuntos()) {
            return new Retorno(0, "", TipoError.ERROR_1);
        }
        NodoPuntoMapa ciudadNueva = new NodoPuntoMapa(TipoPunto.CIUDAD, nombre, coordX, coordY);
        if (GrafoMatriz.getPuntosMapa().buscarPuntoMapa(ciudadNueva) == null) {
            GrafoMatriz.getPuntosMapa().agregarPunto(ciudadNueva);
            return new Retorno(0, "", TipoError.OK);
        }
        return new Retorno(0, "", TipoError.ERROR_2);
    }

    @Override
    public Retorno registrarTambo(String nombre, Double coordX, Double coordY, String cedula_productor, int capacidad) {
        if (!GrafoMatriz.getPuntosMapa().quedanPuntos()) {
            return new Retorno(0, "", TipoError.ERROR_1);
        }
        if (capacidad > 0) {
            NodoPuntoMapa tamboNuevo = new NodoPuntoMapa(TipoPunto.TAMBO, nombre, coordX, coordY, cedula_productor, capacidad);
            if (GrafoMatriz.getPuntosMapa().buscarPuntoMapa(tamboNuevo) == null) {
                if (GrafoMatriz.getPuntosMapa().agregarPunto(tamboNuevo)) {
                    return new Retorno(0, "", TipoError.OK);
                }
            }
            return new Retorno(0, "", TipoError.ERROR_3);
        }
        return new Retorno(0, "", TipoError.ERROR_2);
    }

    @Override
    public Retorno registrarCentro(String nombre, Double coordX, Double coordY, int capacidad) {
        if (!GrafoMatriz.getPuntosMapa().quedanPuntos()) {
            return new Retorno(0, "", TipoError.ERROR_1);
        }
        if (capacidad > 0) {
            NodoPuntoMapa centroNuevo = new NodoPuntoMapa(TipoPunto.CENTRO_PASTEURIZADO, nombre, coordX, coordY, capacidad);
            if (GrafoMatriz.getPuntosMapa().buscarPuntoMapa(centroNuevo) == null) {
                if (GrafoMatriz.getPuntosMapa().agregarPunto(centroNuevo)) {
                    return new Retorno(0, "", TipoError.OK);
                }
            }
            return new Retorno(0, "", TipoError.ERROR_3);
        }
        return new Retorno(0, "", TipoError.ERROR_2);
    }

    @Override
    public Retorno registrarTramo(Double coordXi, Double coordYi, Double coordXf, Double coordYf, int peso) {
        NodoPuntoMapa I = new NodoPuntoMapa(coordXi, coordYi);
        NodoPuntoMapa F = new NodoPuntoMapa(coordXf, coordYf);
        if (peso <= 0) {
            return new Retorno(0, "", TipoError.ERROR_1);
        }
        if (GrafoMatriz.getPuntosMapa().buscarPuntoMapa(I) == null || GrafoMatriz.getPuntosMapa().buscarPuntoMapa(F) == null) {
            return new Retorno(0, "", TipoError.ERROR_2);
        }
        if (GrafoMatriz.existeTramo(I, F)) {
            return new Retorno(0, "", TipoError.ERROR_3);
        }
        GrafoMatriz.agregarTramo(I, F, peso);
        return new Retorno(0, "", TipoError.OK);
    }

    @Override
    public Retorno eliminarTramo(Double coordXi, Double coordYi, Double coordXf, Double coordYf) {
        NodoPuntoMapa I = new NodoPuntoMapa(coordXi, coordYi);
        NodoPuntoMapa F = new NodoPuntoMapa(coordXf, coordYf);
        if (GrafoMatriz.getPuntosMapa().buscarPuntoMapa(I) == null || GrafoMatriz.getPuntosMapa().buscarPuntoMapa(F) == null) {
            return new Retorno(0, "", TipoError.ERROR_1);
        }
        if (GrafoMatriz.existeTramo(I, F)) {
            GrafoMatriz.eliminarTramo(I, F);
            return new Retorno(0, "", TipoError.OK);
        }
        return new Retorno(0, "", TipoError.ERROR_2);
    }

    @Override
    public Retorno eliminarPunto(Double coordX, Double coordY) {
        NodoPuntoMapa nodoPunto = new NodoPuntoMapa(coordX, coordY);
        if (GrafoMatriz.getPuntosMapa().buscarPuntoMapa(nodoPunto) != null) {
            GrafoMatriz.borrarPunto(nodoPunto);
            return new Retorno(0, "", TipoError.OK);
        }
        return new Retorno(0, "", TipoError.ERROR_1);
    }

    @Override
    public Retorno mapaEstado() {
        if (GrafoMatriz.getMatrizAdyacencia() != null || GrafoMatriz.getPuntosMapa() != null) {
            String URL = "http://maps.googleapis.com/maps/api/staticmap?size=1024x768&language=spanish";
            String strPuntos = "";
            String strTramos = "&path=color:0x0000ff%7Cweight:5";
            for (int i = 0; i < GrafoMatriz.getPuntosMapa().getPuntos().length; i++) {
                if (GrafoMatriz.getPuntosMapa().getPuntos()[i] != null) {
                    strPuntos += GrafoMatriz.getPuntosMapa().getPuntos()[i].stringMapa();
                }
            }
            for (int i = 0; i < GrafoMatriz.getMatrizAdyacencia().length; i++) {
                for (int j = 0; j < GrafoMatriz.getMatrizAdyacencia().length; j++) {
                    if (GrafoMatriz.getMatrizAdyacencia()[i][j] != null) {
                        strTramos += "%7C" + GrafoMatriz.getMatrizAdyacencia()[i][j].stringMapaTrayecto();
                    }
                }
            }
            URL += strPuntos + strTramos;
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(URI.create(URL));
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            } else {
                System.out.println("La acción BROWSE no es soportada en esta plataforma.");
            }
            return new Retorno(0, "", TipoError.OK);
        }
        return new Retorno(0, "", TipoError.OK);
    }

    @Override
    public Retorno rutaACentroMasCercano(Double coordX, Double coordY) {
        String listado = "";
        NodoPuntoMapa tambo = GrafoMatriz.getPuntosMapa().buscarPuntoMapa(new NodoPuntoMapa(coordX, coordY));
        NodoPuntoMapa centroDestino = null;
        int costo = Integer.MAX_VALUE;
        if (tambo != null) {
            for (NodoPuntoMapa centro : GrafoMatriz.getPuntosMapa().getPuntos()) {
                if (centro != null && centro.getTipoPunto() == TipoPunto.CENTRO_PASTEURIZADO) {
                    if (centro.getCapacidadRemanente() >= tambo.getCapacidad()) {
                        int I = GrafoMatriz.getPuntosMapa().indicePuntoMapa(tambo);
                        int F = GrafoMatriz.getPuntosMapa().indicePuntoMapa(centro);
                        int costoIF = GrafoMatriz.caminoMasCorto(I, F);
                        if (costoIF < costo) {
                            costo = costoIF;
                            GrafoMatriz.imprimirCamino(GrafoMatriz.getPrevio(), I, F);
                            listado = tambo.listarCoordenadas() + "|" + GrafoMatriz.getMiValor() + centro.listarCoordenadas();
                            GrafoMatriz.setMiValor("");
                            centroDestino = centro;
                        }
                    }
                }
            }
            if (centroDestino != null) {
                centroDestino.setCapacidadRemanente(centroDestino.getCapacidadRemanente() - tambo.getCapacidad());
                return new Retorno(0, listado, TipoError.OK);
            }
            return new Retorno(0, "", TipoError.ERROR_2);
        }
        return new Retorno(0, "", TipoError.ERROR_1);
    }

    @Override
    public Retorno listadoDeTambosEnCiudad(Double coordX, Double coordY) {
        String listado = "";
        NodoPuntoMapa ciudad = GrafoMatriz.getPuntosMapa().buscarPuntoMapa(new NodoPuntoMapa(coordX, coordY));
        if (ciudad != null) {
            for (NodoPuntoMapa tambo : GrafoMatriz.getPuntosMapa().getPuntos()) {
                if (tambo != null && tambo.getTipoPunto() == TipoPunto.TAMBO) {
                    int I = GrafoMatriz.getPuntosMapa().indicePuntoMapa(ciudad);
                    int F = GrafoMatriz.getPuntosMapa().indicePuntoMapa(tambo);
                    if ((GrafoMatriz.caminoMasCorto(I, F)) <= 20) {
                        listado += tambo.listarCoordenadas() + "|";
                    }
                }
            }
            return new Retorno(0, listado, TipoError.OK);
        }
        return new Retorno(0, "", TipoError.ERROR_1);
    }

    @Override
    public Retorno listadoProductores() {
        String listado = arbolProductores.mostrarInOrder(); // Muestra los productores en orden
        System.out.println(listado);
        return new Retorno(0, listado, TipoError.OK);
    }

    @Override
    public Retorno listadoDeCentros() {
        String listado = "";
        for (NodoPuntoMapa puntoMapa : GrafoMatriz.getPuntosMapa().getPuntos()) {
            if (puntoMapa != null && puntoMapa.getTipoPunto() == TipoPunto.CENTRO_PASTEURIZADO) {
                listado += puntoMapa.listarCentro();
            }
        }
        System.out.println(listado);
        return new Retorno(0, listado, TipoError.OK);
    }

    // Valida que la cédula tenga el formato correcto (N.NNN.NNN-N)
    public boolean validarCedula(String cedula) {
        int f = 1;
        if (cedula.length() != 11) {
            return false;
        }
        if (!cedula.substring(1, 2).equalsIgnoreCase(".")) {
            return false;
        }
        if (!cedula.substring(5, 6).equalsIgnoreCase(".")) {
            return false;
        }
        if (!cedula.substring(9, 10).equalsIgnoreCase("-")) {
            return false;
        }
        for (int i = 0; i < cedula.length(); i++) {
            if (i == 1 || i == 5 || i == 9) {
                i++;
                f++;
            }
            if (!tryParseInt(cedula.substring(i, f))) {
                return false;
            }
            f++;
        }
        return true;
    }

    // Valida que el celular tenga el formato correcto (09NNNNNNN)
    public boolean validarCelular(String celular) {
        if (celular.length() != 9) {
            return false;
        }
        if (!celular.substring(0, 1).equalsIgnoreCase("0") || !celular.substring(1, 2).equalsIgnoreCase("9")) {
            return false;
        }
        int i;
        int f = 3;
        for (i = 2; i < celular.length(); i++) {
            if (!tryParseInt(celular.substring(i, f))) {
                return false;
            }
            f++;
        }
        return true;
    }

    // Valida que el email tenga un formato correcto
    public boolean validarEmail(String email) {
        boolean puntoEsOk = false;
        boolean arrobaEsOk = false;
        int f = 1;
        for (int i = 0; i < email.length(); i++) {
            if (email.substring(i, f).equalsIgnoreCase("@")) {
                arrobaEsOk = true;
            }
            if (email.substring(i, f).equalsIgnoreCase(".")) {
                puntoEsOk = true;
            }
            f++;
        }
        return puntoEsOk && arrobaEsOk;
    }

    public boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
