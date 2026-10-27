package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.example.estructuras.GrafoMatriz;
import com.example.estructuras.NodoPuntoMapa;

/**
 * Regression tests for the custom Dijkstra implementation in GrafoMatriz
 * (adjacency matrix with bidirectional edges). They lock in direct and
 * multi-hop routes, disconnected vertices, sparse hash indices, origin equals
 * destination, integer overflow handling, and route reconstruction.
 */
public class TestGrafoMatriz {

	// Registers a point on the graph and returns its hash index.
	private int agregarPunto(GrafoMatriz grafo, double coordX, double coordY) {
		NodoPuntoMapa punto = new NodoPuntoMapa(coordX, coordY);
		assertTrue(grafo.getPuntosMapa().agregarPunto(punto));
		return grafo.getPuntosMapa().indicePuntoMapa(punto);
	}

	// Registers a directed segment origen -> destino with the given peso.
	private void agregarTramo(GrafoMatriz grafo, int origen, int destino, int peso) {
		NodoPuntoMapa nodoOrigen = grafo.getPuntosMapa().getPuntos()[origen];
		NodoPuntoMapa nodoDestino = grafo.getPuntosMapa().getPuntos()[destino];
		grafo.agregarTramo(nodoOrigen, nodoDestino, peso);
	}

	@Test
	public void testDirectRoute() {
		GrafoMatriz grafo = new GrafoMatriz(2);
		int a = agregarPunto(grafo, 0.0, 0.0);
		int b = agregarPunto(grafo, 1.0, 0.0);
		agregarTramo(grafo, a, b, 4);

		// The segment is stored a -> b, but routes are bidirectional.
		assertEquals(4, grafo.caminoMasCorto(a, b));
		int[] previo = grafo.getPrevio();
		assertEquals(a, previo[b]);
		// A direct route has no intermediate vertices.
		grafo.imprimirCamino(previo, a, b);
		assertEquals("", grafo.getMiValor());

		// The same segment queried from the other end.
		assertEquals(4, grafo.caminoMasCorto(b, a));
		assertEquals(b, grafo.getPrevio()[a]);
	}

	@Test
	public void testMultiHopRoute() {
		GrafoMatriz grafo = new GrafoMatriz(3);
		int a = agregarPunto(grafo, 0.0, 0.0);
		int b = agregarPunto(grafo, 1.0, 0.0);
		int c = agregarPunto(grafo, 0.0, 1.0);
		agregarTramo(grafo, a, b, 10);
		agregarTramo(grafo, b, c, 5);
		agregarTramo(grafo, a, c, 100);

		// The two-hop route a -> b -> c beats the expensive direct segment.
		assertEquals(15, grafo.caminoMasCorto(a, c));
		int[] previo = grafo.getPrevio();
		assertEquals(b, previo[c]);
		assertEquals(a, previo[b]);

		// Reconstruction lists only the intermediate vertex, in route order.
		grafo.imprimirCamino(previo, a, c);
		assertEquals("1.0;0.0|", grafo.getMiValor());
	}

	@Test
	public void testMultiHopRouteReconstruction() {
		GrafoMatriz grafo = new GrafoMatriz(4);
		int a = agregarPunto(grafo, 0.0, 0.0);
		int b = agregarPunto(grafo, 1.0, 0.0);
		int c = agregarPunto(grafo, 0.0, 1.0);
		int d = agregarPunto(grafo, 1.0, 1.0);
		agregarTramo(grafo, a, b, 10);
		agregarTramo(grafo, b, c, 5);
		agregarTramo(grafo, c, d, 3);
		agregarTramo(grafo, a, d, 50);

		assertEquals(18, grafo.caminoMasCorto(a, d));
		int[] previo = grafo.getPrevio();
		assertEquals(c, previo[d]);
		assertEquals(b, previo[c]);

		// Intermediates are appended in origin -> destination order.
		grafo.imprimirCamino(previo, a, d);
		assertEquals("1.0;0.0|0.0;1.0|", grafo.getMiValor());
	}

	@Test
	public void testDisconnectedVertices() {
		GrafoMatriz grafo = new GrafoMatriz(4);
		int a = agregarPunto(grafo, 0.0, 0.0);
		int b = agregarPunto(grafo, 1.0, 0.0);
		int c = agregarPunto(grafo, 0.0, 1.0);
		int d = agregarPunto(grafo, 1.0, 1.0);
		agregarTramo(grafo, a, b, 4);
		agregarTramo(grafo, c, d, 6);

		// Vertices in the other component stay unreachable in both directions
		// instead of reporting a poisoned (negative) cost.
		assertEquals(Integer.MAX_VALUE, grafo.caminoMasCorto(a, d));
		int[] previo = grafo.getPrevio();
		assertEquals(-1, previo[d]);
		// Reconstruction of an absent path must stay empty, never throw.
		grafo.imprimirCamino(previo, a, d);
		assertEquals("", grafo.getMiValor());
		grafo.imprimirCamino(previo, d, a);
		assertEquals("", grafo.getMiValor());

		assertEquals(Integer.MAX_VALUE, grafo.caminoMasCorto(d, a));
		assertEquals(Integer.MAX_VALUE, grafo.caminoMasCorto(b, c));
		assertEquals(Integer.MAX_VALUE, grafo.caminoMasCorto(c, b));

		// An isolated origin equals its own destination at zero cost.
		assertEquals(0, grafo.caminoMasCorto(d, d));

		// Connected queries keep working after unreachable ones.
		assertEquals(4, grafo.caminoMasCorto(a, b));
		assertEquals(6, grafo.caminoMasCorto(c, d));
	}

	@Test
	public void testSparseIndices() {
		// cantPuntos = 3 expands to five matrix slots, and the dispersion
		// function spreads these three points over slots 0, 1 and 2.
		GrafoMatriz grafo = new GrafoMatriz(3);
		int a = agregarPunto(grafo, 0.0, 0.0);
		int b = agregarPunto(grafo, 1.0, 0.0);
		int c = agregarPunto(grafo, 0.0, 1.0);

		// One point must sit on slot 0 so the route below can only be found
		// when relaxation covers the whole matrix range.
		assertEquals(0, a);
		assertEquals(1, b);
		assertEquals(2, c);

		// c -> b -> a: two hops landing on slot 0.
		agregarTramo(grafo, c, b, 7);
		agregarTramo(grafo, b, a, 5);

		assertEquals(12, grafo.caminoMasCorto(c, a));
		int[] previo = grafo.getPrevio();
		assertEquals(b, previo[a]);
		grafo.imprimirCamino(previo, c, a);
		assertEquals("1.0;0.0|", grafo.getMiValor());

		// Bidirectional: the same route found from the other end.
		assertEquals(12, grafo.caminoMasCorto(a, c));
		assertEquals(7, grafo.caminoMasCorto(c, b));
		assertEquals(5, grafo.caminoMasCorto(b, a));
	}

	@Test
	public void testOriginEqualsDestination() {
		GrafoMatriz grafo = new GrafoMatriz(3);
		int a = agregarPunto(grafo, 0.0, 0.0);
		int b = agregarPunto(grafo, 1.0, 0.0);
		int c = agregarPunto(grafo, 0.0, 1.0);
		agregarTramo(grafo, a, b, 10);
		agregarTramo(grafo, b, c, 5);

		// A route from a point to itself costs zero, even when the point is
		// connected, and its only predecessor is itself.
		assertEquals(0, grafo.caminoMasCorto(a, a));
		assertEquals(a, grafo.getPrevio()[a]);
		assertEquals(0, grafo.caminoMasCorto(c, c));
		assertEquals(c, grafo.getPrevio()[c]);

		// Distances to other points keep working afterwards.
		assertEquals(15, grafo.caminoMasCorto(a, c));
	}

	@Test
	public void testOverflowDoesNotWrap() {
		// Three segments of 1_000_000_000 add up to 3_000_000_000, which does
		// not fit in an int: the route must be reported unreachable, never
		// negative.
		GrafoMatriz grafo = new GrafoMatriz(4);
		int a = agregarPunto(grafo, 0.0, 0.0);
		int b = agregarPunto(grafo, 1.0, 0.0);
		int c = agregarPunto(grafo, 0.0, 1.0);
		int d = agregarPunto(grafo, 1.0, 1.0);
		agregarTramo(grafo, a, b, 1_000_000_000);
		agregarTramo(grafo, b, c, 1_000_000_000);
		agregarTramo(grafo, c, d, 1_000_000_000);

		// The first two hops still fit and must add up exactly.
		assertEquals(2_000_000_000, grafo.caminoMasCorto(a, c));
		assertEquals(Integer.MAX_VALUE, grafo.caminoMasCorto(a, d));
		assertEquals(-1, grafo.getPrevio()[d]);
	}

	@Test
	public void testMaxValueWeightStaysUnreachable() {
		GrafoMatriz grafo = new GrafoMatriz(3);
		int a = agregarPunto(grafo, 0.0, 0.0);
		int b = agregarPunto(grafo, 1.0, 0.0);
		int c = agregarPunto(grafo, 0.0, 1.0);
		agregarTramo(grafo, a, b, 3);
		agregarTramo(grafo, b, c, Integer.MAX_VALUE);
		agregarTramo(grafo, a, c, Integer.MAX_VALUE);

		// The cheap segment keeps its cost, and the overflowing segments are
		// reported unreachable instead of wrapping to negative costs.
		assertEquals(3, grafo.caminoMasCorto(a, b));
		assertEquals(Integer.MAX_VALUE, grafo.caminoMasCorto(a, c));
		assertEquals(-1, grafo.getPrevio()[c]);
	}

	@Test
	public void testDistanciaMasCortaCoversFullArray() {
		GrafoMatriz grafo = new GrafoMatriz(2);

		// The minimum can sit on the last slot of the array.
		assertEquals(2, grafo.distanciaMasCorta(new boolean[4],
				new int[] { Integer.MAX_VALUE, 7, 3, 5 }));
		// Visited vertices are skipped, keeping the next cheapest one.
		assertEquals(3, grafo.distanciaMasCorta(new boolean[] { false, false, true, false },
				new int[] { Integer.MAX_VALUE, 7, 3, 5 }));
		// Ties resolve to the lowest index.
		assertEquals(0, grafo.distanciaMasCorta(new boolean[4],
				new int[] { 5, 5, Integer.MAX_VALUE, Integer.MAX_VALUE }));
		// Every vertex visited means there is nothing left to select.
		assertEquals(-1, grafo.distanciaMasCorta(new boolean[] { true, true, true, true }, new int[4]));
	}
}
