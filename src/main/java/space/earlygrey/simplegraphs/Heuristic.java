package space.earlygrey.simplegraphs;

public interface Heuristic<V> {

    float getEstimate(V currentNode, V targetNode);

}
