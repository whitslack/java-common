/*
 * Created on Dec 2, 2017
 */
package com.mattwhitlock.common.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.mattwhitlock.common.AmbiguousMethodException;
import com.mattwhitlock.common.ClassUtil;

/**
 * @author Matt Whitlock
 */
class ClassUtilTest {

	public static class TestMethods {

		public static void noArgs() {
		}

		public static void noArgsOrVarArgsWantFormer() {
		}

		public static void noArgsOrVarArgsWantFormer(Object... args) {
			fail("should not be called");
		}

		public static void noArgsOrVarArgsWantLatter() {
			fail("should not be called");
		}

		public static void noArgsOrVarArgsWantLatter(Object... args) {
		}

		public static void oneArgOrVarArgsWantFormer(Object arg) {
		}

		public static void oneArgOrVarArgsWantFormer(Object... args) {
			fail("should not be called");
		}

		public static void oneArgOrVarArgsWantLatter(Object arg) {
			fail("should not be called");
		}

		public static void oneArgOrVarArgsWantLatter(Object... args) {
		}

		public static void varArgsOrOneArgAndVarArgsWantFormer(Object... args) {
		}

		public static void varArgsOrOneArgAndVarArgsWantFormer(Object arg, Object... args) {
			fail("should not be called");
		}

		public static void varArgsOrOneArgAndVarArgsWantNeither(Object... args) {
			fail("should not be called");
		}

		public static void varArgsOrOneArgAndVarArgsWantNeither(Object arg, Object... args) {
			fail("should not be called");
		}

		public static void twoArgsOrOneArgAndVarArgsWantFormer(Object arg0, Object arg1) {
		}

		public static void twoArgsOrOneArgAndVarArgsWantFormer(Object arg, Object... args) {
			fail("should not be called");
		}

		public static void twoArgsOrOneArgAndVarArgsWantLatter(Object arg0, Object arg1) {
			fail("should not be called");
		}

		public static void twoArgsOrOneArgAndVarArgsWantLatter(Object arg, Object... args) {
		}

		public static void twoArgsOrOneArgAndVarArgsWantNeither(Object arg0, Object arg1) {
			fail("should not be called");
		}

		public static void twoArgsOrOneArgAndVarArgsWantNeither(Object arg, Object... args) {
			fail("should not be called");
		}

		public static void intArg(int arg) {
		}

		public static void longArg(long arg) {
		}

		public static void intArrayArg(int[] arg) {
		}

		public static void longObjArg(Long arg) {
		}

		public static void intOrLongArgWantFormer(int arg) {
		}

		public static void intOrLongArgWantFormer(long arg) {
			fail("should not be called");
		}

		public static void intOrLongArgWantLatter(int arg) {
			fail("should not be called");
		}

		public static void intOrLongArgWantLatter(long arg) {
		}

		public static void intOrLongArgWantNeither(int arg) {
			fail("should not be called");
		}

		public static void intOrLongArgWantNeither(long arg) {
			fail("should not be called");
		}

		public static void longFloatOrFloatLongWantFormer(long arg0, float arg1) {
		}

		public static void longFloatOrFloatLongWantFormer(float arg0, long arg1) {
			fail("should not be called");
		}

		public static void longFloatOrFloatLongWantLatter(long arg0, float arg1) {
			fail("should not be called");
		}

		public static void longFloatOrFloatLongWantLatter(float arg0, long arg1) {
		}

		public static void longFloatOrFloatLongWantNeither(long arg0, float arg1) {
			fail("should not be called");
		}

		public static void longFloatOrFloatLongWantNeither(float arg0, long arg1) {
			fail("should not be called");
		}

		public static void longVarArgsOrOneIntArgAndLongVarArgsWantFormer(long... args) {
		}

		public static void longVarArgsOrOneIntArgAndLongVarArgsWantFormer(int arg, long... args) {
			fail("should not be called");
		}

		public static void longVarArgsOrOneIntArgAndLongVarArgsWantLatter(long... args) {
			fail("should not be called");
		}

		public static void longVarArgsOrOneIntArgAndLongVarArgsWantLatter(int arg, long... args) {
		}

		public static void longVarArgsOrFloatVarArgsWantFormer(long... args) {
		}

		public static void longVarArgsOrFloatVarArgsWantFormer(float... args) {
			fail("should not be called");
		}

		public static void longVarArgsOrFloatVarArgsWantLatter(long... args) {
			fail("should not be called");
		}

		public static void longVarArgsOrFloatVarArgsWantLatter(float... args) {
		}

		public static void longVarArgsOrFloatVarArgsWantNeither(long... args) {
			fail("should not be called");
		}

		public static void longVarArgsOrFloatVarArgsWantNeither(float... args) {
			fail("should not be called");
		}

	}

	@Test
	void testInvokeNoArgs() throws Exception {
		TestMethods.noArgs();
		ClassUtil.invokeStaticMethod(TestMethods.class, "noArgs");
	}

	@Test
	void testInvokeNoArgsOrVarArgsWithNoArgs() throws Exception {
		TestMethods.noArgsOrVarArgsWantFormer();
		ClassUtil.invokeStaticMethod(TestMethods.class, "noArgsOrVarArgsWantFormer");
	}

	@Test
	void testInvokeNoArgsOrVarArgsWithOneArg() throws Exception {
		TestMethods.noArgsOrVarArgsWantLatter(42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "noArgsOrVarArgsWantLatter", 42);
	}

	@Test
	void testInvokeOneArgOrVarArgsWithNoArgs() throws Exception {
		TestMethods.oneArgOrVarArgsWantLatter();
		ClassUtil.invokeStaticMethod(TestMethods.class, "oneArgOrVarArgsWantLatter");
	}

	@Test
	void testInvokeOneArgOrVarArgsWithOneArg() throws Exception {
		TestMethods.oneArgOrVarArgsWantFormer(42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "oneArgOrVarArgsWantFormer", 42);
	}

	@Test
	void testInvokeOneArgOrVarArgsWithTwoArgs() throws Exception {
		TestMethods.oneArgOrVarArgsWantLatter(42, 69);
		ClassUtil.invokeStaticMethod(TestMethods.class, "oneArgOrVarArgsWantLatter", 42, 69);
	}

	@Test
	void testInvokeVarArgsOrOneArgAndVarArgsWithNoArgs() throws Exception {
		TestMethods.varArgsOrOneArgAndVarArgsWantFormer();
		ClassUtil.invokeStaticMethod(TestMethods.class, "varArgsOrOneArgAndVarArgsWantFormer");
	}

	@Test
	void testInvokeVarArgsOrOneArgAndVarArgsWithOneArg() throws Exception {
		// TestMethods.varArgsOrOneArgAndVarArgsWantNeither(42);
		assertThrows(AmbiguousMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "varArgsOrOneArgAndVarArgsWantNeither", 42);
		});
	}

	@Test
	void testInvokeVarArgsOrOneArgAndVarArgsWithTwoArgs() throws Exception {
		// TestMethods.varArgsOrOneArgAndVarArgsWantNeither(42, 69);
		assertThrows(AmbiguousMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "varArgsOrOneArgAndVarArgsWantNeither", 42, 69);
		});
	}

	@Test
	void testInvokeTwoArgsOrOneArgAndVarArgsWithNoArgs() throws Exception {
		// TestMethods.twoArgsOrOneArgAndVarArgsWantNeither();
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "twoArgsOrOneArgAndVarArgsWantNeither");
		});
	}

	@Test
	void testInvokeTwoArgsOrOneArgAndVarArgsWithOneArg() throws Exception {
		TestMethods.twoArgsOrOneArgAndVarArgsWantLatter(42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "twoArgsOrOneArgAndVarArgsWantLatter", 42);
	}

	@Test
	void testInvokeTwoArgsOrOneArgAndVarArgsWithTwoArgs() throws Exception {
		TestMethods.twoArgsOrOneArgAndVarArgsWantFormer(42, 69);
		ClassUtil.invokeStaticMethod(TestMethods.class, "twoArgsOrOneArgAndVarArgsWantFormer", 42, 69);
	}

	@Test
	void testInvokeTwoArgsOrOneArgAndVarArgsWithThreeArgs() throws Exception {
		TestMethods.twoArgsOrOneArgAndVarArgsWantLatter(42, 69, 0);
		ClassUtil.invokeStaticMethod(TestMethods.class, "twoArgsOrOneArgAndVarArgsWantLatter", 42, 69, 0);
	}

	@Test
	void testInvokeIntArgWithInt() throws Exception {
		TestMethods.intArg(42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "intArg", 42);
	}

	@Test
	void testInvokeIntArgWithLong() throws Exception {
		// TestMethods.intArg(42L);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "intArg", 42L);
		});
	}

	@Test
	void testInvokeIntArgWithNull() throws Exception {
		// TestMethods.intArg(null);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "intArg", (Object) null);
		});
	}

	@Test
	void testInvokeLongArgWithInt() throws Exception {
		TestMethods.longArg(42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longArg", 42);
	}

	@Test
	void testInvokeLongArgWithLong() throws Exception {
		TestMethods.longArg(42L);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longArg", 42L);
	}

	@Test
	void testInvokeLongArgWithNull() throws Exception {
		// TestMethods.longArg(null);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longArg", (Object) null);
		});
	}

	@Test
	void testInvokeIntArrayArgWithNoArg() throws Exception {
		// TestMethods.intArrayArg();
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "intArrayArg");
		});
	}

	@Test
	void testInvokeIntArrayArgWithInt() throws Exception {
		// TestMethods.intArrayArg(42);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "intArrayArg", 42);
		});
	}

	@Test
	void testInvokeIntArrayArgWithNull() throws Exception {
		TestMethods.intArrayArg(null);
		ClassUtil.invokeStaticMethod(TestMethods.class, "intArrayArg", (Object) null);
	}

	@Test
	void testInvokeIntArrayArgWithIntInt() throws Exception {
		// TestMethods.intArrayArg(42, 69);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "intArrayArg", 42, 69);
		});
	}

	@Test
	void testInvokeIntArrayArgWithIntArray() throws Exception {
		TestMethods.intArrayArg(new int[] { 42, 69 });
		ClassUtil.invokeStaticMethod(TestMethods.class, "intArrayArg", new int[] { 42, 69 });
	}

	@Test
	void testInvokeLongObjArgWithInt() throws Exception {
		// TestMethods.longObjArg(42);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longObjArg", 42);
		});
	}

	@Test
	void testInvokeLongObjArgWithLong() throws Exception {
		TestMethods.longObjArg(42L);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longObjArg", 42L);
	}

	@Test
	void testInvokeIntOrLongArgWithInt() throws Exception {
		TestMethods.intOrLongArgWantFormer(42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "intOrLongArgWantFormer", 42);
	}

	@Test
	void testInvokeIntOrLongArgWithLong() throws Exception {
		TestMethods.intOrLongArgWantLatter(42L);
		ClassUtil.invokeStaticMethod(TestMethods.class, "intOrLongArgWantLatter", 42L);
	}

	@Test
	void testInvokeIntOrLongArgWithShort() throws Exception {
		TestMethods.intOrLongArgWantFormer((short) 42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "intOrLongArgWantFormer", (short) 42);
	}

	@Test
	void testInvokeIntOrLongArgWithFloat() throws Exception {
		// TestMethods.intOrLongArgWantNeither(42f);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "intOrLongArgWantNeither", 42f);
		});
	}

	@Test
	void testInvokeLongFloatOrFloatLongWithIntInt() throws Exception {
		// TestMethods.longFloatOrFloatLongWantNeither(42, 42);
		assertThrows(AmbiguousMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longFloatOrFloatLongWantNeither", 42, 42);
		});
	}

	@Test
	void testInvokeLongFloatOrFloatLongWithIntLong() throws Exception {
		// TestMethods.longFloatOrFloatLongWantNeither(42, 42L);
		assertThrows(AmbiguousMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longFloatOrFloatLongWantNeither", 42, 42L);
		});
	}

	@Test
	void testInvokeLongFloatOrFloatLongWithIntFloat() throws Exception {
		TestMethods.longFloatOrFloatLongWantFormer(42, 42f);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longFloatOrFloatLongWantFormer", 42, 42f);
	}

	@Test
	void testInvokeLongFloatOrFloatLongWithLongInt() throws Exception {
		// TestMethods.longFloatOrFloatLongWantNeither(42L, 42);
		assertThrows(AmbiguousMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longFloatOrFloatLongWantNeither", 42L, 42);
		});
	}

	@Test
	void testInvokeLongFloatOrFloatLongWithLongLong() throws Exception {
		// TestMethods.longFloatOrFloatLongWantNeither(42L, 42L);
		assertThrows(AmbiguousMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longFloatOrFloatLongWantNeither", 42L, 42L);
		});
	}

	@Test
	void testInvokeLongFloatOrFloatLongWithLongFloat() throws Exception {
		TestMethods.longFloatOrFloatLongWantFormer(42L, 42f);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longFloatOrFloatLongWantFormer", 42L, 42f);
	}

	@Test
	void testInvokeLongFloatOrFloatLongWithFloatInt() throws Exception {
		TestMethods.longFloatOrFloatLongWantLatter(42f, 42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longFloatOrFloatLongWantLatter", 42f, 42);
	}

	@Test
	void testInvokeLongFloatOrFloatLongWithFloatLong() throws Exception {
		TestMethods.longFloatOrFloatLongWantLatter(42f, 42L);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longFloatOrFloatLongWantLatter", 42f, 42L);
	}

	@Test
	void testInvokeLongFloatOrFloatLongWithFloatFloat() throws Exception {
		// TestMethods.longFloatOrFloatLongWantNeither(42f, 42f);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longFloatOrFloatLongWantNeither", 42f, 42f);
		});
	}

	@Test
	void testInvokeLongVarArgsOrOneIntArgAndLongVarArgsWithNoArgs() throws Exception {
		TestMethods.longVarArgsOrOneIntArgAndLongVarArgsWantFormer();
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrOneIntArgAndLongVarArgsWantFormer");
	}

	@Test
	void testInvokeLongVarArgsOrOneIntArgAndLongVarArgsWithInt() throws Exception {
		TestMethods.longVarArgsOrOneIntArgAndLongVarArgsWantLatter(42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrOneIntArgAndLongVarArgsWantLatter", 42);
	}

	@Test
	void testInvokeLongVarArgsOrOneIntArgAndLongVarArgsWithLong() throws Exception {
		TestMethods.longVarArgsOrOneIntArgAndLongVarArgsWantFormer(42L);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrOneIntArgAndLongVarArgsWantFormer", 42L);
	}

	@Test
	void testInvokeLongVarArgsOrOneIntArgAndLongVarArgsWithNull() throws Exception {
		TestMethods.longVarArgsOrOneIntArgAndLongVarArgsWantFormer(null);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrOneIntArgAndLongVarArgsWantFormer", (Object) null);
	}

	@Test
	void testInvokeLongVarArgsOrOneIntArgAndLongVarArgsWithIntInt() throws Exception {
		TestMethods.longVarArgsOrOneIntArgAndLongVarArgsWantLatter(42, 42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrOneIntArgAndLongVarArgsWantLatter", 42, 42);
	}

	@Test
	void testInvokeLongVarArgsOrOneIntArgAndLongVarArgsWithIntLong() throws Exception {
		TestMethods.longVarArgsOrOneIntArgAndLongVarArgsWantLatter(42, 42L);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrOneIntArgAndLongVarArgsWantLatter", 42, 42L);
	}

	@Test
	void testInvokeLongVarArgsOrOneIntArgAndLongVarArgsWithIntNull() throws Exception {
		TestMethods.longVarArgsOrOneIntArgAndLongVarArgsWantLatter(42, null);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrOneIntArgAndLongVarArgsWantLatter", 42, null);
	}

	@Test
	void testInvokeLongVarArgsOrOneIntArgAndLongVarArgsWithLongInt() throws Exception {
		TestMethods.longVarArgsOrOneIntArgAndLongVarArgsWantFormer(42L, 42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrOneIntArgAndLongVarArgsWantFormer", 42L, 42);
	}

	@Test
	void testInvokeLongVarArgsOrOneIntArgAndLongVarArgsWithLongLong() throws Exception {
		TestMethods.longVarArgsOrOneIntArgAndLongVarArgsWantFormer(42L, 42L);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrOneIntArgAndLongVarArgsWantFormer", 42L, 42L);
	}

	@Test
	void testInvokeLongVarArgsOrOneIntArgAndLongVarArgsWithLongNull() throws Exception {
		// TestMethods.longVarArgsOrOneIntArgAndLongVarArgsWantNeither(42L, null);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrOneIntArgAndLongVarArgsWantNeither", 42L, null);
		});
	}

	@Test
	void testInvokeLongVarArgsOrOneIntArgAndLongVarArgsWithNullInt() throws Exception {
		// TestMethods.longVarArgsOrOneIntArgAndLongVarArgsWantNeither(null, 42);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrOneIntArgAndLongVarArgsWantNeither", null, 42);
		});
	}

	@Test
	void testInvokeLongVarArgsOrOneIntArgAndLongVarArgsWithNullLong() throws Exception {
		// TestMethods.longVarArgsOrOneIntArgAndLongVarArgsWantNeither(null, 42L);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrOneIntArgAndLongVarArgsWantNeither", null, 42L);
		});
	}

	@Test
	void testInvokeLongVarArgsOrOneIntArgAndLongVarArgsWithNullNull() throws Exception {
		// TestMethods.longVarArgsOrOneIntArgAndLongVarArgsWantNeither(null, null);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrOneIntArgAndLongVarArgsWantNeither", null, null);
		});
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithNoArgs() throws Exception {
		TestMethods.longVarArgsOrFloatVarArgsWantFormer();
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantFormer");
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithNullArg() throws Exception {
		// TestMethods.longVarArgsOrFloatVarArgsWantNeither(null);
		assertThrows(AmbiguousMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantNeither", (Object) null);
		});
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithInt() throws Exception {
		TestMethods.longVarArgsOrFloatVarArgsWantFormer(42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantFormer", 42);
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithLong() throws Exception {
		TestMethods.longVarArgsOrFloatVarArgsWantFormer(42L);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantFormer", 42L);
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithFloat() throws Exception {
		TestMethods.longVarArgsOrFloatVarArgsWantLatter(42f);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantLatter", 42f);
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithNull() throws Exception {
		// TestMethods.longVarArgsOrFloatVarArgsWantNeither(null);
		assertThrows(AmbiguousMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantNeither", (Object) null);
		});
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithIntInt() throws Exception {
		TestMethods.longVarArgsOrFloatVarArgsWantFormer(42, 42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantFormer", 42, 42);
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithIntLong() throws Exception {
		TestMethods.longVarArgsOrFloatVarArgsWantFormer(42, 42L);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantFormer", 42, 42L);
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithIntFloat() throws Exception {
		TestMethods.longVarArgsOrFloatVarArgsWantLatter(42, 42f);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantLatter", 42, 42f);
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithIntNull() throws Exception {
		// TestMethods.longVarArgsOrFloatVarArgsWantNeither(42, null);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantNeither", 42, null);
		});
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithLongInt() throws Exception {
		TestMethods.longVarArgsOrFloatVarArgsWantFormer(42L, 42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantFormer", 42L, 42);
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithLongLong() throws Exception {
		TestMethods.longVarArgsOrFloatVarArgsWantFormer(42L, 42L);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantFormer", 42L, 42L);
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithLongFloat() throws Exception {
		TestMethods.longVarArgsOrFloatVarArgsWantLatter(42L, 42f);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantLatter", 42L, 42f);
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithLongNull() throws Exception {
		// TestMethods.longVarArgsOrFloatVarArgsWantNeither(42L, null);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantNeither", 42L, null);
		});
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithFloatInt() throws Exception {
		TestMethods.longVarArgsOrFloatVarArgsWantLatter(42f, 42);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantLatter", 42f, 42);
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithFloatLong() throws Exception {
		TestMethods.longVarArgsOrFloatVarArgsWantLatter(42f, 42L);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantLatter", 42f, 42L);
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithFloatFloat() throws Exception {
		TestMethods.longVarArgsOrFloatVarArgsWantLatter(42f, 42f);
		ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantLatter", 42f, 42f);
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithFloatNull() throws Exception {
		// TestMethods.longVarArgsOrFloatVarArgsWantNeither(42f, null);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantNeither", 42f, null);
		});
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithNullInt() throws Exception {
		// TestMethods.longVarArgsOrFloatVarArgsWantNeither(null, 42);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantNeither", null, 42);
		});
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithNullLong() throws Exception {
		// TestMethods.longVarArgsOrFloatVarArgsWantNeither(null, 42L);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantNeither", null, 42L);
		});
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithNullFloat() throws Exception {
		// TestMethods.longVarArgsOrFloatVarArgsWantNeither(null, 42f);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantNeither", null, 42f);
		});
	}

	@Test
	void testInvokeLongVarArgsOrFloatVarArgsWithNullNull() throws Exception {
		// TestMethods.longVarArgsOrFloatVarArgsWantNeither(null, null);
		assertThrows(NoSuchMethodException.class, () -> {
			ClassUtil.invokeStaticMethod(TestMethods.class, "longVarArgsOrFloatVarArgsWantNeither", null, null);
		});
	}

}
