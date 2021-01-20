package space.earlygrey.simplegraphs.utils;

import space.earlygrey.simplegraphs.Edge;


/**
 * A function which is called every time a vertex is visited during a shortest path search.
 * This may have side effects, though should not modify the graph in any way.
 * It additionally decides whether to cancel processing this vertex or terminate the algorithm.
 */

public interface ShortestPathPreProcessor<V> {

    /**
     * Called immediately before v is processed by the search algorithm.
     * This may have side effects, though should not modify the graph in any way.
     * @param v the vertex being processed
     * @param edge the edge from which this vertex was found
     * @param distance the length of a shortest path from the search vertex to v
     * @return how to handle processing this vertex
     */
    ProcessorOutcome process(V v, Edge<V> edge, float distance);

}
