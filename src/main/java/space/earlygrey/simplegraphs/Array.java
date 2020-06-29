package space.earlygrey.simplegraphs;

import java.util.Arrays;

class Array<T> {

    Object[] items;
    int size;

    Array() {
        this(8);
    }

    Array(int capacity) {
        items = new Object[capacity];
        this.size = 0;
    }

    void add(T item) {
        checkSize();
        items[size] = item;
        size++;
    }

    void remove(T item) {
        int index = -1;
        for (int i = size-1; i >= 0; i--) {
            if (item == items[i]) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            for (int i = index; i < size-1; i++) {
                items[i] = items[i+1];
            }
            size--;
            checkSize();
        }
    }

    void checkSize() {
        if (items.length <= size) {
            items = Arrays.copyOf(items, 2*items.length);
        } else if (items.length > 4) {
            int newSize = (int) (0.5*size);
            if (items.length < newSize) {
                items = Arrays.copyOf(items, newSize);
            }
        }
    }

    void set(int index, T item) {
        items[index] = item;
    }

    void clear() {
        Arrays.fill(items, null);
        size = 0;
    }

    @SuppressWarnings("unchecked")
    T get(int i) {
        return (T) items[i];
    }

    int size() {
        return size;
    }

    boolean isEmpty() {
        return size==0;
    }


}
