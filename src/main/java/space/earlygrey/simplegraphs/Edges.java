package space.earlygrey.simplegraphs;

import java.util.function.Supplier;

class Edges<V> extends AbstractGraphElementSupplier<V, Connection<V>>{

    Edges(Graph<V> graph, Supplier<Connection<V>> edgeSupplier) {
            super(graph, edgeSupplier);
    }

    Connection<V> getEdge(Node<V> a, Node<V> b, float weight) {
        Connection<V> edge;
        if (freeIndexStack.size() > 0) {
            int index = freeIndexStack.pop();
            edge = objects.get(index);
            edge.set(a, b, weight);
        } else {
            int index = objects.size();
            edge = objectSupplier.get();
            edge.setIndex(index);
            edge.set(a, b, weight);
            objects.add(edge);
            largestUsedIndex = Math.max(index, largestUsedIndex);
        }
        return edge;
    }

}
