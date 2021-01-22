package space.earlygrey.simplegraphs;

public class AlgorithmStep<V> {

    boolean terminate, ignore;
    Node<V> node;
    int count;

    AlgorithmStep() {

    }

    void prepare(Node<V> node) {
        this.node = node;
        terminate = false;
        ignore = false;
        count++;
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

    public V previous() {
        return node.connection.getA();
    }

    public int depth() {
            return node.i;
        }

    public float distance() {
            return node.distance;
        }

    public int count() {
        return count;
    }

    public Path<V> createPath() {
        return new Path<>(node);
    }

}
