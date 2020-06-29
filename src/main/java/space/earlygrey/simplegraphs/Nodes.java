package space.earlygrey.simplegraphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

class Nodes<V> {

    final Graph<V> graph;
    final ArrayDeque<Integer> freeIndexStack;
    int largestUsedIndex = 0;
    final List<Node<V>> nodeList;

    Nodes(Graph<V> graph) {
        this.graph = graph;
        freeIndexStack = new ArrayDeque<>();
        nodeList = new ArrayList<>();
    }


    Node<V> getNode(V v) {
        Node<V> node;
        if (freeIndexStack.size() > 0) {
            int index = freeIndexStack.pop();
            node = nodeList.get(index);
            node.object = v;
        } else {
            int index = nodeList.size();
            node = new Node(v, graph, index);
            nodeList.add(node);
            largestUsedIndex = Math.max(index, largestUsedIndex);
            graph.algorithms.ensureCapacity(largestUsedIndex+1);
        }
        return node;
    }

    void free(Node<V> node) {
        if (node == null) return;
        freeIndexStack.push(node.index);
        node.reset();
    }

    void clear() {
        int n = nodeList.size();
        for (int i = 0; i < n; i++) {
            free(nodeList.get(i));
        }
    }


}
