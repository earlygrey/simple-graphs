package space.earlygrey.simplegraphs.utils;

public interface Heuristic<V> {

    float getEstimate(V currentNode, V targetNode);

}
