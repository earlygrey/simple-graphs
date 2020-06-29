package space.earlygrey.simplegraphs;

import java.util.function.Supplier;

class Connections<V> extends AbstractGraphElementSupplier<V, Connection<V>>{

    Connections(Graph<V> graph, Supplier<Connection<V>> connectionSupplier) {
            super(graph, connectionSupplier);
    }

    Connection<V> getEdge(Node<V> a, Node<V> b, float weight) {
        Connection<V> connection;
        if (freeIndexStack.size() > 0) {
            int index = freeIndexStack.pop();
            connection = objects.get(index);
            connection.set(a, b, weight);
        } else {
            int index = objects.size();
            connection = objectSupplier.get();
            connection.setIndex(index);
            connection.set(a, b, weight);
            objects.add(connection);
            largestUsedIndex = Math.max(index, largestUsedIndex);
        }
        return connection;
    }

}
