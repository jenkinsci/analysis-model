package edu.hm.hafner.analysis;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.stream.StreamSupport;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * {@link List} of {@link LineRange} that stores values more efficiently at runtime.
 * <p>
 * This class thinks of {@link LineRange} as two integers (start and end-start), hence a list of {@link LineRange}
 * becomes a list of integers. The class then stores those integers in {@code byte[]}. Each number is packed to UTF-8
 * like variable length format. To store a long value N, we first split into 7 bit chunk, and store each 7 bit chunk as
 * a byte, in the little endian order. The last byte gets its 8th bit set to indicate that that's the last byte. Thus in
 * this format, 0x0 gets stored as 0x80, 0x1234 gets stored as {0x34,0xA4(0x24|0x80)}.
 * <p>
 * This variable length mode stores data most efficiently, since most line numbers are small. Access characteristic gets
 * close to that of {@link LinkedList}, since we can only traverse this packed byte[] from the start or from the end.
 *
 * @author Kohsuke Kawaguchi
 */
public class LineRangeList extends AbstractList<LineRange> implements Serializable {
    private static final long serialVersionUID = -1123973098942984623L;
    private static final int DEFAULT_CAPACITY = 16;
    private static final boolean SEQUENTIAL = false;

    /** Encoded bits. */
    private byte[] data;
    /** Number of bytes in {@link #data} that's already used. This is not {@link List#size()}. */
    private int len;

    /**
     * Creates an empty {@link LineRangeList}. It uses a capacity of {@link LineRangeList#DEFAULT_CAPACITY}.
     */
    public LineRangeList() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates an empty {@link LineRangeList} with the specified capacity.
     *
     * @param capacity
     *         the initial capacity of the list
     */
    public LineRangeList(final int capacity) {
        super();

        data = new byte[capacity];
        len = 0;
    }

    /**
     * Creates a new {@link LineRangeList} with the specified elements.
     *
     * @param copy
     *         the initial elements
     */
    public LineRangeList(final Collection<LineRange> copy) {
        this(copy.size() * 4); // guess

        addAll(copy);
    }

    /**
     * Creates a new {@link LineRangeList} with the specified elements.
     *
     * @param initialElements
     *         the initial elements
     */
    public LineRangeList(final LineRange... initialElements) {
        this(initialElements.length * 4); // guess

        addAll(Arrays.asList(initialElements));
    }

    @Override
    public final boolean addAll(final Collection<? extends LineRange> c) {
        return super.addAll(c);
    }

    /**
     * Appends all of the elements in the specified collection to the end of this list, in the order that they are
     * returned by the specified collection's iterator (optional operation).  The behavior of this operation is
     * undefined if the specified collection is modified while the operation is in progress.  (Note that this will occur
     * if the specified collection is this list, and it's nonempty.)
     *
     * @param ranges
     *         collection containing elements to be added to this list
     *
     * @return {@code true} if this list changed as a result of the call
     * @throws NullPointerException
     *         if the specified collection contains one or more null elements and this list does not permit null
     *         elements, or if the specified collection is null
     * @throws IllegalArgumentException
     *         if some property of an element of the specified collection prevents it from being added to this list
     * @see #add(Object)
     */
    public final boolean addAll(final Iterable<? extends LineRange> ranges) {
        return StreamSupport.stream(ranges.spliterator(), SEQUENTIAL)
                .map(this::add)
                .reduce(Boolean::logicalOr)
                .orElse(false);
    }

    /**
     * Makes sure that the buffer has capability to store N bytes.
     *
     * @param n
     *         capacity
     */
    private void ensure(final int n) {
        if (data.length < n) {
            byte[] buf = new byte[Math.max(n, data.length * 2)];
            System.arraycopy(data, 0, buf, 0, len);
            data = buf;
        }
    }

    @Override
    public boolean contains(final Object o) {
        if (o instanceof LineRange) {
            LineRange lr = (LineRange) o;

            for (Cursor c = new Cursor(); c.hasNext();) {
                if (c.compare(lr)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public LineRange get(final int index) {
        return new Cursor().skip(index).next();
    }

    @Override
    public int size() {
        return new Cursor().count();
    }

    @Override
    public LineRange set(final int index, final LineRange element) {
        return new Cursor().skip(index).rewrite(element);
    }

    @Override
    public void add(final int index, final LineRange element) {
        new Cursor().skip(index).add(element);
    }

    @Override
    public final boolean add(final LineRange lr) {
        new Cursor(len).add(lr);
        return true;
    }

    @Override
    public LineRange remove(final int index) {
        return new Cursor().skip(index).delete();
    }

    @Override
    public void clear() {
        len = 0;
    }

    @Override
    public Iterator<LineRange> iterator() {
        return new Cursor();
    }

    @Override
    public ListIterator<LineRange> listIterator() {
        return new Cursor();
    }

    @Override
    public ListIterator<LineRange> listIterator(final int index) {
        return new Cursor().skip(index);
    }

    /**
     * Minimizes the memory waste by throwing away excess capacity.
     */
    public void trim() {
        if (len != data.length) {
            byte[] small = new byte[len];
            System.arraycopy(data, 0, small, 0, len);
            data = small;
        }
    }

    /**
     * Navigates through the ranges and performs the conversion to and from {@link LineRange}.
     */
    private class Cursor implements ListIterator<LineRange> {
        private int position;

        Cursor(final int position) {
            this.position = position;
        }

        Cursor() {
            this(0);
        }

        /**
         * Does the opposite of {@link #read()} and skips back one int.
         */
        private void prev() {
            if (position == 0) {
                throw new NoSuchElementException("Cursor is a the beginning.");
            }
            do {
                position--;
            } while (position > 0 && (data[position - 1] & 0x80) == 0);
        }

        /**
         * Reads the {@link LineRange} object the cursor is pointing at.
         *
         * @return the current element
         */
        @Override @SuppressFBWarnings(value = "IT_NO_SUCH_ELEMENT", justification = "thrown in read()")
        public LineRange next() {
            int s = read();
            int d = read();
            return new LineRange(s, s + d);
        }

        @Override
        public LineRange previous() {
            prev();
            prev();
            return copy().next();
        }

        /**
         * Removes the last returned value.
         */
        @Override
        public void remove() {
            prev();
            prev();
            delete();
        }

        @Override
        public boolean hasNext() {
            return position < len;
        }

        @Override
        public boolean hasPrevious() {
            return position > 0;
        }

        /**
         * Reads the current variable-length encoded int value under the cursor, and moves the cursor ahead.
         *
         * @return the current value
         * @throws NoSuchElementException if encoded buffer contains no more element
         */
        private int read() throws NoSuchElementException {
            if (len <= position) {
                throw new NoSuchElementException("Position " + position + " is >= length " + len);
            }

            int i = 0;
            int v = 0;
            do {
                v += (data[position] & 0x7F) << (i++ * 7);
            } while ((data[position++] & 0x80) == 0);
            return v;
        }

        private void write(final int index) {
            int i = index;
            boolean last;
            do {
                last = i < 0x80;
                data[position++] = (byte) ((i & 0x7F) | (last ? 0x80 : 0));
                i /= 0x80;
            } while (!last);
        }

        private void write(final LineRange r) {
            write(r.getStart());
            write(r.getEnd() - r.getStart());
        }

        /**
         * Reads the current value at the cursor and compares it.
         *
         * @param other
         *         the line range to compare with
         *
         * @return {@code true} if the read value is equal to the specified range
         */
        public boolean compare(final LineRange other) {
            int s = read();
            int d = read();
            return other.getStart() == s && other.getEnd() == s + d;
        }

        /**
         * Skips forward and gets the pointer to N-th element.
         *
         * @param n
         *         number of elements to skip
         *
         * @return this cursor
         */
        private Cursor skip(final int n) {
            int i = n;
            for (; i > 0; i--) {
                read();
                read();
            }
            return this;
        }

        /**
         * Counts the # of elements from the current cursor position to the end.
         *
         * @return number of elements
         */
        private int count() {
            int n = 0;
            while (position < len) {
                read();
                read();
                n++;
            }
            return n;
        }

        @Override
        public int nextIndex() {
            throw new UnsupportedOperationException("nextIndex is not supported");
        }

        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException("previousIndex is not supported");
        }

        public Cursor copy() {
            return new Cursor(position);
        }

        private void adjust(final int diff) {
            ensure(len + diff);
            if (diff > 0) {
                System.arraycopy(data, position, data, position + diff, len - position);
            }
            else {
                System.arraycopy(data, position - diff, data, position, len - position + diff);
            }
            len += diff;
        }

        /**
         * Rewrites the value at the current cursor position.
         *
         * @param other
         *         the line range to rewrite
         *
         * @return the changed line range
         */
        public LineRange rewrite(final LineRange other) {
            Cursor c = copy();
            LineRange old = c.next();
            int oldSize = c.position - position;
            int newSize = sizeOf(other);
            adjust(newSize - oldSize);
            write(other);
            return old;
        }

        @Override
        public void set(final LineRange v) {
            rewrite(v);
        }

        /**
         * Inserts the value at the current cursor position.
         */
        @Override
        public void add(final LineRange v) {
            int newSize = sizeOf(v);
            adjust(newSize);
            write(v);
        }

        /**
         * Removes the current value at the cursor position.
         *
         * @return the deleted element
         */
        public LineRange delete() {
            Cursor c = copy();
            LineRange old = c.next();
            adjust(position - c.position);
            return old;
        }

        private int sizeOf(final LineRange v) {
            return sizeOf(v.getStart()) + sizeOf(v.getEnd() - v.getStart());
        }

        /**
         * Computes the number of bytes that the value 'index' would occupy in its encoded form.
         *
         * @param index
         *         the index to check
         *
         * @return the number of bytes
         */
        private int sizeOf(final int index) {
            int i = index;
            int n = 0;
            do {
                i /= 0x80;
                n++;
            } while (i > 0);
            return n;
        }
    }
}
