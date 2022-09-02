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
package space.earlygrey.simplegraphs.algorithms;

import java.util.concurrent.atomic.AtomicInteger;

import space.earlygrey.simplegraphs.Errors;
import space.earlygrey.simplegraphs.Graph;
import space.earlygrey.simplegraphs.Node;
import space.earlygrey.simplegraphs.Path;
import space.earlygrey.simplegraphs.utils.Heuristic;
import space.earlygrey.simplegraphs.utils.SearchProcessor;

public abstract class Algorithms<V> {

    protected final Graph<V> graph;
    private AtomicInteger runID = new AtomicInteger();

    Algorithms(Graph<V> graph) {
        this.graph = graph;
    }

    public int requestRunID() {
        return runID.getAndIncrement();
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
     * @param processor a consumer which is called immediately before processing each vertex. See {@link SearchStep}.
     * @return a list of vertices from start to target containing the ordered vertices of a shortest path, including both the start and target vertices.
     * If there is no path from the start vertex to the target vertex, the returned path is empty.
     */
    public Path<V> findShortestPath(V start, V target, SearchProcessor<V> processor) {
        return findShortestPath(start, target, null, processor);
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
     * @param processor a consumer which is called immediately before processing each vertex. See {@link SearchStep}.
     * @return a list of vertices from start to target containing the ordered vertices of a shortest path, including both the start and target vertices.
     * If there is no path from the start vertex to the target vertex, the returned path is empty.
     */
    public Path<V> findShortestPath(V start, V target, Heuristic<V> heuristic, SearchProcessor<V> processor) {
        AStarSearch<V> search = newAstarSeach(start, target, heuristic, processor);
        search.finish();
        return search.getPath();
    }

    /**
     * Find a shortest path from the start vertex to the target vertex, using the A* search algorithm with the provided heuristic, and implemented with a priority queue.
     * <br>The heuristic is a function, which for any two vertices returns an estimate of the distance between them. Note: the heuristic h
     * must be admissible, that is, for any two vertices x and y, h(x,y) &#8804; d(x,y), where d(x,y) is the actual distance of a shortest path from x to y.
     * @param start the starting vertex
     * @param target the target vertex
     * @param heuristic a heuristic to guide the search
     * @param processor a consumer which is called immediately before processing each vertex. See {@link SearchStep}.
     * @return a list of vertices from start to target containing the ordered vertices of a shortest path, including both the start and target vertices.
     * If there is no path from the start vertex to the target vertex, the returned path is empty.
     */
    public AStarSearch<V> newAstarSeach(V start, V target, Heuristic<V> heuristic, SearchProcessor<V> processor) {
        Node<V> startNode = graph.internals().getNode(start);
        Node<V> targetNode = graph.internals().getNode(target);
        if (startNode == null || targetNode == null) Errors.throwVertexNotInGraphVertexException(true);
        return new AStarSearch<>(requestRunID(), startNode, targetNode, heuristic, processor);
    }


    /**
     * Find the length of a shortest path from the start vertex to the target vertex, using Dijkstra's algorithm implemented with a priority queue.
     * @param start the starting vertex
     * @param target the target vertex
     * @return the sum of the weights in a shortest path from the starting vertex to the target vertex.
     * If there is no path from the start vertex to the target vertex, {@link Float#MAX_VALUE} is returned.
     */
    public float findMinimumDistance(V start, V target) {
        return findMinimumDistance(start, target, null);
    }

    /**
     * Find the length of a shortest path from the start vertex to the target vertex, using the A* search algorithm with the provided heuristic, and implemented with a priority queue.
     * @param start the starting vertex
     * @param target the target vertex
     * @return the sum of the weights in a shortest path from the starting vertex to the target vertex.
     * If there is no path from the start vertex to the target vertex, {@link Float#MAX_VALUE} is returned.
     */
    public float findMinimumDistance(V start, V target, Heuristic<V> heuristic) {
        AStarSearch<V> search = newAstarSeach(start, target, heuristic, null);
        search.finish();
        if (search.getEnd() == null) return Float.MAX_VALUE;
        else return search.getEnd().getDistance();
    }

    /**
     * Checks whether there exists a path from the start vertex to target vertex, using Dijkstra's algorithm implemented with a priority queue.
     * @param start the starting vertex
     * @param target the target vertex
     * @return whether there exists a path from the start vertex to target vertex
     */
    public boolean isConnected(V start, V target) {
        return findMinimumDistance(start, target) < Float.MAX_VALUE;
    }

    //--------------------
    // Graph Searching
    //--------------------

    /**
     * Perform a breadth first search starting from the specified vertex.
     * @param v the vertex at which to start the search
     * @param processor a consumer which is called immediately before processing each vertex. See {@link SearchStep}.
     */
    public void breadthFirstSearch(V v, SearchProcessor<V> processor) {
        Node<V> node = graph.internals().getNode(v);
        if (node == null) Errors.throwVertexNotInGraphVertexException(false);
        new BreadthFirstSearch<>(requestRunID(), node, processor).finish();
    }


    /**
     * Perform a depth first search starting from the specified vertex.
     * @param v the vertex at which to start the search
     * @param processor a consumer which is called immediately before processing each vertex. See {@link SearchStep}.
     */
    public void depthFirstSearch(V v, SearchProcessor<V> processor) {
        Node<V> node = graph.internals().getNode(v);
        if (node == null) Errors.throwVertexNotInGraphVertexException(false);
        new DepthFirstSearch<>(requestRunID(), node, processor).finish();
    }


    /**
     * Perform a search using Dijkstra's algorithm starting from the specified vertex.
     * @param v the vertex at which to start the search
     * @param processor a consumer which is called immediately before processing each vertex. See {@link SearchStep}.
     */
    public void dijkstraSearch(V v, SearchProcessor<V> processor) {
        Node<V> node = graph.internals().getNode(v);
        if (node == null) Errors.throwVertexNotInGraphVertexException(false);
        new AStarSearch<>(requestRunID(), node, null, null, processor).finish();
    }


    //--------------------
    //  Structures
    //--------------------

    /**
     * Checks whether there are any cycles in the graph using depth first searches.
     * @return true if the graph contains a cycle, false otherwise
     */
    public boolean containsCycle() {
        return new CycleDetector<>(requestRunID(), graph).containsCycle();
    }

}
