package utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class UnremovedListTest {

	int i0;
	int i1;
	int i2;
	int i3;
	int i4;
	int i5;
	int i6;
	int i7;
	int i8;
	int i9;

	List<Integer> L;

	UnremovedList<Integer> urmL0;
	UnremovedList<Integer> urmL1;
	UnremovedList<Integer> urmL2;
	UnremovedList<Integer> urmL3;
	UnremovedList<Integer> urmL4;

	@Before
	public void setUp() throws Exception {
		i0 = 0;
		i1 = 1;
		i2 = 31;
		i3 = -5;
		i4 = 5000000;
		i5 = 0;
		i6 = 1;
		i7 = 0;
		i8 = -1;
		i9 = Integer.MAX_VALUE;

		L = new ArrayList<Integer>(5);
		L.add(i0);
		L.add(i1);
		L.add(i2);

		urmL0 = new UnremovedList<Integer>(L);
		urmL1 = new UnremovedList<Integer>(urmL0);
		urmL2 = new UnremovedList<Integer>();
		urmL3 = new UnremovedList<Integer>();
		urmL4 = new UnremovedList<Integer>();
	}

	@Test
	public final void testUnremovedListListOfE() {
		urmL0 = new UnremovedList<Integer>(L);

		assertEquals(3, urmL0.size());
		for (int i = 0; i < 3; i++)
			assertEquals(L.get(0), urmL0.get(0));
	}

	@Test
	public final void testUnremovedListUnremovedListOfE() {
		urmL1 = new UnremovedList<Integer>(urmL0);

		assertEquals(3, urmL1.size());
		for (int i = 0; i < 3; i++)
			assertEquals(urmL0.get(0), urmL0.get(0));
	}

	@Test
	public final void testSize() {
		assertEquals(3, urmL0.size());
		assertEquals(3, urmL1.size());
		assertEquals(0, urmL4.size());

		urmL0.add(i8);
		urmL1.remove(1);
		urmL4.add(i9);

		assertEquals(4, urmL0.size());
		assertEquals(2, urmL1.size());
		assertEquals(1, urmL4.size());
	}

	@Test
	public final void testIsEmpty() {
		assertTrue(urmL4.isEmpty());
		assertFalse(urmL1.isEmpty());
	}

	@Test
	public final void testClear() {
		assertFalse(urmL0.isEmpty());

		urmL0.clear();

		assertTrue(urmL0.isEmpty());
	}

	@Test
	public final void testContainsObject() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testIndexOfObject() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testLastIndexOfObject() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetIntE() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testAddE() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testAddIntE() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRemoveInt() {
		urmL0.remove(0);
		assertEquals(2, urmL0.size());

		urmL0.remove(1);
		assertEquals(1, urmL0.size());
		assertEquals((long) i1, (long) urmL0.get(0));
	}

	@Test
	public final void testRemoveObject() {
		urmL0.remove((Object) i0);
		assertEquals(2, urmL0.size());

		urmL0.remove((Object) i2);
		assertEquals(1, urmL0.size());
		assertEquals((long) i1, (long) urmL0.get(0));
	}

	@Test
	public final void testAddAllCollectionOfQextendsE() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testAddAllIntCollectionOfQextendsE() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToArray() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToArrayTArray() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testContainsAllCollectionOfQ() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRemoveAllCollectionOfQ() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRetainAllCollectionOfQ() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testIterator() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testListIterator() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testListIteratorInt() {
		fail("Not yet implemented"); // TODO
	}

}
