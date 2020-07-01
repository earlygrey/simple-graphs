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
