/*
 * Created on Aug 5, 2006
 */
package com.mattwhitlock.common.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mattwhitlock.common.ranges.Range;

/**
 * @author Matt Whitlock
 */
public class RangeTest {

	private final Range<Integer> rnn = new Range<>(null, false, null, false);
	private final Range<Integer> rni = new Range<>(null, false, 3, true);
	private final Range<Integer> rnx = new Range<>(null, false, 3, false);
	private final Range<Integer> rin = new Range<>(1, true, null, false);
	private final Range<Integer> rxn = new Range<>(1, false, null, false);
	private final Range<Integer> rii = new Range<>(1, true, 3, true);
	private final Range<Integer> rix = new Range<>(1, true, 3, false);
	private final Range<Integer> rxi = new Range<>(1, false, 3, true);
	private final Range<Integer> rxx = new Range<>(1, false, 3, false);

	private static void assertContains(boolean truth, Range<Integer> r, Integer v) {
		if (r.contains(v) != truth) {
			fail(r + (truth ? " should contain " : " should not contain ") + v);
		}
	}

	private static void assertContains(boolean truth, Range<Integer> r, Range<Integer> s) {
		if (r.contains(s) != truth) {
			fail(r + (truth ? " should contain " : " should not contain ") + s);
		}
	}

	private static void assertOverlaps(boolean truth, Range<Integer> r, Range<Integer> s) {
		if (r.intersects(s) != truth) {
			fail(r + (truth ? " should overlap " : " should not overlap ") + s);
		}
		if (!r.equals(s)) {
			if (s.intersects(r) != truth) {
				fail(s + (truth ? " should overlap " : " should not overlap ") + r);
			}
		}
	}

	private static void assertDifference(Object d, Range<Integer> r, Range<Integer> s) {
		Range<Integer> a = r.difference(s);
		if (!Objects.equals(a, d)) {
			fail("difference of " + r + " with " + s + " should be " + d + " but was " + a);
		}
	}

	private static void assertIntersection(Object i, Range<Integer> r, Range<Integer> s) {
		Range<Integer> a = r.intersection(s);
		if (!Objects.equals(a, i)) {
			fail("intersection of " + r + " with " + s + " should be " + i + " but was " + a);
		}
		if (!r.equals(s)) {
			a = s.intersection(r);
			if (!Objects.equals(a, i)) {
				fail("intersection of " + s + " with " + r + " should be " + i + " but was " + a);
			}
		}
	}

	private static void assertUnion(Range<Integer> u, Range<Integer> r, Range<Integer> s) {
		Range<Integer> a = r.union(s);
		if (!Objects.equals(a, u)) {
			fail("union of " + r + " with " + s + " should be " + u + " but was " + a);
		}
		if (!r.equals(s)) {
			a = s.union(r);
			if (!Objects.equals(a, u)) {
				fail("union of " + s + " with " + r + " should be " + u + " but was " + a);
			}
		}
	}

	@Test
	public void testContainsValue() {
		boolean tnn[] = { true, true, true, true, true };
		boolean tni[] = { true, true, true, true, false };
		boolean tnx[] = { true, true, true, false, false };
		boolean tin[] = { false, true, true, true, true };
		boolean txn[] = { false, false, true, true, true };
		boolean tii[] = { false, true, true, true, false };
		boolean tix[] = { false, true, true, false, false };
		boolean txi[] = { false, false, true, true, false };
		boolean txx[] = { false, false, true, false, false };
		for (int i = 0; i < 5; ++i) {
			assertContains(tnn[i], rnn, i);
			assertContains(tni[i], rni, i);
			assertContains(tnx[i], rnx, i);
			assertContains(tin[i], rin, i);
			assertContains(txn[i], rxn, i);
			assertContains(tii[i], rii, i);
			assertContains(tix[i], rix, i);
			assertContains(txi[i], rxi, i);
			assertContains(txx[i], rxx, i);
		}
	}

	@Test
	public void testContainsRangeNN() {
		Range<Integer> s = Range.<Integer>allValues();
		assertContains(true, rnn, s);
		assertContains(false, rni, s);
		assertContains(false, rnx, s);
		assertContains(false, rin, s);
		assertContains(false, rxn, s);
		assertContains(false, rii, s);
		assertContains(false, rix, s);
		assertContains(false, rxi, s);
		assertContains(false, rxx, s);
	}

	@Test
	public void testContainsRangeNI() {
		boolean tnn[] = { true, true, true, true, true };
		boolean tni[] = { true, true, true, true, false };
		boolean tnx[] = { true, true, true, false, false };
		boolean tin[] = { false, false, false, false, false };
		boolean txn[] = { false, false, false, false, false };
		boolean tii[] = { false, false, false, false, false };
		boolean tix[] = { false, false, false, false, false };
		boolean txi[] = { false, false, false, false, false };
		boolean txx[] = { false, false, false, false, false };
		for (int u = 4; u >= 0; --u) {
			Range<Integer> s = new Range<>(null, false, u, true);
			assertContains(tnn[u], rnn, s);
			assertContains(tni[u], rni, s);
			assertContains(tnx[u], rnx, s);
			assertContains(tin[u], rin, s);
			assertContains(txn[u], rxn, s);
			assertContains(tii[u], rii, s);
			assertContains(tix[u], rix, s);
			assertContains(txi[u], rxi, s);
			assertContains(txx[u], rxx, s);
		}
	}

	@Test
	public void testContainsRangeIN() {
		boolean tnn[] = { true, true, true, true, true };
		boolean tni[] = { false, false, false, false, false };
		boolean tnx[] = { false, false, false, false, false };
		boolean tin[] = { false, true, true, true, true };
		boolean txn[] = { false, false, true, true, true };
		boolean tii[] = { false, false, false, false, false };
		boolean tix[] = { false, false, false, false, false };
		boolean txi[] = { false, false, false, false, false };
		boolean txx[] = { false, false, false, false, false };
		for (int l = 4; l >= 0; --l) {
			Range<Integer> s = new Range<>(l, true, null, false);
			assertContains(tnn[l], rnn, s);
			assertContains(tni[l], rni, s);
			assertContains(tnx[l], rnx, s);
			assertContains(tin[l], rin, s);
			assertContains(txn[l], rxn, s);
			assertContains(tii[l], rii, s);
			assertContains(tix[l], rix, s);
			assertContains(txi[l], rxi, s);
			assertContains(txx[l], rxx, s);
		}
	}

	@Test
	public void testContainsRangeII() {
		boolean tnn[][] = { { true }, { true, true }, { true, true, true }, { true, true, true, true }, { true, true, true, true, true } };
		boolean tni[][] = { { true }, { true, true }, { true, true, true }, { true, true, true, true }, { false, false, false, false, false } };
		boolean tnx[][] = { { true }, { true, true }, { true, true, true }, { false, false, false, false }, { false, false, false, false, false } };
		boolean tin[][] = { { false }, { false, true }, { false, true, true }, { false, true, true, true }, { false, true, true, true, true } };
		boolean txn[][] = { { false }, { false, false }, { false, false, true }, { false, false, true, true }, { false, false, true, true, true } };
		boolean tii[][] = { { false }, { false, true }, { false, true, true }, { false, true, true, true }, { false, false, false, false, false } };
		boolean tix[][] = { { false }, { false, true }, { false, true, true }, { false, false, false, false }, { false, false, false, false, false } };
		boolean txi[][] = { { false }, { false, false }, { false, false, true }, { false, false, true, true }, { false, false, false, false, false } };
		boolean txx[][] = { { false }, { false, false }, { false, false, true }, { false, false, false, false }, { false, false, false, false, false } };
		for (int u = 4; u >= 0; --u) {
			for (int l = u; l >= 0; --l) {
				Range<Integer> s = new Range<>(l, true, u, true);
				assertContains(tnn[u][l], rnn, s);
				assertContains(tni[u][l], rni, s);
				assertContains(tnx[u][l], rnx, s);
				assertContains(tin[u][l], rin, s);
				assertContains(txn[u][l], rxn, s);
				assertContains(tii[u][l], rii, s);
				assertContains(tix[u][l], rix, s);
				assertContains(txi[u][l], rxi, s);
				assertContains(txx[u][l], rxx, s);
			}
		}
	}

	@Test
	public void testContainsExtra() {
		Range<Integer> t;
		t = new Range<>(1, true, 2, true);
		Assertions.assertTrue(rii.contains(t));
		Assertions.assertTrue(!rxx.contains(t));
		t = new Range<>(1, false, 2, true);
		Assertions.assertTrue(rii.contains(t));
		Assertions.assertTrue(rxx.contains(t));
		t = new Range<>(2, true, 3, true);
		Assertions.assertTrue(rii.contains(t));
		Assertions.assertTrue(!rxx.contains(t));
		t = new Range<>(2, true, 3, false);
		Assertions.assertTrue(rii.contains(t));
		Assertions.assertTrue(rxx.contains(t));
	}

	@Test
	public void testOverlapsNN() {
		Range<Integer> s = Range.<Integer>allValues();
		assertOverlaps(true, rnn, s);
		assertOverlaps(true, rni, s);
		assertOverlaps(true, rnx, s);
		assertOverlaps(true, rin, s);
		assertOverlaps(true, rxn, s);
		assertOverlaps(true, rii, s);
		assertOverlaps(true, rix, s);
		assertOverlaps(true, rxi, s);
		assertOverlaps(true, rxx, s);
	}

	@Test
	public void testOverlapsNI() {
		boolean tnn[] = { true, true, true, true, true };
		boolean tni[] = { true, true, true, true, true };
		boolean tnx[] = { true, true, true, true, true };
		boolean tin[] = { false, true, true, true, true };
		boolean txn[] = { false, false, true, true, true };
		boolean tii[] = { false, true, true, true, true };
		boolean tix[] = { false, true, true, true, true };
		boolean txi[] = { false, false, true, true, true };
		boolean txx[] = { false, false, true, true, true };
		for (int u = 4; u >= 0; --u) {
			Range<Integer> s = new Range<>(null, false, u, true);
			assertOverlaps(tnn[u], rnn, s);
			assertOverlaps(tni[u], rni, s);
			assertOverlaps(tnx[u], rnx, s);
			assertOverlaps(tin[u], rin, s);
			assertOverlaps(txn[u], rxn, s);
			assertOverlaps(tii[u], rii, s);
			assertOverlaps(tix[u], rix, s);
			assertOverlaps(txi[u], rxi, s);
			assertOverlaps(txx[u], rxx, s);
		}
	}

	@Test
	public void testOverlapsBN() {
		boolean tnn[] = { true, true, true, true, true };
		boolean tni[] = { true, true, true, true, false };
		boolean tnx[] = { true, true, true, false, false };
		boolean tin[] = { true, true, true, true, true };
		boolean txn[] = { true, true, true, true, true };
		boolean tii[] = { true, true, true, true, false };
		boolean tix[] = { true, true, true, false, false };
		boolean txi[] = { true, true, true, true, false };
		boolean txx[] = { true, true, true, false, false };
		for (int l = 4; l >= 0; --l) {
			Range<Integer> s = new Range<>(l, true, null, false);
			assertOverlaps(tnn[l], rnn, s);
			assertOverlaps(tni[l], rni, s);
			assertOverlaps(tnx[l], rnx, s);
			assertOverlaps(tin[l], rin, s);
			assertOverlaps(txn[l], rxn, s);
			assertOverlaps(tii[l], rii, s);
			assertOverlaps(tix[l], rix, s);
			assertOverlaps(txi[l], rxi, s);
			assertOverlaps(txx[l], rxx, s);
		}
	}

	@Test
	public void testOverlapsII() {
		boolean tnn[][] = { { true }, { true, true }, { true, true, true }, { true, true, true, true }, { true, true, true, true, true } };
		boolean tni[][] = { { true }, { true, true }, { true, true, true }, { true, true, true, true }, { true, true, true, true, false } };
		boolean tnx[][] = { { true }, { true, true }, { true, true, true }, { true, true, true, false }, { true, true, true, false, false } };
		boolean tin[][] = { { false }, { true, true }, { true, true, true }, { true, true, true, true }, { true, true, true, true, true } };
		boolean txn[][] = { { false }, { false, false }, { true, true, true }, { true, true, true, true }, { true, true, true, true, true } };
		boolean tii[][] = { { false }, { true, true }, { true, true, true }, { true, true, true, true }, { true, true, true, true, false } };
		boolean tix[][] = { { false }, { true, true }, { true, true, true }, { true, true, true, false }, { true, true, true, false, false } };
		boolean txi[][] = { { false }, { false, false }, { true, true, true }, { true, true, true, true }, { true, true, true, true, false } };
		boolean txx[][] = { { false }, { false, false }, { true, true, true }, { true, true, true, false }, { true, true, true, false, false } };
		for (int u = 4; u >= 0; --u) {
			for (int l = u; l >= 0; --l) {
				Range<Integer> s = new Range<>(l, true, u, true);
				assertOverlaps(tnn[u][l], rnn, s);
				assertOverlaps(tni[u][l], rni, s);
				assertOverlaps(tnx[u][l], rnx, s);
				assertOverlaps(tin[u][l], rin, s);
				assertOverlaps(txn[u][l], rxn, s);
				assertOverlaps(tii[u][l], rii, s);
				assertOverlaps(tix[u][l], rix, s);
				assertOverlaps(txi[u][l], rxi, s);
				assertOverlaps(txx[u][l], rxx, s);
			}
		}
	}

	@Test
	public void testOverlapsExtra() {
		assertOverlaps(false, new Range<>(null, false, 0, false), new Range<>(0, false, null, false));
	}

	@Test
	public void testDifference() {
		assertDifference(new Range<>(2, false, 3, true), rni, new Range<>(null, false, 2, true));
		assertDifference(new Range<>(2, true, 3, true), rni, new Range<>(null, false, 2, false));
		assertDifference(new Range<>(null, false, 2, false), rni, new Range<>(2, true, 4, true));
		assertDifference(new Range<>(null, false, 2, true), rni, new Range<>(2, false, 4, true));
		assertDifference(new Range<>(1, true, 2, false), rin, new Range<>(2, true, null, false));
		assertDifference(new Range<>(1, true, 2, true), rin, new Range<>(2, false, null, false));
		assertDifference(new Range<>(2, false, null, false), rin, new Range<>(0, true, 2, true));
		assertDifference(new Range<>(2, true, null, false), rin, new Range<>(0, true, 2, false));
		assertDifference(new Range<>(2, false, 3, true), rii, new Range<>(0, true, 2, true));
		assertDifference(new Range<>(2, true, 3, true), rii, new Range<>(0, true, 2, false));
		assertDifference(new Range<>(1, true, 2, false), rii, new Range<>(2, true, 4, true));
		assertDifference(new Range<>(1, true, 2, true), rii, new Range<>(2, false, 4, true));
	}

	@Test
	public void testIntersection() {
		assertIntersection(rnn, rnn, rnn);
		assertIntersection(rni, rni, rnn);
		assertIntersection(rin, rin, rnn);
		assertIntersection(rii, rii, rnn);
		assertIntersection(rii, rni, rin);
		assertIntersection(rii, rii, rni);
		assertIntersection(rii, rii, rin);
		assertIntersection(rii, rii, rii);
		assertIntersection(new Range<>(0, true, 2, true), rni, new Range<>(0, true, 2, true));
		assertIntersection(new Range<>(2, true, 3, true), rni, new Range<>(2, true, 4, true));
		assertIntersection(new Range<>(2, false, 3, true), rni, new Range<>(2, false, 4, true));
		assertIntersection(new Range<>(1, true, 2, true), rin, new Range<>(0, true, 2, true));
		assertIntersection(new Range<>(1, true, 2, false), rin, new Range<>(0, true, 2, false));
		assertIntersection(new Range<>(2, true, 4, true), rin, new Range<>(2, true, 4, true));
		assertIntersection(new Range<>(2, false, 4, true), rin, new Range<>(2, false, 4, true));
		assertIntersection(new Range<>(1, true, 2, true), rii, new Range<>(0, true, 2, true));
		assertIntersection(new Range<>(1, true, 2, false), rii, new Range<>(0, true, 2, false));
		assertIntersection(new Range<>(2, true, 3, true), rii, new Range<>(2, true, 4, true));
		assertIntersection(new Range<>(2, false, 3, true), rii, new Range<>(2, false, 4, true));
	}

	@Test
	public void testUnion() {
		assertUnion(rnn, rnn, rnn);
		assertUnion(rnn, rni, rnn);
		assertUnion(rnn, rin, rnn);
		assertUnion(rnn, rii, rnn);
		assertUnion(rnn, rni, rin);
		assertUnion(rni, rii, rni);
		assertUnion(rin, rii, rin);
		assertUnion(rii, rii, rii);
		assertUnion(rni, rni, new Range<>(0, true, 2, true));
		assertUnion(new Range<>(null, false, 4, true), rni, new Range<>(2, true, 4, true));
		assertUnion(new Range<>(null, false, 4, true), rni, new Range<>(2, false, 4, true));
		assertUnion(new Range<>(null, false, 4, true), rni, new Range<>(3, false, 4, true));
		assertUnion(new Range<>(0, true, null, false), rin, new Range<>(0, true, 1, false));
		assertUnion(new Range<>(0, false, null, false), rin, new Range<>(0, false, 2, true));
		assertUnion(new Range<>(0, true, null, false), rin, new Range<>(0, true, 2, false));
		assertUnion(rin, rin, new Range<>(2, true, 4, true));
		assertUnion(rin, rin, new Range<>(2, false, 4, true));
		assertUnion(new Range<>(0, true, 3, true), rii, new Range<>(0, true, 1, false));
		assertUnion(new Range<>(0, true, 3, true), rii, new Range<>(0, true, 1, true));
		assertUnion(new Range<>(0, true, 3, true), rii, new Range<>(0, true, 2, true));
		assertUnion(new Range<>(0, true, 3, true), rii, new Range<>(0, true, 2, false));
		assertUnion(new Range<>(1, true, 4, false), rii, new Range<>(2, true, 4, false));
		assertUnion(new Range<>(1, true, 4, true), rii, new Range<>(2, false, 4, true));
		assertUnion(new Range<>(1, true, 4, true), rii, new Range<>(3, true, 4, true));
		assertUnion(new Range<>(1, true, 4, true), rii, new Range<>(3, false, 4, true));
	}

}
