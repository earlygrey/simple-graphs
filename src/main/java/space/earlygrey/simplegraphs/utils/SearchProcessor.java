package space.earlygrey.simplegraphs.utils;

import space.earlygrey.simplegraphs.Edge;

/**
 * A function which is called every time a vertex is visited during a graph search.
 * This may have side effects, though should not modify the graph in any way.
 * It additionally decides whether to immediately terminate the search.
 */

public interface SearchProcessor<V> {

    /**
     * Called immediately before v is processed by the search algorithm.
     * This may have side effects, though should not modify the graph in any way.
     * @param v the vertex being processed
     * @param edge the edge from which this vertex was found
     * @param depth the fewest number of edges in a path from the search vertex to v
     * @return whether to terminate the search
     */
    boolean process(V v, Edge<V> edge, int depth);

}
