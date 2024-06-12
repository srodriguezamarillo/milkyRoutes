package com.example;

import static org.junit.Assert.*;

import org.junit.Test;

import com.example.sistema.ISistema;
import com.example.sistema.Retorno;
import com.example.sistema.Sistema;

public class TestSistema {

	@Test
	public void testInicializarDestruirSistema() {
		Sistema s = new Sistema();
		s.inicializarSistema(1);
		s.destruirSistema();
		s.inicializarSistema(1);
	}

	// Tests Productores

	@Test
	public void testRegistrarProductor() {
		Sistema s = new Sistema();
		s.inicializarSistema(1);
		String ciProductor = "3.702.515-7";

		assertEquals(ISistema.TipoError.OK,
				s.registrarProductor(ciProductor, "Omar Alcides", "60 metros", "o@gmail.com", "098206900").getResultado());
	}

	@Test
	public void testRegistrarProductorDuplicado() {
		Sistema s = new Sistema();
		s.inicializarSistema(1);
		String ciProductor = "3.702.515-7";

		Retorno res = s.registrarProductor(ciProductor, "Omar Alcides", "60 metros", "o@gmail.com", "098206900");
		assertEquals(ISistema.TipoError.OK, res.getResultado());

		res = s.registrarProductor(ciProductor, "Omar Alcides", "60 metros", "o@gmail.com", "098206900");
		assertEquals(ISistema.TipoError.ERROR_4, res.getResultado());
	}

	@Test
	public void testRegistrarProductorChequeoFormatos() {
		Sistema s = new Sistema();
		s.inicializarSistema(1);

		// Formatos de Cedula incorrectos
		assertEquals(ISistema.TipoError.ERROR_1,
				s.registrarProductor("702.515-7", "", "", "o@g.com", "098206900").getResultado());
		assertEquals(ISistema.TipoError.ERROR_1,
				s.registrarProductor("1702515-7", "", "", "o@g.com", "098206900").getResultado());
		assertEquals(ISistema.TipoError.ERROR_1,
				s.registrarProductor("1.702.51-70", "", "", "o@g.com", "098206900").getResultado());
		assertEquals(ISistema.TipoError.ERROR_1,
				s.registrarProductor("1.702.51--0", "", "", "o@g.com", "098206900").getResultado());
		assertEquals(ISistema.TipoError.ERROR_1,
				s.registrarProductor("..702.510-0", "", "", "o@g.com", "098206900").getResultado());
		assertEquals(ISistema.TipoError.ERROR_1,
				s.registrarProductor("1.zzz.051-7", "", "", "o@g.com", "098206900").getResultado());
		assertEquals(ISistema.TipoError.ERROR_1,
				s.registrarProductor("           ", "", "", "o@g.com", "098206900").getResultado());
		// Formatos de mail incorrectos
		assertEquals(ISistema.TipoError.ERROR_3,
				s.registrarProductor("1.702.517-0", "", "", "", "098206900").getResultado());
		assertEquals(ISistema.TipoError.ERROR_3,
				s.registrarProductor("1.702.517-0", "", "", "estonoesunmail.com", "098206900").getResultado());
		// Formatos de celular incorrectos
		assertEquals(ISistema.TipoError.ERROR_2,
				s.registrarProductor("1.702.517-0", "", "", "a@mail.com", "123206900").getResultado());
		assertEquals(ISistema.TipoError.ERROR_2,
				s.registrarProductor("1.702.517-0", "", "", "a@mail.com", "000692000").getResultado());
		assertEquals(ISistema.TipoError.ERROR_2,
				s.registrarProductor("1.702.517-0", "", "", "a@mail.com", "099692abc").getResultado());
	}

	@Test
	public void testListadoProductorOK() {
		Sistema s = new Sistema();
		s.inicializarSistema(1);
		String ciProductor1 = "3.702.515-7";
		String ciProductor2 = "3.702.515-9";
		String ciProductor3 = "3.702.515-8";
		String ciProductor1FormatoNum = "37025157";
		String ciProductor2FormatoNum = "37025159";
		String ciProductor3FormatoNum = "37025158";
		String nom1 = "Omar";
		String nom2 = "Annabella";
		String nom3 = "Neca";

		assertEquals(ISistema.TipoError.OK,
				s.registrarProductor(ciProductor1, nom1, "60 metros", "o@gmail.com", "098206900").getResultado());
		assertEquals(ISistema.TipoError.OK,
				s.registrarProductor(ciProductor2, nom2, "60 metros", "o@gmail.com", "098206900").getResultado());
		assertEquals(ISistema.TipoError.OK,
				s.registrarProductor(ciProductor3, nom3, "60 metros", "o@gmail.com", "098206900").getResultado());

		Retorno res = s.listadoProductores();
		assertEquals(ISistema.TipoError.OK, res.getResultado());
		assertTrue(res.getValorString().contains(ciProductor1) || res.getValorString().contains(ciProductor1FormatoNum));
		assertTrue(res.getValorString().contains(nom1));
		assertTrue(res.getValorString().contains(ciProductor2) || res.getValorString().contains(ciProductor2FormatoNum));
		assertTrue(res.getValorString().contains(ciProductor3) || res.getValorString().contains(ciProductor3FormatoNum));
	}

	@Test
	public void testListadoProductoresOrdenados() {
		Sistema s = new Sistema();
		s.inicializarSistema(1);
		String ciProductor2 = "3.702.515-7";
		String ciProductor4 = "3.702.515-9";
		String ciProductor3 = "3.702.515-8";
		String ciProductor1 = "1.111.111-1";
		String ciProductor6 = "9.999.999-9";
		String ciProductor5 = "5.555.555-5";
		String ciProductor1FormatoNum = "11111111";
		String ciProductor2FormatoNum = "37025157";
		String ciProductor3FormatoNum = "37025158";
		String ciProductor4FormatoNum = "37025159";
		String ciProductor5FormatoNum = "55555555";
		String ciProductor6FormatoNum = "99999999";
		String nom1 = "Omar";
		String nom2 = "Annabella";
		String nom3 = "Neca";

		assertEquals(ISistema.TipoError.OK,
				s.registrarProductor(ciProductor4, nom1, "60 metros", "o@gmail.com", "098206900").getResultado());
		assertEquals(ISistema.TipoError.OK,
				s.registrarProductor(ciProductor5, nom2, "60 metros", "o@gmail.com", "098206900").getResultado());
		assertEquals(ISistema.TipoError.OK,
				s.registrarProductor(ciProductor1, nom3, "60 metros", "o@gmail.com", "098206900").getResultado());
		assertEquals(ISistema.TipoError.OK,
				s.registrarProductor(ciProductor3, nom1, "60 metros", "o@gmail.com", "098206900").getResultado());
		assertEquals(ISistema.TipoError.OK,
				s.registrarProductor(ciProductor6, nom1, "60 metros", "o@gmail.com", "098206900").getResultado());
		assertEquals(ISistema.TipoError.OK,
				s.registrarProductor(ciProductor2, nom1, "60 metros", "o@gmail.com", "098206900").getResultado());

		Retorno res = s.listadoProductores();
		assertEquals(ISistema.TipoError.OK, res.getResultado());
		assertEquals(6, res.getValorString().split("\\|").length);
		assertTrue(res.getValorString().contains(ciProductor1) || res.getValorString().contains(ciProductor1FormatoNum));
		assertTrue(res.getValorString().contains(ciProductor2) || res.getValorString().contains(ciProductor2FormatoNum));
		assertTrue(res.getValorString().contains(ciProductor3) || res.getValorString().contains(ciProductor3FormatoNum));
		assertTrue(res.getValorString().contains(ciProductor4) || res.getValorString().contains(ciProductor4FormatoNum));
		assertTrue(res.getValorString().contains(ciProductor5) || res.getValorString().contains(ciProductor5FormatoNum));
		assertTrue(res.getValorString().contains(ciProductor6) || res.getValorString().contains(ciProductor6FormatoNum));
		assertTrue((res.getValorString().indexOf(ciProductor1) < res.getValorString().indexOf(ciProductor2)
				&& res.getValorString().indexOf(ciProductor2) < res.getValorString().indexOf(ciProductor3)
				&& res.getValorString().indexOf(ciProductor3) < res.getValorString().indexOf(ciProductor4)
				&& res.getValorString().indexOf(ciProductor4) < res.getValorString().indexOf(ciProductor5)
				&& res.getValorString().indexOf(ciProductor5) < res.getValorString().indexOf(ciProductor6))
				||
				(res.getValorString().indexOf(ciProductor1FormatoNum) < res.getValorString().indexOf(ciProductor2FormatoNum)
						&& res.getValorString().indexOf(ciProductor2FormatoNum) < res.getValorString().indexOf(ciProductor3FormatoNum)
						&& res.getValorString().indexOf(ciProductor3FormatoNum) < res.getValorString().indexOf(ciProductor4FormatoNum)
						&& res.getValorString().indexOf(ciProductor4FormatoNum) < res.getValorString().indexOf(ciProductor5FormatoNum)
						&& res.getValorString().indexOf(ciProductor5FormatoNum) < res.getValorString().indexOf(ciProductor6FormatoNum)));
	}

	// TEST CIUDADES
	@Test
	public void testRegistrarCiudad() {
		Sistema s = new Sistema();
		s.inicializarSistema(1);
		assertEquals(ISistema.TipoError.OK, s.registrarCiudad("Paysandu", -32.3105104,-58.0759192).getResultado());
	}

	@Test
	public void testRegistrarCiudadDuplicada() {
		Sistema s = new Sistema();
		s.inicializarSistema(2);
		assertEquals(ISistema.TipoError.OK, s.registrarCiudad("Paysandu", -32.3105104,-58.0759192).getResultado());
		assertEquals(ISistema.TipoError.ERROR_2, s.registrarCiudad("Salto", -32.3105104,-58.0759192).getResultado());
	}

	@Test
	public void testRegistrarCiudadErrorGrafoCompleto() {
		Sistema s = new Sistema();
		s.inicializarSistema(1);
		assertEquals(ISistema.TipoError.OK, s.registrarCiudad("Paysandu", -32.3105104,-58.0759192).getResultado());
		assertEquals(ISistema.TipoError.ERROR_1, s.registrarCiudad("Salto", -32.00,-58.00).getResultado());
	}

	@Test
	public void testMapaEstadoSoloCiudades() {
		Sistema s = new Sistema();
		s.inicializarSistema(10);
		cargarCiudades(s);
		assertEquals(ISistema.TipoError.OK, s.mapaEstado().getResultado());
	}

	// PRUEBAS DE registro de Tambos y CENTROS
	@Test
	public void testRegistrarTamboOK() {
		Sistema s = new Sistema();
		s.inicializarSistema(10);
		String ciProductor = "3.702.515-7";
		s.registrarCiudad("Paysandu", -32.3105104,-58.0759192);
		s.registrarProductor(ciProductor, "Omar", "60 metros", "o@gmail.com", "098206900");
		assertEquals(ISistema.TipoError.OK, s.registrarTambo("Apirario 1", -32.3105100,-58.0759192, ciProductor, 10).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarTambo("Apirario 2", -32.3105100,-58.0759100, ciProductor, 10).getResultado());
	}

	@Test
	public void testRegistrarTamboERROR1() {
		Sistema s = new Sistema();
		s.inicializarSistema(1);
		String ciProductor = "3.702.515-7";
		s.registrarCiudad("Paysandu", -32.3105104,-58.0759192);
		s.registrarProductor(ciProductor, "Omar", "60 metros", "o@gmail.com", "098206900");
		assertEquals(ISistema.TipoError.ERROR_1, s.registrarTambo("Apirario 3", -32.3105100,-58.0759192, ciProductor, 10).getResultado());
	}

	@Test
	public void testRegistrarTamboERROR2() {
		Sistema s = new Sistema();
		s.inicializarSistema(2);
		String ciProductor = "3.702.515-7";
		s.registrarProductor(ciProductor, "Omar", "60 metros", "o@gmail.com", "098206900");
		assertEquals(ISistema.TipoError.ERROR_2, s.registrarTambo("Apirario 3", -32.3105100,-58.0759192, ciProductor, -1).getResultado());
		assertEquals(ISistema.TipoError.ERROR_2, s.registrarTambo("Apirario 3", -32.3105100,-58.0759192, ciProductor, 0).getResultado());
	}

	@Test
	public void testRegistrarTamboERROR3() {
		Sistema s = new Sistema();
		s.inicializarSistema(2);
		String ciProductor = "3.702.515-7";
		s.registrarCiudad("Paysandu", -32.3105104,-58.0759192);
		s.registrarProductor(ciProductor, "Omar", "60 metros", "o@gmail.com", "098206900");
		assertEquals(ISistema.TipoError.ERROR_3, s.registrarTambo("Apirario 3", -32.3105104,-58.0759192, ciProductor, 10).getResultado());
	}

	@Test
	public void testRegistrarTamboERROR4() {
		Sistema s = new Sistema();
		s.inicializarSistema(2);
		assertEquals(ISistema.TipoError.ERROR_4, s.registrarTambo("Apirario 1", -32.3105100,-58.0759192, "0.000.000-0", 10).getResultado());
	}

	@Test
	public void testRegistrarCentroOK() {
		Sistema s = new Sistema();
		s.inicializarSistema(10);
		s.registrarCiudad("Paysandu", -32.3105104,-58.0759192);
		assertEquals(ISistema.TipoError.OK, s.registrarCentro("Centro 1", -32.3105100,-58.0759192, 10).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarCentro("Centro 2", -32.3105100,-58.0759100, 10).getResultado());
	}

	@Test
	public void testRegistrarCentroError1() {
		Sistema s = new Sistema();
		s.inicializarSistema(1);
		s.registrarCiudad("Paysandu", -32.3105104,-58.0759192);
		assertEquals(ISistema.TipoError.ERROR_1, s.registrarCentro("Centro 1", -32.3105101,-58.0759192, 10).getResultado());
	}

	@Test
	public void testRegistrarCentroError2() {
		Sistema s = new Sistema();
		s.inicializarSistema(2);
		assertEquals(ISistema.TipoError.ERROR_2, s.registrarCentro("Centro 1", -32.3105104,-58.0759192, -1).getResultado());
		assertEquals(ISistema.TipoError.ERROR_2, s.registrarCentro("Centro 1", -32.3105104,-58.0759192, 0).getResultado());
	}

	@Test
	public void testRegistrarCentroError3() {
		Sistema s = new Sistema();
		s.inicializarSistema(2);
		s.registrarCiudad("Paysandu", -32.3105104,-58.0759192);
		assertEquals(ISistema.TipoError.ERROR_3, s.registrarCentro("Centro 1", -32.3105104,-58.0759192, 10).getResultado());
	}

	@Test
	public void testRegistrarTramoOK() {
		Sistema s = new Sistema();
		s.inicializarSistema(10);
		s.registrarCiudad("Paysandu", -32.3105104,-58.0759192);
		s.registrarCentro("Centro 1", -32.3105100,-58.0759192, 10);
		s.registrarCentro("Centro 2", -32.3105100,-58.0759100, 10);

		assertEquals(ISistema.TipoError.OK, s.registrarTramo(-32.3105104,-58.0759192, -32.3105100,-58.0759192, 10).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarTramo(-32.3105104,-58.0759192, -32.3105100,-58.0759100, 5).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarTramo(-32.3105100,-58.0759192, -32.3105100,-58.0759192, 3).getResultado());
	}

	@Test
	public void testRegistrarTramoError1() {
		Sistema s = new Sistema();
		s.inicializarSistema(10);
		s.registrarCiudad("Paysandu", -32.3105104,-58.0759192);
		s.registrarCentro("Centro 1", -32.3105100,-58.0759192, 10);
		s.registrarCentro("Centro 2", -32.3105100,-58.0759100, 10);
		assertEquals(ISistema.TipoError.ERROR_1, s.registrarTramo(-32.3105104,-58.0759192, -32.3105100,-58.0759192, -1).getResultado());
	}

	@Test
	public void testRegistrarTramoError2() {
		Sistema s = new Sistema();
		s.inicializarSistema(10);
		s.registrarCiudad("Paysandu", -32.3105104,-58.0759192);
		s.registrarCentro("Centro 1", -32.3105100,-58.0759192, 10);
		s.registrarCentro("Centro 2", -32.3105100,-58.0759100, 10);
		assertEquals(ISistema.TipoError.ERROR_2, s.registrarTramo(-32.99999,-58.0759192, -32.3105100,-58.0759192, 10).getResultado());
		assertEquals(ISistema.TipoError.ERROR_2, s.registrarTramo(-32.3105100,-58.0759100, -32.999999,-58.0759192, 10).getResultado());
	}

	@Test
	public void testRegistrarTramoError3() {
		Sistema s = new Sistema();
		s.inicializarSistema(10);
		s.registrarCiudad("Paysandu", -32.3105104,-58.0759192);
		s.registrarCentro("Centro 1", -32.3105100,-58.0759192, 10);
		s.registrarCentro("Centro 2", -32.3105100,-58.0759100, 10);

		assertEquals(ISistema.TipoError.OK, s.registrarTramo(-32.3105104,-58.0759192, -32.3105100,-58.0759192, 10).getResultado());
		assertEquals(ISistema.TipoError.ERROR_3, s.registrarTramo(-32.3105104,-58.0759192, -32.3105100,-58.0759192, 10).getResultado());
		assertEquals(ISistema.TipoError.ERROR_3, s.registrarTramo(-32.3105100,-58.0759192, -32.3105104,-58.0759192, 10).getResultado());
	}

	@Test
	public void testListadoDeTambosEnCiudad() {
		// Inicializacion
		Sistema s = new Sistema();
		s.inicializarSistema(10);
		String ci = "1.234.567-8";
		String api1 = "api1";
		String api2 = "apI2";
		String api3 = "api3";
		String api4 = "api4";
		String api5 = "api5";
		double coordX1 = -32.3105100;
		double coordX2 = -58.0759192;
		double coordX3 = -32.3105133;
		double coordX4 = -32.3105144;
		double coordX5 = -32.3105155;
		double coordX6 = -32.3105666;
		double coordY1 = -58.0759100;
		double coordY2 = -32.3102222;
		double coordY3 = -58.0759222;
		double coordY4 = -58.0759555;
		double coordY5 = -58.0759666;
		double coordY6 = -58.0759777;
		int cap1 = 10;

		// Esperados
		String NOesperado1 = coordX1 + ";" + coordY1;
		String esperado2 = coordX2 + ";" + coordY2;
		String NOesperado3 = coordX3 + ";" + coordY3;
		String esperado4 = coordX4 + ";" + coordY4;
		String esperado5 = coordX5 + ";" + coordY5;
		String NOesperado6 = coordX6 + ";" + coordY6;

		s.registrarCiudad("Ciudad", -32.3105099, -58.0759190);
		s.registrarProductor(ci, "Omar", "60 metros", "o@gmail.com", "098206900");

		assertEquals(ISistema.TipoError.OK, s.registrarTambo(api1, coordX1, coordY1, ci, cap1).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarTambo(api2, coordX2, coordY2, ci, cap1).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarTambo(api3, coordX3, coordY3, ci, cap1).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarTambo(api4, coordX4, coordY4, ci, cap1).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarTambo(api5, coordX5, coordY5, ci, cap1).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarCentro("Centro1", coordX6, coordY6, cap1).getResultado());

		assertEquals(ISistema.TipoError.OK, s.registrarTramo(-32.3105099, -58.0759190, coordX6, coordY6, 10).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarTramo(-32.3105099, -58.0759190, coordX1, coordY1, 25).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarTramo(coordX6, coordY6, coordX2, coordY2, 7).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarTramo(coordX2, coordY2, coordX3, coordY3, 8).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarTramo(-32.3105099, -58.0759190, coordX4, coordY4, 15).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarTramo(coordX4, coordY4, coordX5, coordY5, 3).getResultado());

		// Estimulo
		Retorno res = s.listadoDeTambosEnCiudad(-32.3105099, -58.0759190);

		// Validacion
		assertEquals(ISistema.TipoError.OK, res.getResultado());
		String r = res.getValorString().toLowerCase().replaceAll(" ", "");
		String[] centros = r.split("\\|");
		assertEquals(3, centros.length);
		assertTrue(!r.contains(NOesperado1));
		assertTrue(r.contains(esperado2));
		assertTrue(!r.contains(NOesperado3));
		assertTrue(r.contains(esperado4));
		assertTrue(r.contains(esperado5));
		assertTrue(!r.contains(NOesperado6));
	}

	@Test
	public void testListadoDeCentros() {

		// Inicializacion
		Sistema s = new Sistema();
		s.inicializarSistema(10);
		String centro1 = "centro1";
		String centro2 = "centro2";
		String centro3 = "centro3";
		double coordX1 = -32.3105100;
		double coordX2 = -58.0759192;
		double coordX3 = -32.3105100;
		double coordY1 = -58.0759100;
		double coordY2 = -32.3102222;
		double coordY3 = -58.0759222;
		int cap1 = 10;
		int cap2 = 15;
		int cap3 = 2;

		// Esperados
		String esperado1 = coordX1 + ";" + coordY1 + ";" + cap1 + ";" + cap1;
		String esperado2 = coordX2 + ";" + coordY2 + ";" + cap2 + ";" + cap2;
		String esperado3 = coordX3 + ";" + coordY3 + ";" + cap3 + ";" + cap3;

		// Estimulos
		s.registrarCentro(centro1, coordX1, coordY1, cap1);
		s.registrarCentro(centro2, coordX2, coordY2, cap2);
		s.registrarCentro(centro3, coordX3, coordY3, cap3);

		// Validacion
		Retorno res = s.listadoDeCentros();
		assertEquals(ISistema.TipoError.OK, res.getResultado());
		String r = res.getValorString().toLowerCase().replaceAll(" ", "");
		String[] centros = r.split("\\|");
		assertEquals(3, centros.length);
		assertTrue(r.contains(esperado1));
		assertTrue(r.contains(esperado2));
		assertTrue(r.contains(esperado3));
	}

	@Test
	public void testRutaACentroMasCercano() {
		// Inicializacion
		Sistema s = new Sistema();
		s.inicializarSistema(20);
		String ci = "1.234.567-8";
		s.registrarProductor(ci, "Omar", "", "email@gmail.com", "098123456");

		double xApi1 = -31.3689985, yApi1 = -57.9119238;
		double xApi2 = -32.3105104, yApi2 = -58.0759192;
		double xYoung = -32.698367, yYoung = -57.6356507;
		double xMercedes = -33.2563781, yMercedes = -58.0360079;
		double xCEFrayB = -33.1291165, yCEFrayB = -58.2985057;
		double xCETrinidad = -33.5198572, yCETrinidad = -56.8987083;
		double xCEDurazno = -33.3851666, yCEDurazno = -56.5568255;
		double xCEDolores = -33.5351509, yCEDolores = -58.2167245;

		s.registrarTambo("Tambo Salto", xApi1, yApi1, ci, 80);
		s.registrarTambo("Tambo Paysandu", xApi2, yApi2, ci, 80);

		s.registrarCiudad("Young", xYoung, yYoung);
		s.registrarCiudad("Mercedes", xMercedes, yMercedes);

		s.registrarCentro("Centro Past. Fray Bentos", xCEFrayB, yCEFrayB, 20);
		s.registrarCentro("Centro Past. Trinidad",  xCETrinidad, yCETrinidad, 20);
		s.registrarCentro("Centro Past. Durazno", xCEDurazno, yCEDurazno, 100);
		s.registrarCentro("Centro Past. Dolores", xCEDolores, yCEDolores, 100);

		s.registrarTramo(xApi1, yApi1, xApi2, yApi2, 120);
		s.registrarTramo(xYoung, yYoung, xApi2, yApi2, 170);
		s.registrarTramo(xMercedes, yMercedes, xApi2, yApi2, 115);

		s.registrarTramo(xYoung, yYoung, xCEDurazno, yCEDurazno, 170);
		s.registrarTramo(xYoung, yYoung, xCETrinidad, yCETrinidad, 160);
		s.registrarTramo(xCETrinidad, yCETrinidad, xCEDurazno, yCEDurazno, 5);

		s.registrarTramo(xMercedes, yMercedes, xCEFrayB, yCEFrayB, 50);
		s.registrarTramo(xMercedes, yMercedes, xCEDolores, yCEDolores, 90);
		s.registrarTramo(xCEFrayB, yCEFrayB, xCEDolores, yCEDolores, 30);

		s.mapaEstado();

		// Api1-Api2-Mercedes-CEFrayBentos-CEDolores
		String rutaEsperada = xApi1 + ";" + yApi1 + "|" + xApi2 + ";" + yApi2 + "|" + xMercedes + ";" + yMercedes + "|" +
				xCEFrayB + ";" + yCEFrayB + "|" + xCEDolores + ";" + yCEDolores;
		Retorno res = s.rutaACentroMasCercano(xApi1, yApi1);
		assertEquals(ISistema.TipoError.OK, res.getResultado());
		assertEquals(rutaEsperada, res.getValorString());

		// Api2-Mercedes-Young-CETrinidad-CEDurazno
		String rutaEsperada2 = xApi2 + ";" + yApi2 + "|" +
				xYoung + ";" + yYoung + "|" + xCETrinidad + ";" + yCETrinidad + "|" + xCEDurazno + ";" + yCEDurazno;
		Retorno res2 = s.rutaACentroMasCercano(xApi2, yApi2);
		assertEquals(ISistema.TipoError.OK, res2.getResultado());
		assertEquals(rutaEsperada2, res2.getValorString());
	}

	// Helper para cargar el mapa de ejemplo y no duplicar el codigo en cada test
	private void cargarCiudades(Sistema s) {
		s.registrarCiudad("Paysandu", -32.3105104,-58.0759192);
		s.registrarCiudad("Salto", -31.3689985,-57.9119238);
		s.registrarCiudad("Young", -32.698367,-57.6356507);
		s.registrarCiudad("Fray Bentos", -33.1291165,-58.2985057);
		s.registrarCiudad("Mercedes", -33.2563781,-58.0360079);
		s.registrarCiudad("Trinidad", -33.5198572,-56.8987083);
		s.registrarCiudad("Durazno", -33.3851666,-56.5568255);
		s.registrarCiudad("Dolores", -33.5351509,-58.2167245);
	}
}
