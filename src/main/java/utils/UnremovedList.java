
package utils;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This list is just an ArrayList optimized for the remove(0) function. Indeed,
 * when we do so, the object won't be removed. When the list has reached its max
 * capacity, then it removes every object which should have been removed
 * earlier.
 * 
 * @author Florian
 *
 */
public class UnremovedList<E> extends AbstractList<E> implements List<E> {

	// The number of elements to consider in this list
	private int size;
	// The first element not removed in the list. -1 if there is no removed elements
	private int firstToConsider;
	// We try to always have the length data at maxCapacity.
	private int maxCapacity;
	private final int DEFAULT_MAX_CAPACITY = 1000000;
	// Where the data is stored
	private E[] data;

	/**
	 * The constructor without parameter
	 */
	public UnremovedList() {
		this.size = 0;
		this.firstToConsider = 0;
		this.maxCapacity = DEFAULT_MAX_CAPACITY;
		this.data = (E[]) new Object[DEFAULT_MAX_CAPACITY];
	}

	/**
	 * Kind of a copy constructor but with a List instead of an UnremovedList
	 * 
	 * @param list
	 *            the object to copy
	 */
	public UnremovedList(List<E> list) {
		E[] listData = (E[]) list.toArray();
		this.size = listData.length;
		this.firstToConsider = 0;
		this.maxCapacity = DEFAULT_MAX_CAPACITY;
		this.data = (E[]) new Object[Math.max(listData.length, DEFAULT_MAX_CAPACITY)];
		System.arraycopy(listData, 0, this.data, 0, listData.length);
	}

	/**
	 * A copy constructor
	 * 
	 * @param urmL
	 *            the object to copy
	 */
	public UnremovedList(UnremovedList<E> urmL) {
		this.size = urmL.size;
		this.firstToConsider = 0;
		this.maxCapacity = urmL.maxCapacity;
		this.data = (E[]) new Object[Math.max(urmL.size, DEFAULT_MAX_CAPACITY)];
		System.arraycopy(urmL.data, urmL.firstToConsider, this.data, 0, urmL.size);
	}

	/**
	 * Guarantees that this list will have at least enough capacity to hold
	 * minCapacity elements.
	 *
	 * @param minCapacity
	 *            the minimum guaranteed capacity
	 * @param minCapacity
	 */
	public void ensureCapacityUp(int minCapacity) {
		if (firstToConsider + minCapacity > data.length) {
			E[] newData = (E[]) new Object[Math.max(maxCapacity, minCapacity)];
			System.arraycopy(data, firstToConsider, newData, 0, size);
			data = newData;
			firstToConsider = 0;
		}
	}

	/**
	 * If the capacity is too high compared to maxCapacity, this function cuts the
	 * data. This is to avoid memory leaks.
	 */
	public void ensureCapacityDown() {
		// To shift everything to the left, the length of data must be higher than
		// maxCapacity, data must have at least half of its values on the left to null
		// and the number of values not to null must be lower than maxCapacity
		if (data.length > maxCapacity && 2 * firstToConsider > maxCapacity && firstToConsider + size <= maxCapacity) {
			E[] newData = (E[]) new Object[this.maxCapacity];
			System.arraycopy(data, firstToConsider, newData, 0, size);
			data = newData;
			firstToConsider = 0;
		}
	}

	/**
	 * Returns the number of considered elements in this list.
	 *
	 * @return the list size
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * @param maxCapacity
	 *            the maxCapacity to set
	 */
	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
		ensureCapacityDown();
	}

	/**
	 * Checks if the list is empty
	 * 
	 * @return true if there are no considered elements
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns true if element is in the considered elements of this List.
	 * 
	 * @param e
	 *            the element whose inclusion in the List is being tested
	 * @return true if the list contains e
	 */
	@Override
	public boolean contains(Object e) {
		return indexOf(e) != -1;
	}

	/**
	 * Returns the lowest index at which element appears in the considered elements
	 * list, or -1 if it does not appear.
	 * 
	 * @param e
	 *            the element whose inclusion in the list is being tested
	 * @return the index where e was found
	 */
	@Override
	public int indexOf(Object e) {
		for (int i = firstToConsider; i < firstToConsider + size; i++)
			if (data[i].equals(e))
				return i - firstToConsider;
		return -1;
	}

	/**
	 * Returns the highest index at which element appears in the considered elements
	 * list, or -1 if it does not appear.
	 *
	 * @param e
	 *            the element whose inclusion in the list is being tested
	 * @return the index where e was found
	 */
	@Override
	public int lastIndexOf(Object e) {
		for (int i = firstToConsider + size - 1; i >= firstToConsider; i--)
			if (data[i].equals(e))
				return i - firstToConsider;
		return -1;
	}

	/**
	 * Retrieves the considered element at the user-supplied index in the elements
	 * to consider.
	 * 
	 * @param index
	 *            the index of the element we are fetching
	 * @return the considered element at the user-supplied index
	 */
	@Override
	public E get(int index) {
		checkBoundsExclusive(index);
		return data[firstToConsider + index];
	}

	/**
	 * Sets the element at the specified index in the elements to consider. The new
	 * element, e, can be an object of any type or null.
	 * 
	 * @param index
	 *            the index at which the element is being set
	 * @param e
	 *            the element to be set
	 * @return the element previously at the specified index
	 */
	@Override
	public E set(int index, E element) {
		E result = this.get(index);
		data[firstToConsider + index] = element;
		return result;
	}

	/**
	 * Appends the supplied element to the end of this list. The element, e, can be
	 * an object of any type or null.
	 * 
	 * @param e
	 *            the element to be appended to this list
	 * @return true, the add will always succeed
	 */
	@Override
	public boolean add(E element) {
		ensureCapacityUp(size + 1);

		data[firstToConsider + size++] = element;

		ensureCapacityDown();
		return true;
	}

	/**
	 * Adds the supplied element at the specified index in the element to consider,
	 * shifting all elements currently at that index or higher one to the right. The
	 * element, e, can be an object of any type or null. The index is the index of
	 * the considered elements.
	 *
	 * @param index
	 *            the index at which the element is being added
	 * @param e
	 *            the item being added
	 */
	@Override
	public void add(int index, E element) {
		checkBoundsInclusive(index);
		ensureCapacityUp(size + 1);

		if (index == 0 && firstToConsider > 0)
			data[--firstToConsider] = element;
		else {
			if (index != size)
				System.arraycopy(data, firstToConsider + index, data, firstToConsider + index + 1, size - index);
			data[firstToConsider + index] = element;
		}
		size++;
		ensureCapacityDown();
	}

	/**
	 * Removes the element at the user-supplied index in the elements to consider.
	 * 
	 * @param index
	 *            the index of the element to be removed
	 * @return the removed Object
	 * @throws IndexOutOfBoundsException
	 *             if index &lt; 0 || index &gt;= size()
	 */
	@Override
	public E remove(int index) {
		checkBoundsExclusive(index);
		E r = data[firstToConsider + index];

		if (index == 0) {
			data[firstToConsider++] = null;
			size--;
			ensureCapacityDown();
			return r;
		}

		if (index != --size)
			System.arraycopy(data, firstToConsider + index + 1, data, firstToConsider + index, size - index);

		// Help the garbage collector
		data[firstToConsider + size] = null;
		ensureCapacityDown();

		return r;
	}

	/**
	 * Removes the first occurrence of the specified element from this considered
	 * list, if it is present. If the considered list does not contain the element,
	 * it is unchanged. More formally, removes the element with the lowest index i
	 * such that (o==null ? get(i)==null : o.equals(get(i))) (if such an element
	 * exists). Returns true if this considered list contained the specified element
	 * (or equivalently, if this list changed as a result of the call).
	 * 
	 * @param o
	 *            element to be removed from this considered list, if present
	 * @return true if this considered list contained the specified element
	 */
	@Override
	public boolean remove(Object o) {
		if (size == 0)
			return false;

		if (data[firstToConsider].equals(o)) {
			data[firstToConsider++] = null;
			size--;
			ensureCapacityDown();
			return true;
		}

		for (int index = 0; index < size; index++) {
			if (data[firstToConsider + index].equals(o)) {
				if (index != --size)
					System.arraycopy(data, firstToConsider + index + 1, data, firstToConsider + index, size - index);

				// Help the garbage collector
				data[firstToConsider + size] = null;
				ensureCapacityDown();
				return true;
			}
		}

		return false;
	}

	/**
	 * Removes all elements in this list
	 */
	public void clear() {
		if (size > 0) {
			// To help the garbage collector
			Arrays.fill(data, 0, firstToConsider + size, null);
			firstToConsider = 0;
			size = 0;
			ensureCapacityDown();
		}
	}

	/**
	 * Add each element in the supplied Collection to this List. It is undefined
	 * what happens if you modify the list while this is taking place; for example,
	 * if the collection contains this list. c can contain objects of any type, as
	 * well as null values.
	 *
	 * @param collection
	 *            a Collection containing elements to be added to this List
	 * @return true if the list was modified, in other words collection is not empty
	 */
	public boolean addAll(Collection<? extends E> collection) {
		return addAll(size, collection);
	}

	/**
	 * Add all elements in the supplied collection, inserting them beginning at the
	 * specified index. c can contain objects of any type, as well as null values.
	 *
	 * @param index
	 *            the index at which the elements will be inserted
	 * @param collection
	 *            the Collection containing the elements to be inserted
	 * @return true if the list was modified, in other words collection is not empty
	 * @throws IndexOutOfBoundsException
	 *             if index &lt; 0 || index &gt; 0
	 * @throws NullPointerException
	 *             if collection is null
	 */
	public boolean addAll(int index, Collection<? extends E> collection) {
		checkBoundsInclusive(index);
		if (collection == null)
			throw new NullPointerException();

		Iterator<? extends E> it = collection.iterator();
		int csize = collection.size();

		if (csize + firstToConsider + size > data.length)
			ensureCapacityUp(size + csize);

		int end = index + csize;
		if (index != size)
			System.arraycopy(data, firstToConsider + index, data, firstToConsider + end, size - index);
		size += csize;

		for (; index < end; index++)
			data[index] = it.next();
		return csize > 0;
	}

	/**
	 * Checks that the index is in the range of existing elements (inclusive).
	 *
	 * @param index
	 *            the index to check
	 * @throws IndexOutOfBoundsException
	 *             if index &lt; 0 || index &gt; size()
	 */
	private void checkBoundsInclusive(int index) {
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
	}

	/**
	 * Checks that the index is in the range of existing elements (exclusive).
	 *
	 * @param index
	 *            the index to check
	 * @throws IndexOutOfBoundsException
	 *             if index &lt; 0 || index &gt;= size()
	 */
	private void checkBoundsExclusive(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
	}

	/**
	 * Returns an Object array containing all of the considered elements in this
	 * ArrayList. The array is independent of this list.
	 *
	 * @return an array representation of this list
	 */
	@Override
	public Object[] toArray() {
		E[] array = (E[]) new Object[size];
		System.arraycopy(data, firstToConsider, array, 0, size);
		return array;
	}

	/**
	 * Returns an Array whose component type is the runtime component type of the
	 * passed-in Array. The returned Array is populated with all of the elements in
	 * this ArrayList. If the passed-in Array is not large enough to store all of
	 * the elements in this List, a new Array will be created and returned; if the
	 * passed-in Array is <i>larger</i> than the size of this List, then size()
	 * index will be set to null.
	 *
	 * @param a
	 *            the passed-in Array
	 * @return an array representation of this list
	 * @throws ArrayStoreException
	 *             if the runtime type of a does not allow an element in this list
	 * @throws NullPointerException
	 *             if a is null
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		if (a.length < size)
			a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
		else if (a.length > size)
			a[size] = null;
		System.arraycopy(data, firstToConsider, a, 0, size);
		return a;
	}

	/**
	 * Returns true if this considered list contains all of the elements in the
	 * specified collection. This implementation iterates over the specified
	 * collection, checking each element returned by the iterator in turn to see if
	 * it's contained in this considered list. If all elements are so contained true
	 * is returned, otherwise false.
	 * 
	 * @param c
	 *            collection to be checked for containment in this collection
	 * @return true if this collection contains all of the elements in the specified
	 *         collection
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		Iterator<?> it = c.iterator();
		for (; it.hasNext();) {
			if (!this.contains(it.next()))
				return false;
		}
		return true;
	}

	/**
	 * Removes from this list all of its elements that are contained in the
	 * specified collection. This function is really low so it is not recommended to
	 * use it.
	 * 
	 * @param c
	 *            collection containing elements to be removed from this list
	 * @return true if this list changed as a result of the call
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		// The elements to remove are at false in retain and true otherwise
		List<Boolean> retain = new ArrayList<Boolean>(size);
		for (int i = 0; i < size; i++)
			retain.add(!c.contains(data[firstToConsider + i]));

		// Number of elements to retain
		int nbRetain = retain.stream().mapToInt(b -> b ? 1 : 0).sum();

		if (nbRetain < size) {
			E[] newData = (E[]) new Object[data.length];
			int newDataIndex = 0;
			for (int i = 0; i < size; i++) {
				if (retain.get(i))
					newData[newDataIndex++] = data[i];
			}
			data = newData;
			firstToConsider = 0;
			size = nbRetain;
			ensureCapacityDown();
		}

		return nbRetain < size;
	}

	/**
	 * Retains only the elements in this list that are contained in the specified
	 * collection. In other words, removes from this list all of its elements that
	 * are not contained in the specified collection.
	 * 
	 * @param c
	 *            collection containing elements to be retained in this list
	 * @return true if this list changed as a result of the call
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		// The elements to remove are at false in retain and true otherwise
		List<Boolean> retain = new ArrayList<Boolean>(size);
		for (int i = 0; i < size; i++)
			retain.add(c.contains(data[firstToConsider + i]));

		// Number of elements to retain
		int nbRetain = retain.stream().mapToInt(b -> b ? 1 : 0).sum();

		if (nbRetain < size) {
			E[] newData = (E[]) new Object[data.length];
			int newDataIndex = 0;
			for (int i = 0; i < size; i++) {
				if (retain.get(i))
					newData[newDataIndex++] = data[i];
			}
			data = newData;
			ensureCapacityDown();
		}

		return nbRetain < size;
	}

	/**
	 * Obtain an Iterator over this considered elements list, whose sequence is the
	 * list order. Also, this implementation is specified by Sun to be distinct from
	 * listIterator, although you could easily implement it as
	 * <code>return listIterator(0)</code>.
	 *
	 * @return an Iterator over the elements of this list, in order
	 */
	@Override
	public Iterator<E> iterator() {
		return new UnremovedListIterator();
	}

	public class UnremovedListIterator implements Iterator<E> {

		private int index = -1;

		/**
		 * Checks whether the iterator has a next element or not
		 * 
		 * @return true if a next element exists and false otherwise
		 */
		@Override
		public boolean hasNext() {
			return index < size - 1;
		}

		/**
		 * Gets the next element of the iterator
		 * 
		 * @return an object E which is the next element
		 */
		@Override
		public E next() {
			return data[firstToConsider + ++index];
		}

	}

	/**
	 * Obtain a ListIterator over this list, starting at the beginning. This
	 * implementation returns listIterator(0).
	 *
	 * @return a ListIterator over the elements of this list, in order, starting at
	 *         the beginning
	 */
	@Override
	public ListIterator<E> listIterator() {
		return new UnremovedListListIterator();
	}

	/**
	 * Obtain a ListIterator over this list, starting at a given position. A first
	 * call to next() would return the same as get(index), and a first call to
	 * previous() would return the same as get(index - 1).
	 *
	 * @param index
	 *            the position, between 0 and size() inclusive, to begin the
	 *            iteration from
	 * @return a ListIterator over the elements of this list, in order, starting at
	 *         index
	 */
	@Override
	public ListIterator<E> listIterator(int index) {
		return new UnremovedListListIterator(index);
	}

	public class UnremovedListListIterator implements ListIterator<E> {

		private int index;

		// Whether next() or previous() have already been called since the construction
		// of
		// the listIterator
		private boolean nextOrPrevious;

		/**
		 * The default ListIterator constructor
		 */
		public UnremovedListListIterator() {
			this.index = 0;
			nextOrPrevious = false;
		}

		/**
		 * The ListIterator constructor
		 * 
		 * @param index
		 *            the index of the beginning of the iterator in the considered list
		 * 
		 * @throws IndexOutOfBoundsException
		 *             if the index is not in the range 0 size()
		 */
		public UnremovedListListIterator(int index) {
			if (index < 0 || index > size)
				throw new IndexOutOfBoundsException("index must be in range 0 size()");

			this.index = index;
			nextOrPrevious = false;
		}

		/**
		 * Checks whether the iterator has a next element or not
		 * 
		 * @return true if a next element exists and false otherwise
		 */
		@Override
		public boolean hasNext() {
			return nextOrPrevious ? index < size - 1 : index < size;
		}

		/**
		 * Gets the next element of the iterator
		 * 
		 * @return an object E which is the next element
		 */
		@Override
		public E next() {
			if (nextOrPrevious)
				return data[firstToConsider + ++index];
			nextOrPrevious = true;
			return data[firstToConsider + index];
		}

		/**
		 * Checks whether the iterator has a previous element or not
		 * 
		 * @return true if a previous element exists and false otherwise
		 */
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}

		/**
		 * Gets the previous element of the iterator
		 * 
		 * @return an object E which is the previous element
		 */
		@Override
		public E previous() {
			nextOrPrevious = true;
			return data[firstToConsider + --index];
		}

		/**
		 * Returns the index of the element that would be returned by a subsequent call
		 * to next(). (Returns list size if the list iterator is at the end of the
		 * list.)
		 * 
		 * @return the index of the element that would be returned by a subsequent call
		 *         to next, or list size if the list iterator is at the end of the list
		 */
		@Override
		public int nextIndex() {
			return nextOrPrevious ? index + 1 : index;
		}

		/**
		 * Returns the index of the element that would be returned by a subsequent call
		 * to previous(). (Returns -1 if the list iterator is at the beginning of the
		 * list.)
		 * 
		 * @return the index of the element that would be returned by a subsequent call
		 *         to previous, or -1 if the list iterator is at the beginning of the
		 *         list
		 */
		@Override
		public int previousIndex() {
			return index - 1;
		}

		/**
		 * Removes from the list the last element that was returned by next() or
		 * previous(). After that, the list is shifted to the left and the iterator
		 * points thus to the next element. If the iterator was at the end of the list,
		 * then the iterator stays at the end of the list.
		 * 
		 * @throws IllegalStateException
		 *             if neither next() nor previous() have been called since the
		 *             construction of the listIterator
		 */
		@Override
		public void remove() {
			if (!nextOrPrevious)
				throw new IllegalStateException(
						"You have to call either next() or previous() at least once before calling remove()");

			UnremovedList.this.remove(index);
			if (index == size)
				index--;
		}

		/**
		 * Replaces the last element returned by next() or previous() with the specified
		 * element. After that, the iterator points thus to the set element.
		 * 
		 * @param element
		 *            the element with which to replace the last element returned by
		 *            next or previous
		 * 
		 * @throws IllegalStateException
		 *             if neither next() nor previous() have been called since the
		 *             construction of the listIterator
		 */
		@Override
		public void set(E element) {
			if (!nextOrPrevious)
				throw new IllegalStateException(
						"You have to call either next() or previous() at least once before calling set()");

			data[firstToConsider + index] = element;
		}

		/**
		 * Adds before the last element returned by next() or previous() the specified
		 * element. After that, the list is shifted to the right and the iterator points
		 * thus to the added element.
		 * 
		 * @param element
		 *            the element with which to replace the last element returned by
		 *            next or previous
		 * 
		 * @throws IllegalStateException
		 *             if neither next() nor previous() have been called since the
		 *             construction of the listIterator
		 */
		@Override
		public void add(E element) {
			if (!nextOrPrevious)
				throw new IllegalStateException(
						"You have to call either next() or previous() at least once before calling add()");

			UnremovedList.this.add(element);
		}

	}

}
