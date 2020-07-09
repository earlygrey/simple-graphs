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
package space.earlygrey.simplegraphs.utils;

import java.util.Arrays;

class Array<T> {

    public Object[] items;
    public int size;

    public Array() {
        this(8);
    }

    public Array(int capacity) {
        items = new Object[capacity];
        this.size = 0;
    }

    public int add(T item) {
        checkSize();
        items[size] = item;
        return size++;
    }

    public int getIndex(T item) {
        for (int i = size-1; i >= 0; i--) {
            if (item == items[i]) {
                return i;
            }
        }
        return -1;
    }

    public void remove(int index) {
        if (index >= 0) {
            size--;
            for (int i = index; i < size; i++) {
                items[i] = items[i+1];
            }
            checkSize();
        }
    }

    public void remove(T item) {
        remove(getIndex(item));
    }

    public void checkSize() {
        if (items.length <= size) {
            items = Arrays.copyOf(items, 2*items.length);
        }
    }

    public void set(int index, T item) {
        items[index] = item;
    }

    public void clear() {
        Arrays.fill(items, null);
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public T get(int i) {
        return (T) items[i];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size==0;
    }


}
