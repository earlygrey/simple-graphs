package space.earlygrey.simplegraphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

class AbstractGraphElementSupplier<V, T extends Pooled> {

    final Graph<V> graph;
    final Supplier<T> objectSupplier;
    final ArrayDeque<Integer> freeIndexStack;
    int largestUsedIndex = 0;
    final List<T> objects;

    AbstractGraphElementSupplier(Graph<V> graph, Supplier<T> objectSupplier) {
        this.graph = graph;
        this.objectSupplier = objectSupplier;
        freeIndexStack = new ArrayDeque<>();
        objects = new ArrayList<>();
    }

    void free(T object) {
        if (object == null) return;
        freeIndexStack.push(object.getIndex());
        object.free();
    }

    void clear() {
        int n = objects.size();
        for (int i = 0; i < n; i++) {
            free(objects.get(i));
        }
    }

    void reindex() {
        for (int i = objects.size()-1; i >= 0; i--) {
            if (objects.get(i).isFree()) {
                objects.remove(i);
            }
        }
        freeIndexStack.clear();
        largestUsedIndex = objects.size()-1;
    }
}
