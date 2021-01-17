package space.earlygrey.simplegraphs.utils;

import space.earlygrey.simplegraphs.Edge;


/**
 * A function which is called every time a vertex is visited during a shortest path search.
 * This may have side effects, though should not modify the graph in any way.
 * It additionally decides whether to cancel processing this vertex.
 */

public interface ShortestPathPreProcessor<V> {

    /**
     * Called immediately before v is processed by the search algorithm.
     * This may have side effects, though should not modify the graph in any way.
     * If this function returns true, the vertex will not be processed and its neighbours will not be added to the queue in that iteration.
     * @param v the vertex being processed
     * @param edge the edge from which this vertex was found
     * @param distance the length of a shortest path from the search vertex to v
     * @return whether to cancel processing this vertex
     */
    boolean process(V v, Edge<V> edge, float distance);

}
