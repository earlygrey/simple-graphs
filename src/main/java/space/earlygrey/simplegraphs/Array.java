/*
MIT License

Copyright (c) 2020 earlygrey

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package space.earlygrey.simplegraphs;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Array<T> extends AbstractCollection<T> {

    public Object[] items;
    public int size;

    public Array() {
        this(8);
    }

    public Array(int capacity) {
        this(capacity, false);
    }

    public Array(int capacity, boolean resize) {
        items = new Object[capacity];
        if (resize) this.size = capacity;
    }

    @Override
    public boolean add(T item) {
        resize(size + 1);
        items[size++] = item;
        return true;
    }

    @SuppressWarnings("unchecked")
    public T set(int index, T item) {
        Object oldVal = items[index];
        items[index] = item;
        return (T) oldVal;
    }

    @Override
    public boolean remove(Object item) {
        return remove(indexOf(item)) != null;
    }


    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object e : collection) if (!contains(e)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        int numNew = collection.size();
        if (numNew == 0) {
            return false;
        } else {
            resize(numNew);
            Object[] collectionArray = collection.toArray();
            System.arraycopy(collectionArray, 0, items, size, numNew);
            return true;
        }
    }

    public int indexOf(Object item) {
        for (int i = size-1; i >= 0; i--) {
            if (item == items[i]) {
                return i;
            }
        }
        return -1;
    }

    public T remove(int index) {
        if (index >= 0 && index < size) {
            T item = (T) items[index];
            size--;
            for (int i = index; i < size; i++) {
                items[i] = items[i+1];
            }
            return item;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }


    void resize(int newSize) {
        if (newSize > items.length) {
            strictResize(Math.max(2*items.length, newSize));
        }
    }

    protected void strictResize(int newSize) {
        items = Arrays.copyOf(items, newSize);
    }

    @Override
    public void clear() {
        Arrays.fill(items, null);
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public T get(int i) {
        return (T) items[i];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(this.items, this.size);
    }

    @Override
    public <U> U[] toArray(U[] var1) {
        if (var1.length < this.size) {
            var1 = Arrays.copyOf(var1, this.size);
            System.arraycopy(this.items, 0, var1, 0, this.size);
        } else {
            System.arraycopy(this.items, 0, var1, 0, this.size);
            if (var1.length > this.size) {
                var1[this.size] = null;
            }
        }
        return var1;
    }



    class ArrayIterator implements Iterator<T> {

        int cursor = 0;

        public boolean hasNext() {
            return cursor < size;
        }

        public T next() {
            if (cursor >= size) {
                throw new NoSuchElementException();
            } else {
                return (T) items[cursor++];
            }
        }

        public void remove() {
            Errors.throwModificationException();
        }

    }
}
