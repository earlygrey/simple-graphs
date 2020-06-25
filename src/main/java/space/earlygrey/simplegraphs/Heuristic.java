package space.earlygrey.simplegraphs;

public interface Heuristic<V> {

    float getEstimate(V currentNode, V targetNode);

    class DefaultHeuristic<V> implements Heuristic<V> {
        @Override
        public float getEstimate(V currentNode, V targetNode) {
            return 0;
        }
    }

}
