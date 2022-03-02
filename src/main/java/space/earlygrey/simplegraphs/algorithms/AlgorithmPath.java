package space.earlygrey.simplegraphs.algorithms;

import space.earlygrey.simplegraphs.Node;
import space.earlygrey.simplegraphs.Path;

class AlgorithmPath<V> extends Path<V> {

    AlgorithmPath() {
        super(0, false);
    }

    AlgorithmPath(Node<V> v) {
        super(v.getIndex() + 1, true);
        setByBacktracking(v);
    }

    void setByBacktracking(Node<V> node) {
        int nodeCount = node.getIndex() + 1;

        if (items.length < nodeCount) strictResize(nodeCount);

        Node<V> v = node;
        while(v != null) {
            set(v.getIndex(), v.getObject());
            v = v.getPrev();
        }

        setLength(node.getDistance());
    }
}
