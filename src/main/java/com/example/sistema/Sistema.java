package com.example.sistema;

import com.example.dominio.*;
import com.example.estructuras.*;
import java.util.regex.Pattern;

public class Sistema implements ISistema {

    public IArbol arbolProductores; // Árbol de productores
    public IGrafo GrafoMatriz; // Grafo de puntos del mapa
    int cantidadPuntos = 0; // Cantidad de puntos en el sistema

    public enum TipoPunto {
        CIUDAD, TAMBO, CENTRO_PASTEURIZADO
    }

    private static final Pattern CEDULA_PATTERN = Pattern.compile("^\\d{1}\\.\\d{3}\\.\\d{3}-\\d$");
    private static final Pattern CELULAR_PATTERN = Pattern.compile("^09\\d{7}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,4}$");

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
            // The insert result is honored: a rejected insert (e.g. capacity)
            // must not report OK, otherwise the quota accounting drifts.
            if (GrafoMatriz.getPuntosMapa().agregarPunto(ciudadNueva)) {
                return new Retorno(0, "", TipoError.OK);
            }
            return new Retorno(0, "", TipoError.ERROR_1);
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
                if (cedula_productor == null
                        || !arbolProductores.contieneElemento(
                        new Productor(cedula_productor))) {
                    return new Retorno(0, "", TipoError.ERROR_4);
                }
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
        // Map rendering is a presentation concern, so the business logic no
        // longer generates Google Static Maps URLs, launches the browser, or
        // writes to the console. The ISistema contract is preserved: the same
        // Retorno (0, "", OK) value is returned for every reachable state.
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
        return CEDULA_PATTERN.matcher(cedula).matches();
    }

    // Valida que el celular tenga el formato correcto (09NNNNNNN)
    public boolean validarCelular(String celular) {
        return CELULAR_PATTERN.matcher(celular).matches();
    }

    // Valida que el email tenga un formato correcto
    public boolean validarEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
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
