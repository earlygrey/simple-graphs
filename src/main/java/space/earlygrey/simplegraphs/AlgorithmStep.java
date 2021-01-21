package space.earlygrey.simplegraphs;

public abstract class AlgorithmStep<V> {

    boolean terminate, ignore;
    Node<V> node;

    AlgorithmStep() {

    }

    void prepare(Node<V> node) {
        this.node = node;
        terminate = false;
        ignore = false;
    }

    public void terminate() {
        terminate = true;
    }

    public void ignore() {
        ignore = true;
    }

    public V vertex() {
        return node.getObject();
    }

    public Edge<V> edge() {
        return node.connection;
    }

    public static class SearchStep<V> extends AlgorithmStep<V> {

        public int depth() {
            return node.i;
        }

    }

}
