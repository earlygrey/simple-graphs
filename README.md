
[![](https://jitpack.io/v/earlygrey/simple-graphs.svg)](https://jitpack.io/#space.earlygrey/simple-graphs)

# Simple Graphs

Simple graphs is a Java library containing basic graph data structures and algorithms. It is lightweight, fast, and intuitive to use.

It has two types of graph data structures representing undirected and directed graphs. Each of these provides methods for adding and removing vertices and edges, for retrieving edges, and for accessing collections of its vertices and edges. All graphs in simple graphs are weighted and (of course) simple.

Algorithms implemented are:
- [breadth first search](https://en.wikipedia.org/wiki/Breadth-first_search)
- [depth first search](https://en.wikipedia.org/wiki/Depth-first_search)
- shortest path using the [A* search algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm) ([Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) when no heuristic is provided)
- [cycle detection](https://en.wikipedia.org/wiki/Cycle_(graph_theory)#Cycle_detection)
- [topological sort](https://en.wikipedia.org/wiki/Topological_sorting) for directed graphs (performed in-place)
- [minimum weight spanning tree](https://en.wikipedia.org/wiki/Minimum_spanning_tree) for undirected graphs using [Kruskal's algorithm](https://en.wikipedia.org/wiki/Kruskal%27s_algorithm)

Simple graphs uses Java 8 and Java Collections. It has no dependencies, and so should be GWT compatible. It uses `float` for floating point values, so should be a little more compatible with libraries that use floats, such as [libgdx](https://github.com/libgdx/libgdx).

If you're looking for a broader, more powerful library and don't care about java 8, GWT, or including a lot of extra dependencies, I'd recommend [JGraphT](https://jgrapht.org/). It essentially does everything this library does, plus a lot more.

---

## Installation
To include Simple Graphs in your project, follow the instructions on the [jitpack website](https://jitpack.io/#space.earlygrey/simple-graphs).


## Usage

### Construction

Usage looks something like:
```java
Graph<Integer> graph = new UndirectedGraph<>(); // or new DirectedGraph<>();

for (int i = 0; i < 5; i++) {
  graph.addVertex(i);
}
boolean containsVertex = graph.contains(0);

graph.addEdge(1, 2);
boolean containsEdge = graph.edgeExists(1, 2)
```

### Collections
You can obtain `Collection<V>`s of the vertices and edges:
```java
Collection<Integer> vertices = graph.getVertices();
Collection<Edge<Integer>> edges = graph.getEdges();
```
Both collections cannot be modified directly - they provide a "read-only" iterable view of the vertices and edges and are subject to the same restrictions as a `Collection` returned by `Collections#unmodifiableCollection()`. The iteration order is guaranteed to be consistent for both collections (default order is insertion order) and both are sortable, though you need to sort using the graph object and not directly on the collection. Something like:

```java
Comparator<V> vertexComparator = ...;
graph.sortVertices(vertexComparator);
```
If you want to access elements by index, you need to convert to an array via the `toArray()` methods or add them to a `List` via eg `new ArrayList<>(graph.getVertices())`.

### Algorithms

To access algorithms, use `Graph#algorithms()`. You need to have a specific reference to a subclass of `Graph` (`DirectedGraph` or `UndirectedGraph`) to access algorithms specific to that type of graph.
```java
V u, v;
Graph<V> graph;
UndirectedGraph<V> undirected;
DirectedGraph<V> directed;

Path<V> path = graph.algorithms().findShortestPath(u, v);
UndirectedGraph<V> tree = undirected.algorithms().findMinimumWeightSpanningTree();
directed.algorithms().topologicalSort();
```

Additionally, simple graphs provides a few functional interfaces that can be used to run a preprocessing step on each vertex as an algorithm is running. These can be used for side effects (for example to construct another graph as the algorithm runs), and can also be used to decide whether to cancel processing that vertex. For example
```java
List<V> vertices = new ArrayList<>();
SearchProcessor<V> processor = (v, edge, depth) -> {
    vertices.add(v); // keep track of processed vertices
    return depth > 4; // terminate search if depth is greater than 4
};
graph.algorithms().breadthFirstSearch(u, processor);

graph.algorithms().depthFirstSearch(u, (v, e, d) -> {System.out.println("processing" + v); return false;});
```

## Technical Considerations

While vertices can be any type of `Object`, care must be taken that they are immutable, in the sense that while in the `Graph` their `hashCode()` method always returns the same value, and `equals` is consistent. In general, vertex objects are subject to the same requirements as keys in a java `Map`.

---
## Wiki

[See the wiki](https://github.com/earlygrey/simple-graphs/wiki) for more info.
