package space.earlygrey.simplegraphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

class Connections<V> {

    final Graph<V> graph;
    final ArrayDeque<Integer> freeIndexStack;
    int largestUsedIndex = 0;
    final List<Connection<V>> connectionList;
    final Supplier<Connection<V>> connectionSupplier;

    Connections(Graph<V> graph, Supplier<Connection<V>> connectionSupplier) {
        this.graph = graph;
        this.connectionSupplier = connectionSupplier;
        freeIndexStack = new ArrayDeque<>();
        connectionList = new ArrayList<>();
    }


    Connection<V> getEdge(Node<V> a, Node<V> b, float weight) {
        Connection<V> connection;
        if (freeIndexStack.size() > 0) {
            int index = freeIndexStack.pop();
            connection = connectionList.get(index);
            connection.set(a, b, weight);
        } else {
            int index = connectionList.size();
            connection = connectionSupplier.get();
            connection.index = index;
            connection.set(a, b, weight);
            connectionList.add(connection);
            largestUsedIndex = Math.max(index, largestUsedIndex);
            graph.algorithms.ensureCapacity(largestUsedIndex+1);
        }
        return connection;
    }

    void free(Connection<V> node) {
        if (node == null) return;
        freeIndexStack.push(node.index);
        node.reset();
    }

    void clear() {
        int n = connectionList.size();
        for (int i = 0; i < n; i++) {
            free(connectionList.get(i));
        }
    }

}
