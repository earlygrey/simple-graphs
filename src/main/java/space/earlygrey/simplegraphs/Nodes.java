package space.earlygrey.simplegraphs;

import java.util.function.Supplier;

class Nodes<V> extends AbstractGraphElementSupplier<V, Node<V>> {

    Nodes(Graph<V> graph, Supplier<Node<V>> vertexSupplier) {
        super(graph, vertexSupplier);
    }

    Node<V> getNode(V v) {
        Node<V> node;
        if (freeIndexStack.size() > 0) {
            int index = freeIndexStack.pop();
            node = objects.get(index);
            node.object = v;
        } else {
            int index = objects.size();
            node = objectSupplier.get();
            node.index = index;
            node.object = v;
            objects.add(node);
            largestUsedIndex = Math.max(index, largestUsedIndex);
            graph.algorithms.ensureVertexCapacity(largestUsedIndex+1);
        }
        return node;
    }

}
