/*
MIT License

Copyright (c) 2020 earlygrey

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package space.earlygrey.simplegraphs;

import java.util.function.Consumer;

import space.earlygrey.simplegraphs.utils.Heuristic;

public class Algorithms<V> {

    final Graph<V> graph;
    final AlgorithmImplementations<V> implementations;

    Algorithms(Graph<V> graph) {
        this.graph = graph;
        implementations = new AlgorithmImplementations<>(graph);
    }

    //--------------------
    //  Shortest Path
    //--------------------

    /**
     * Find a shortest path from the start vertex to the target vertex, using Dijkstra's algorithm implemented with a priority queue.
     * @param start the starting vertex
     * @param target the target vertex
     * @return a list of vertices from start to target containing the ordered vertices of a shortest path, including both the start and target vertices.
     * If there is no path from the start vertex to the target vertex, the returned path is empty.
     */
    public Path<V> findShortestPath(V start, V target) {
        return findShortestPath(start, target, null, null);
    }

    /**
     * Find a shortest path from the start vertex to the target vertex, using Dijkstra's algorithm implemented with a priority queue.
     * @param start the starting vertex
     * @param target the target vertex
     * @param preprocessor
     * @return a list of vertices from start to target containing the ordered vertices of a shortest path, including both the start and target vertices.
     * If there is no path from the start vertex to the target vertex, the returned path is empty.
     */
    public Path<V> findShortestPath(V start, V target, Consumer<AlgorithmStep<V>> preprocessor) {
        return findShortestPath(start, target, null, null, preprocessor);
    }

    /**
     * Find a shortest path from the start vertex to the target vertex, using Dijkstra's algorithm implemented with a priority queue.
     * @param start the starting vertex
     * @param target the target vertex
     * @param path a path instance to reuse
     * @return a list of vertices from start to target containing the ordered vertices of a shortest path, including both the start and target vertices.
     * If there is no path from the start vertex to the target vertex, the returned path is empty.
     */
    public Path<V> findShortestPath(V start, V target, Path<V> path) {
        return findShortestPath(start, target, null, path);
    }

    /**
     * Find a shortest path from the start vertex to the target vertex, using the A* search algorithm with the provided heuristic, and implemented with a priority queue.
     * <br>The heuristic is a function, which for any two vertices returns an estimate of the distance between them. Note: the heuristic h
     * must be admissible, that is, for any two vertices x and y, h(x,y) &#8804; d(x,y), where d(x,y) is the actual distance of a shortest path from x to y.
     * @param start the starting vertex
     * @param target the target vertex
     * @param heuristic a heuristic to guide the search
     * @return a list of vertices from start to target containing the ordered vertices of a shortest path, including both the start and target vertices.
     * If there is no path from the start vertex to the target vertex, the returned path is empty.
     */
    public Path<V> findShortestPath(V start, V target, Heuristic<V> heuristic) {
        return findShortestPath(start, target, heuristic, null);
    }

    /**
     * Find a shortest path from the start vertex to the target vertex, using the A* search algorithm with the provided heuristic, and implemented with a priority queue.
     * <br>The heuristic is a function, which for any two vertices returns an estimate of the distance between them. Note: the heuristic h
     * must be admissible, that is, for any two vertices x and y, h(x,y) &#8804; d(x,y), where d(x,y) is the actual distance of a shortest path from x to y.
     * @param start the starting vertex
     * @param target the target vertex
     * @param heuristic a heuristic to guide the search
     * @param path a path instance to reuse
     * @return a list of vertices from start to target containing the ordered vertices of a shortest path, including both the start and target vertices.
     * If there is no path from the start vertex to the target vertex, the returned path is empty.
     */
    public Path<V> findShortestPath(V start, V target, Heuristic<V> heuristic, Path<V> path) {
        return findShortestPath(start, target, heuristic, path, null);
    }

    /**
     * Find a shortest path from the start vertex to the target vertex, using the A* search algorithm with the provided heuristic, and implemented with a priority queue.
     * <br>The heuristic is a function, which for any two vertices returns an estimate of the distance between them. Note: the heuristic h
     * must be admissible, that is, for any two vertices x and y, h(x,y) &#8804; d(x,y), where d(x,y) is the actual distance of a shortest path from x to y.
     * @param start the starting vertex
     * @param target the target vertex
     * @param heuristic a heuristic to guide the search
     * @param path a path instance to reuse
     * @param preprocessor
     * @return a list of vertices from start to target containing the ordered vertices of a shortest path, including both the start and target vertices.
     * If there is no path from the start vertex to the target vertex, the returned path is empty.
     */
    public Path<V> findShortestPath(V start, V target, Heuristic<V> heuristic, Path<V> path, Consumer<AlgorithmStep<V>> preprocessor) {
        Node<V> startNode = graph.getNode(start);
        Node<V> targetNode = graph.getNode(target);
        if (startNode==null || targetNode==null) Errors.throwVertexNotInGraphVertexException();
        path = implementations.findShortestPath(startNode, targetNode, heuristic, path, preprocessor);
        path.setFixed(true);
        return path;
    }


    /**
     * Find the length of a shortest path from the start vertex to the target vertex, using Dijkstra's algorithm implemented with a priority queue.
     * @param start the starting vertex
     * @param target the target vertex
     * @return the sum of the weights in a shortest path from the starting vertex to the target vertex.
     * If there is no path from the start vertex to the target vertex, {@link Float#MAX_VALUE} is returned.
     */
    public float findMinimumDistance(V start, V target) {
        return implementations.findMinimumDistance(graph.getNode(start), graph.getNode(target));
    }

    /**
     * Find the length of a shortest path from the start vertex to the target vertex, using the A* search algorithm with the provided heuristic, and implemented with a priority queue.
     * @param start the starting vertex
     * @param target the target vertex
     * @return the sum of the weights in a shortest path from the starting vertex to the target vertex.
     * If there is no path from the start vertex to the target vertex, {@link Float#MAX_VALUE} is returned.
     */
    public float findMinimumDistance(V start, V target, Heuristic<V> heuristic) {
        return implementations.findMinimumDistance(graph.getNode(start), graph.getNode(target), heuristic);
    }

    /**
     * Checks whether there exists a path from the start vertex to target vertex, using Dijkstra's algorithm implemented with a priority queue.
     * @param start the starting vertex
     * @param target the target vertex
     * @return whether there exists a path from the start vertex to target vertex
     */
    public boolean isConnected(V start, V target) {
        return implementations.findMinimumDistance(graph.getNode(start), graph.getNode(target)) < Float.MAX_VALUE;
    }

    //--------------------
    // Graph Searching
    //--------------------

    /**
     * Perform a breadth first search starting from the specified vertex.
     * @param v the vertex at which to start the search
     * @param preprocessor
     */
    public void breadthFirstSearch(V v, Consumer<AlgorithmStep<V>> preprocessor) {
        Node<V> node = graph.getNode(v);
        if (node==null) Errors.throwVertexNotInGraphVertexException();
        implementations.breadthFirstSearch(node, preprocessor);
    }


    /**
     * Perform a depth first search starting from the specified vertex.
     * @param v the vertex at which to start the search
     * @param preprocessor
     */
    public void depthFirstSearch(V v, Consumer<AlgorithmStep<V>> preprocessor) {
        Node<V> node = graph.getNode(v);
        if (node==null) Errors.throwVertexNotInGraphVertexException();
        implementations.depthFirstSearch(node, preprocessor);
    }

    //--------------------
    //  Structures
    //--------------------

    /**
     * Checks whether there are any cycles in the graph using depth first searches.
     * @return true if the graph contains a cycle, false otherwise
     */
    public boolean detectCycle() {
        return implementations.containsCycle(graph);
    }

}
