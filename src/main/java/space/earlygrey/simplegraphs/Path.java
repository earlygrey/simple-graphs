package space.earlygrey.simplegraphs;

import java.util.Collection;
import java.util.function.Predicate;

public class Path<V> extends Array<V> {

    float length = 0;
    boolean fixed = false;

    public Path(int size) {
        super(size, true);
    }

    public float getLength() {
        return length;
    }

    void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    void checkFixed() {
        if (fixed) throw new UnsupportedOperationException("You cannot modify this path.");
    }

    @Override
    public boolean add(V item) {
        checkFixed();
        return super.add(item);
    }

    @Override
    public V set(int index, V item) {
        checkFixed();
        return super.set(index, item);
    }

    @Override
    public boolean remove(Object item) {
        checkFixed();
        return super.remove(item);
    }

    @Override
    public boolean addAll(Collection<? extends V> collection) {
        checkFixed();
        return super.addAll(collection);
    }

    @Override
    public V remove(int index) {
        checkFixed();
        return super.remove(index);
    }

    @Override
    public void clear() {
        checkFixed();
        super.clear();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        checkFixed();
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        checkFixed();
        return super.retainAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super V> filter) {
        checkFixed();
        return false;
    }
}
