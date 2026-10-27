package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.example.estructuras.GrafoMatriz;
import com.example.estructuras.HashPuntos;
import com.example.estructuras.NodoPuntoMapa;
import com.example.sistema.ISistema;
import com.example.sistema.Sistema;

/**
 * Regression tests for HashPuntos and its interaction with GrafoMatriz and
 * Sistema: decimal coordinate precision in the dispersion function, collision
 * probing, duplicate detection, bounded lookups, deletion with probe-chain
 * preservation, and capacity accounting.
 */
public class TestHashPuntos {

	// Adds a point and returns the slot it lands on.
	private int colocar(HashPuntos hash, double coordX, double coordY) {
		NodoPuntoMapa punto = new NodoPuntoMapa(coordX, coordY);
		assertTrue(hash.agregarPunto(punto));
		return hash.indicePuntoMapa(punto);
	}

	@Test
	public void testFuncionDispPreservesDecimalPrecision() {
		HashPuntos hash = new HashPuntos(3);

		// Integer coordinates keep the legacy buckets of 10000*(x+y)%3.
		assertEquals(0, hash.funcionDisp(0.0, 0.0));
		assertEquals(1, hash.funcionDisp(1.0, 0.0));
		assertEquals(2, hash.funcionDisp(1.0, 1.0));
		assertEquals(0, hash.funcionDisp(2.0, 1.0));

		// Decimals keep their weight: they no longer collapse when truncated.
		assertEquals(2, hash.funcionDisp(0.5, 0.0));
		assertEquals(1, hash.funcionDisp(0.1, 0.0));
		assertNotEquals(hash.funcionDisp(0.0, 0.0), hash.funcionDisp(0.5, 0.0));
		assertNotEquals(hash.funcionDisp(0.0, 0.0), hash.funcionDisp(0.25, 0.0));

		// Negative coordinates keep using their absolute value.
		assertEquals(hash.funcionDisp(1.0, 0.0), hash.funcionDisp(-1.0, 0.0));
		assertEquals(hash.funcionDisp(32.3105104, 58.0759192),
				hash.funcionDisp(-32.3105104, -58.0759192));
	}

	@Test
	public void testSameBucketPointsOccupyDistinctSlots() {
		// With cantPuntos = 4 every integer sum of 10000-based coordinates
		// reduces to bucket 0, forcing a single probe chain.
		HashPuntos hash = new HashPuntos(4);
		int a = colocar(hash, 0.0, 0.0);
		int b = colocar(hash, 1.0, 0.0);
		int c = colocar(hash, 0.0, 1.0);
		int d = colocar(hash, 1.0, 1.0);

		assertEquals(0, a);
		assertEquals(1, b);
		assertEquals(2, c);
		assertEquals(3, d);
		assertEquals(4, hash.getPuntosUsados());
		assertFalse(hash.quedanPuntos());

		// Lookups follow the chain to every displaced point.
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(0.0, 0.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(1.0, 0.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(0.0, 1.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(1.0, 1.0)));

		// A missing point returns null and its index miss is -1 instead of
		// silently aliasing the last slot.
		assertNull(hash.buscarPuntoMapa(new NodoPuntoMapa(2.0, 2.0)));
		assertEquals(-1, hash.indicePuntoMapa(new NodoPuntoMapa(2.0, 2.0)));
	}

	@Test
	public void testDuplicatePointsRejected() {
		HashPuntos hash = new HashPuntos(4);
		colocar(hash, 0.0, 0.0);
		colocar(hash, 0.5, 0.0);
		assertEquals(2, hash.getPuntosUsados());

		// Same coordinates are rejected regardless of the instance.
		assertFalse(hash.agregarPunto(new NodoPuntoMapa(0.0, 0.0)));
		assertFalse(hash.agregarPunto(new NodoPuntoMapa(0.5, 0.0)));
		assertEquals(2, hash.getPuntosUsados());

		// Points that differ only in decimals coexist.
		colocar(hash, 1.0, 1.0);
		assertEquals(3, hash.getPuntosUsados());
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(0.5, 0.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(1.0, 1.0)));
	}

	@Test
	public void testCapacityQuotaRespected() {
		HashPuntos hash = new HashPuntos(2);
		colocar(hash, 0.0, 0.0);
		colocar(hash, 1.0, 0.0);

		// The quota of allowed points is enforced even through collisions, and
		// a bounded probe ends in null instead of recursing forever.
		assertFalse(hash.agregarPunto(new NodoPuntoMapa(2.0, 0.0)));
		assertFalse(hash.agregarPunto(new NodoPuntoMapa(0.5, 0.0)));
		assertEquals(2, hash.getPuntosUsados());
		assertFalse(hash.quedanPuntos());
		assertNull(hash.buscarPuntoMapa(new NodoPuntoMapa(2.0, 0.0)));
	}

	@Test
	public void testProbingWrapsAroundTable() {
		// cantPuntos = 7 expands to eleven slots. These six points share
		// bucket 6, so the chain runs 6,7,8,9,10 and then wraps to 0 and 1.
		HashPuntos hash = new HashPuntos(7);
		int a = colocar(hash, 1.0, 4.0);
		int b = colocar(hash, 2.0, 3.0);
		int c = colocar(hash, 5.0, 0.0);
		int d = colocar(hash, 4.0, 1.0);
		int e = colocar(hash, 0.0, 5.0);
		int f = colocar(hash, 3.0, 2.0);
		int g = colocar(hash, 6.0, 6.0);

		assertEquals(6, a);
		assertEquals(7, b);
		assertEquals(8, c);
		assertEquals(9, d);
		assertEquals(10, e);
		assertEquals(0, f);
		assertEquals(1, g);

		// The index lookup covers the whole table, including the last slot.
		assertEquals(10, hash.indicePuntoMapa(new NodoPuntoMapa(0.0, 5.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(3.0, 2.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(6.0, 6.0)));
		assertEquals(7, hash.getPuntosUsados());
	}

	@Test
	public void testBuscarPuntoContinuacionBoundedFromIndex() {
		HashPuntos hash = new HashPuntos(4);
		colocar(hash, 0.0, 0.0);
		NodoPuntoMapa b = new NodoPuntoMapa(1.0, 0.0);
		assertTrue(hash.agregarPunto(b));

		// The search continues from a given slot and finds the point there.
		assertSame(b, hash.buscarPuntoContinuacion(1, b));

		// A different chain segment or an out of range index is normalized and
		// still terminates with null, without recursing forever.
		assertNull(hash.buscarPuntoContinuacion(2, b));
		assertNull(hash.buscarPuntoContinuacion(9, b));
	}

	@Test
	public void testQuitarPuntoKeepsChainReachable() {
		HashPuntos hash = new HashPuntos(7);
		colocar(hash, 1.0, 4.0); // slot 6
		colocar(hash, 2.0, 3.0); // slot 7
		colocar(hash, 5.0, 0.0); // slot 8
		colocar(hash, 4.0, 1.0); // slot 9
		colocar(hash, 0.0, 5.0); // slot 10
		colocar(hash, 3.0, 2.0); // slot 0 (wrap)
		colocar(hash, 6.0, 6.0); // slot 1 (wrap)

		assertTrue(hash.quitarPunto(new NodoPuntoMapa(2.0, 3.0)));
		assertEquals(6, hash.getPuntosUsados());

		// The deleted point is gone; its slot is a tombstone, not a hole.
		assertNull(hash.buscarPuntoMapa(new NodoPuntoMapa(2.0, 3.0)));
		assertTrue(HashPuntos.esEliminado(hash.getPuntos()[7]));
		assertEquals(-1, hash.indicePuntoMapa(new NodoPuntoMapa(2.0, 3.0)));

		// Every point placed after the tombstone stays reachable: lookups skip
		// the marker instead of stopping at the hole.
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(1.0, 4.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(5.0, 0.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(4.0, 1.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(0.0, 5.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(3.0, 2.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(6.0, 6.0)));

		// Slot indexes are stable, so nothing shifted.
		assertEquals(8, hash.indicePuntoMapa(new NodoPuntoMapa(5.0, 0.0)));
		assertEquals(1, hash.indicePuntoMapa(new NodoPuntoMapa(6.0, 6.0)));

		// Removing again is a miss and does not double-count.
		assertFalse(hash.quitarPunto(new NodoPuntoMapa(2.0, 3.0)));
		assertEquals(6, hash.getPuntosUsados());

		// The tombstone slot can be reused by a later insert.
		assertTrue(hash.agregarPunto(new NodoPuntoMapa(2.0, 3.0)));
		assertEquals(7, hash.getPuntosUsados());
		assertEquals(7, hash.indicePuntoMapa(new NodoPuntoMapa(2.0, 3.0)));
	}

	@Test
	public void testQuitarPuntoAtDisplacedSlotLeavesBucketNeighbor() {
		// cantPuntos = 3: (0.0,0.5) and (2.0,0.0) share bucket 2, so the second
		// point is displaced to slot 3, unlike the dispersion bucket.
		HashPuntos hash = new HashPuntos(3);
		colocar(hash, 0.0, 0.5);
		colocar(hash, 2.0, 0.0);

		// Removing the displaced point must not touch the bucket neighbor,
		// which used to be nulled instead of the victim.
		assertTrue(hash.quitarPunto(new NodoPuntoMapa(2.0, 0.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(0.0, 0.5)));
		assertNull(hash.buscarPuntoMapa(new NodoPuntoMapa(2.0, 0.0)));
		assertEquals(1, hash.getPuntosUsados());

		// Fresh table: removing the bucket point leaves its displaced neighbor
		// reachable through the probe chain and its slot untouched.
		hash = new HashPuntos(3);
		colocar(hash, 0.0, 0.5);
		colocar(hash, 2.0, 0.0);
		assertTrue(hash.quitarPunto(new NodoPuntoMapa(0.0, 0.5)));
		assertNull(hash.buscarPuntoMapa(new NodoPuntoMapa(0.0, 0.5)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(2.0, 0.0)));
		assertEquals(1, hash.getPuntosUsados());

		// A coordinate pair that was never stored is a miss.
		assertFalse(hash.quitarPunto(new NodoPuntoMapa(1.0, 2.0)));
	}

	@Test
	public void testQuitarPuntoSlotReuseKeepsLookupsConsistent() {
		HashPuntos hash = new HashPuntos(4);
		colocar(hash, 0.0, 0.0); // slot 0
		colocar(hash, 1.0, 0.0); // slot 1
		colocar(hash, 0.0, 1.0); // slot 2
		colocar(hash, 1.0, 1.0); // slot 3

		assertTrue(hash.quitarPunto(new NodoPuntoMapa(1.0, 0.0)));

		// A later insert reuses the tombstone slot, and the missing point is
		// still told apart from the new occupant.
		assertTrue(hash.agregarPunto(new NodoPuntoMapa(2.0, 0.0)));
		assertEquals(4, hash.getPuntosUsados());
		assertEquals(1, hash.indicePuntoMapa(new NodoPuntoMapa(2.0, 0.0)));
		assertNull(hash.buscarPuntoMapa(new NodoPuntoMapa(1.0, 0.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(2.0, 0.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(0.0, 1.0)));
		assertNotNull(hash.buscarPuntoMapa(new NodoPuntoMapa(1.0, 1.0)));
	}

	@Test
	public void testBorrarPuntoClearsFullMatrixRowAndColumn() {
		GrafoMatriz grafo = new GrafoMatriz(7);
		NodoPuntoMapa a = new NodoPuntoMapa(1.0, 4.0); // slot 6
		NodoPuntoMapa b = new NodoPuntoMapa(2.0, 3.0); // slot 7
		NodoPuntoMapa c = new NodoPuntoMapa(5.0, 0.0); // slot 8
		NodoPuntoMapa d = new NodoPuntoMapa(0.0, 5.0); // slot 9
		assertTrue(grafo.getPuntosMapa().agregarPunto(a));
		assertTrue(grafo.getPuntosMapa().agregarPunto(b));
		assertTrue(grafo.getPuntosMapa().agregarPunto(c));
		assertTrue(grafo.getPuntosMapa().agregarPunto(d));

		// One tramo sits on row/column 7 and another crosses slot 9, beyond
		// cantPuntos, so the full matrix side must be cleared.
		grafo.agregarTramo(b, d, 9);
		grafo.agregarTramo(c, a, 4);
		assertNotNull(grafo.getMatrizAdyacencia()[7][9]);
		assertNotNull(grafo.getMatrizAdyacencia()[8][6]);

		grafo.borrarPunto(new NodoPuntoMapa(2.0, 3.0));

		// Every cell of row and column 7 is gone, including cells past
		// cantPuntos that the old bounded loop never reached.
		for (int i = 0; i < grafo.getMatrizAdyacencia().length; i++) {
			assertNull(grafo.getMatrizAdyacencia()[7][i]);
			assertNull(grafo.getMatrizAdyacencia()[i][7]);
		}

		// The remaining tramo keeps its exact cell: indexes are stable.
		assertNotNull(grafo.getMatrizAdyacencia()[8][6]);
		assertTrue(grafo.existeTramo(c, a));
		assertTrue(grafo.existeTramo(a, c));
		assertFalse(grafo.existeTramo(c, d));

		// The hash side: victim gone, neighbors reachable, count correct.
		assertNull(grafo.getPuntosMapa().buscarPuntoMapa(new NodoPuntoMapa(2.0, 3.0)));
		assertNotNull(grafo.getPuntosMapa().buscarPuntoMapa(new NodoPuntoMapa(1.0, 4.0)));
		assertNotNull(grafo.getPuntosMapa().buscarPuntoMapa(new NodoPuntoMapa(5.0, 0.0)));
		assertNotNull(grafo.getPuntosMapa().buscarPuntoMapa(new NodoPuntoMapa(0.0, 5.0)));
		assertEquals(3, grafo.getPuntosMapa().getPuntosUsados());
	}

	@Test
	public void testEliminarPuntoKeepsCollidingPointFindable() {
		// Sistema-level deletion of a displaced point must not destroy the
		// point sharing its dispersion bucket.
		Sistema s = new Sistema();
		s.inicializarSistema(3);
		assertEquals(ISistema.TipoError.OK, s.registrarCiudad("A", 0.0, 0.0).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarCiudad("B", 0.0, 3.0).getResultado());
		assertEquals(ISistema.TipoError.OK,
				s.registrarTramo(0.0, 3.0, 0.0, 0.0, 5).getResultado());

		assertEquals(ISistema.TipoError.OK, s.eliminarPunto(0.0, 3.0).getResultado());

		// A still exists: re-registering it reports a duplicate.
		assertEquals(ISistema.TipoError.ERROR_2, s.registrarCiudad("A", 0.0, 0.0).getResultado());

		// B can be registered again and its tramo re-created on the same slot.
		assertEquals(ISistema.TipoError.OK, s.registrarCiudad("B", 0.0, 3.0).getResultado());
		assertEquals(ISistema.TipoError.OK,
				s.registrarTramo(0.0, 3.0, 0.0, 0.0, 5).getResultado());

		// Deleting everything frees the capacity again.
		assertEquals(ISistema.TipoError.OK, s.eliminarPunto(0.0, 0.0).getResultado());
		assertEquals(ISistema.TipoError.OK, s.eliminarPunto(0.0, 3.0).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarCiudad("C", 0.0, 6.0).getResultado());
	}

	@Test
	public void testEliminarPuntoInexistente() {
		Sistema s = new Sistema();
		s.inicializarSistema(2);
		assertEquals(ISistema.TipoError.ERROR_1, s.eliminarPunto(0.0, 0.0).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarCiudad("A", 0.0, 0.0).getResultado());
		assertEquals(ISistema.TipoError.ERROR_1, s.eliminarPunto(1.0, 1.0).getResultado());
		assertEquals(ISistema.TipoError.OK, s.eliminarPunto(0.0, 0.0).getResultado());
	}

	@Test
	public void testRegistrarCiudadDuplicadaConDecimales() {
		// Two cities that share the truncated integer coordinates used to
		// confuse the dispersion function: both must register as distinct
		// points, and the repeated coordinates must be rejected.
		Sistema s = new Sistema();
		s.inicializarSistema(4);
		assertEquals(ISistema.TipoError.OK, s.registrarCiudad("Medio", 0.5, 0.0).getResultado());
		assertEquals(ISistema.TipoError.OK, s.registrarCiudad("Cero", 0.0, 0.0).getResultado());
		assertEquals(ISistema.TipoError.ERROR_2, s.registrarCiudad("Otra", 0.5, 0.0).getResultado());

		NodoPuntoMapa medio = s.GrafoMatriz.getPuntosMapa()
				.buscarPuntoMapa(new NodoPuntoMapa(0.5, 0.0));
		assertNotNull(medio);
		assertEquals(Sistema.TipoPunto.CIUDAD, medio.getTipoPunto());
	}

	@Test
	public void testInsertAfterDeletionDetectsDuplicateLaterInChain() {
		// cantPuntos = 2: (0.0,0.0) takes the bucket slot 0 and (1.0,0.0)
		// shares the bucket, probing to slot 1.
		HashPuntos hash = new HashPuntos(2);
		NodoPuntoMapa a = new NodoPuntoMapa(0.0, 0.0);
		NodoPuntoMapa b = new NodoPuntoMapa(1.0, 0.0);
		assertTrue(hash.agregarPunto(a));
		assertTrue(hash.agregarPunto(b));
		assertEquals(2, hash.getPuntosUsados());

		// Deleting the bucket occupant leaves a tombstone at slot 0 while the
		// same-bucket point stays displaced at slot 1.
		assertTrue(hash.quitarPunto(a));
		assertTrue(HashPuntos.esEliminado(hash.getPuntos()[0]));
		assertSame(b, hash.buscarPuntoMapa(new NodoPuntoMapa(1.0, 0.0)));

		// Re-adding the displaced coordinates must be rejected even when the
		// first slot of the chain is reusable: the linear-probe insert may not
		// overwrite a tombstone before checking the rest of the chain.
		assertFalse(hash.agregarPunto(new NodoPuntoMapa(1.0, 0.0)));
		assertEquals(1, hash.getPuntosUsados());
		assertTrue(HashPuntos.esEliminado(hash.getPuntos()[0]));
		assertSame(b, hash.buscarPuntoMapa(new NodoPuntoMapa(1.0, 0.0)));
		assertEquals(1, hash.indicePuntoMapa(new NodoPuntoMapa(1.0, 0.0)));

		// Once the displaced point is gone too, the coordinates can be
		// registered again, and the tombstone closest to the bucket wins.
		assertTrue(hash.quitarPunto(b));
		assertTrue(hash.agregarPunto(new NodoPuntoMapa(1.0, 0.0)));
		assertEquals(1, hash.getPuntosUsados());
		assertEquals(0, hash.indicePuntoMapa(new NodoPuntoMapa(1.0, 0.0)));
	}

	@Test
	public void testEncontrarLugarVacioCannotBypassConstraints() {
		// Capacity: a direct call over a full quota table must not create a
		// slot beyond the allowed points.
		HashPuntos hash = new HashPuntos(2);
		hash.agregarPunto(new NodoPuntoMapa(0.0, 0.0));
		hash.agregarPunto(new NodoPuntoMapa(1.0, 0.0));
		assertFalse(hash.encontrarLugarVacio(0, new NodoPuntoMapa(2.0, 0.0)));
		assertEquals(2, hash.getPuntosUsados());

		// Duplicates: starting the insertion at a tombstone slot must also
		// reject coordinates that live deeper in the same probe chain.
		HashPuntos hash2 = new HashPuntos(2);
		hash2.agregarPunto(new NodoPuntoMapa(0.0, 0.0));
		hash2.agregarPunto(new NodoPuntoMapa(1.0, 0.0));
		hash2.quitarPunto(new NodoPuntoMapa(0.0, 0.0));
		assertFalse(hash2.encontrarLugarVacio(0, new NodoPuntoMapa(1.0, 0.0)));
		assertEquals(1, hash2.getPuntosUsados());
		assertTrue(HashPuntos.esEliminado(hash2.getPuntos()[0]));

		// A genuinely new point is still accepted, reusing the tombstone slot.
		assertTrue(hash2.encontrarLugarVacio(0, new NodoPuntoMapa(5.0, 5.0)));
		assertEquals(2, hash2.getPuntosUsados());
		assertNotNull(hash2.buscarPuntoMapa(new NodoPuntoMapa(5.0, 5.0)));
	}
}
