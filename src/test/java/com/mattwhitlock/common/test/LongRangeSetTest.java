/*
 * Created on Aug 6, 2006
 */
package com.mattwhitlock.common.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.mattwhitlock.common.ranges.LongRange;
import com.mattwhitlock.common.ranges.LongRangeSet;

/**
 * @author Matt Whitlock
 */
public class LongRangeSetTest {

	public static final LongRange negativeRange, nonPositiveRange, zeroRange, nonNegativeRange,
			positiveRange, infiniteRange;
	public static final LongRangeSet nullSet, negativeSet, nonPositiveSet, zeroSet, nonNegativeSet,
			positiveSet, infiniteSet, nonZeroSet;

	static {
		negativeRange = LongRange.to(0, false);
		nonPositiveRange = LongRange.to(0, true);
		zeroRange = LongRange.ofValue(0);
		nonNegativeRange = LongRange.from(0, true);
		positiveRange = LongRange.from(0, false);
		infiniteRange = LongRange.allValues;
		nullSet = new LongRangeSet();
		negativeSet = new LongRangeSet(negativeRange);
		nonPositiveSet = new LongRangeSet(nonPositiveRange);
		zeroSet = new LongRangeSet(zeroRange);
		nonNegativeSet = new LongRangeSet(nonNegativeRange);
		positiveSet = new LongRangeSet(positiveRange);
		infiniteSet = new LongRangeSet(infiniteRange);
		nonZeroSet = new LongRangeSet();
		nonZeroSet.add(negativeRange);
		nonZeroSet.add(positiveRange);
	}

	private static final LongRange[] ranges = { negativeRange, nonPositiveRange, zeroRange, nonNegativeRange, positiveRange, infiniteRange };
	private static final LongRangeSet[] sets = { nullSet, negativeSet, nonPositiveSet, zeroSet, nonNegativeSet, positiveSet, infiniteSet, nonZeroSet };

	private static void assertAdd(Object expected, LongRangeSet orig, LongRange add) {
		LongRangeSet actual = orig.clone();
		actual.add(add);
		if (!actual.equals(expected)) {
			fail("adding " + add + " to " + orig + " should produce " + expected + " but actually produced " + actual);
		}
	}

	private static void assertRemove(Object expected, LongRangeSet orig, LongRange remove) {
		LongRangeSet actual = orig.clone();
		actual.remove(remove);
		if (!actual.equals(expected)) {
			fail("removing " + remove + " from " + orig + " should produce " + expected + " but actually produced " + actual);
		}
	}

	private static void assertComplement(Object expected, LongRangeSet orig) {
		LongRangeSet actual = new LongRangeSet(LongRange.allValues);
		actual.removeAll(orig);
		if (!actual.equals(expected)) {
			fail("complementing " + orig + " should produce " + expected + " but actually produced " + actual);
		}
	}

	@Test
	public void testSets() {
		assertEquals("{ }", nullSet.toString());
		assertEquals("{ (, 0) }", negativeSet.toString());
		assertEquals("{ (, 0] }", nonPositiveSet.toString());
		assertEquals("{ [0, 0] }", zeroSet.toString());
		assertEquals("{ [0, ) }", nonNegativeSet.toString());
		assertEquals("{ (0, ) }", positiveSet.toString());
		assertEquals("{ (, ) }", infiniteSet.toString());
		assertEquals("{ (, 0), (0, ) }", nonZeroSet.toString());
	}

	@Test
	public void testAdd() {
		LongRangeSet expected[][] = { { negativeSet, nonPositiveSet, zeroSet, nonNegativeSet, positiveSet, infiniteSet }, { negativeSet, nonPositiveSet, nonPositiveSet, infiniteSet, nonZeroSet, infiniteSet }, { nonPositiveSet, nonPositiveSet, nonPositiveSet, infiniteSet, infiniteSet, infiniteSet }, { nonPositiveSet, nonPositiveSet, zeroSet, nonNegativeSet, nonNegativeSet, infiniteSet }, { infiniteSet, infiniteSet, nonNegativeSet, nonNegativeSet, nonNegativeSet, infiniteSet }, { nonZeroSet, infiniteSet, nonNegativeSet, nonNegativeSet, positiveSet, infiniteSet }, { infiniteSet, infiniteSet, infiniteSet, infiniteSet, infiniteSet, infiniteSet }, { nonZeroSet, infiniteSet, infiniteSet, infiniteSet, nonZeroSet, infiniteSet } };
		for (int i = 0; i < sets.length; ++i) {
			for (int j = 0; j < ranges.length; ++j) {
				assertAdd(expected[i][j], sets[i], ranges[j]);
			}
		}
	}

	@Test
	public void testAddDisjoint2() {
		LongRangeSet orig = new LongRangeSet();
		orig.add(LongRange.ofValue(-1));
		orig.add(LongRange.ofValue(1));
		LongRangeSet expected = new LongRangeSet();
		expected.add(new LongRange(-1, true, 1, true));
		assertAdd(expected, orig, new LongRange(-1, false, 1, false));
	}

	@Test
	public void testAddOverlap2() {
		LongRangeSet orig = new LongRangeSet();
		orig.add(new LongRange(-1, true, 0, false));
		orig.add(new LongRange(0, false, 1, true));
		LongRangeSet expected = new LongRangeSet();
		expected.add(new LongRange(-1, true, 1, true));
		assertAdd(expected, orig, new LongRange(-1, false, 1, false));
	}

	@Test
	public void testAddOverlap3() {
		LongRangeSet orig = new LongRangeSet();
		orig.add(new LongRange(-2, true, -1, false));
		orig.add(new LongRange(-1, false, 1, false));
		orig.add(new LongRange(1, false, 2, true));
		LongRangeSet expected = new LongRangeSet();
		expected.add(new LongRange(-2, true, 2, true));
		assertAdd(expected, orig, new LongRange(-2, false, 2, false));
	}

	@Test
	public void testRemove() {
		LongRangeSet expected[][] = { { nullSet, nullSet, nullSet, nullSet, nullSet, nullSet }, { nullSet, nullSet, negativeSet, negativeSet, negativeSet, nullSet }, { zeroSet, nullSet, negativeSet, negativeSet, nonPositiveSet, nullSet }, { zeroSet, nullSet, nullSet, nullSet, zeroSet, nullSet }, { nonNegativeSet, positiveSet, positiveSet, nullSet, zeroSet, nullSet }, { positiveSet, positiveSet, positiveSet, nullSet, nullSet, nullSet }, { nonNegativeSet, positiveSet, nonZeroSet, negativeSet, nonPositiveSet, nullSet }, { positiveSet, positiveSet, nonZeroSet, negativeSet, negativeSet, nullSet } };
		for (int i = 0; i < sets.length; ++i) {
			for (int j = 0; j < ranges.length; ++j) {
				assertRemove(expected[i][j], sets[i], ranges[j]);
			}
		}
	}

	@Test
	public void testRemoveCleave1() {
		LongRangeSet orig = new LongRangeSet();
		orig.add(new LongRange(-1, true, 1, true));
		LongRangeSet expected = new LongRangeSet();
		expected.add(LongRange.ofValue(-1));
		expected.add(LongRange.ofValue(1));
		assertRemove(expected, orig, new LongRange(-1, false, 1, false));
	}

	@Test
	public void testRemoveChop2() {
		LongRangeSet orig = new LongRangeSet();
		orig.add(new LongRange(-1, true, 0, false));
		orig.add(new LongRange(0, false, 1, true));
		LongRangeSet expected = new LongRangeSet();
		expected.add(LongRange.ofValue(-1));
		expected.add(LongRange.ofValue(1));
		assertRemove(expected, orig, new LongRange(-1, false, 1, false));
	}

	@Test
	public void testRemoveChop2Delete1() {
		LongRangeSet orig = new LongRangeSet();
		orig.add(new LongRange(-2, true, -1, false));
		orig.add(new LongRange(-1, false, 1, false));
		orig.add(new LongRange(1, false, 2, true));
		LongRangeSet expected = new LongRangeSet();
		expected.add(LongRange.ofValue(-2));
		expected.add(LongRange.ofValue(2));
		assertRemove(expected, orig, new LongRange(-2, false, 2, false));
	}

	@Test
	public void testComplement() {
		assertComplement(infiniteSet, nullSet);
		assertComplement(nonNegativeSet, negativeSet);
		assertComplement(positiveSet, nonPositiveSet);
		assertComplement(nonZeroSet, zeroSet);
		assertComplement(negativeSet, nonNegativeSet);
		assertComplement(nonPositiveSet, positiveSet);
		assertComplement(zeroSet, nonZeroSet);
	}

}
