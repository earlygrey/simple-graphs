
[![](https://jitpack.io/v/earlygrey/simple-graphs.svg)](https://jitpack.io/#space.earlygrey/simple-graphs)

*note: still in "alpha"*

# Simple Graphs

Simple graphs is a java library containing basic graph data structures and algorithms.

It has two types of graph data structures representing undirected and directed graphs. Each of these provides methods for adding and removing vertices and edges, for retrieving edges, and for accessing collections of its vertices and edges. All graphs in simple graphs are weighted and (of course) simple.

Algorithms implemented are:
- [breadth first search](https://en.wikipedia.org/wiki/Breadth-first_search)
- [depth first search](https://en.wikipedia.org/wiki/Depth-first_search)
- shortest path using the [A* search algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm) (which is [Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) when no heuristic is provided)
- [cycle detection](https://en.wikipedia.org/wiki/Cycle_(graph_theory)#Cycle_detection)
- [minimum weight spanning tree](https://en.wikipedia.org/wiki/Minimum_spanning_tree) for undirected graphs using [Kruskal's algorithm](https://en.wikipedia.org/wiki/Kruskal%27s_algorithm)
- [topological sort](https://en.wikipedia.org/wiki/Topological_sorting) for directed graphs

You might be wondering what the point of this graph library is, since there are already some very [powerful](https://jgrapht.org/) and [versatile](https://github.com/google/guava/wiki/GraphsExplained) libraries out there. Well the idea behind simple graphs is that it's first and foremost user-friendly and simple to use. Just instantiate a graph object, add some vertices and edges and you're ready. Algorithms are called directly on the graph object.

Simple graphs uses java 8 (JGraphT 1.5.0+ requires JDK 11) and only uses java collections. It has no dependencies, and so should be GWT compatible (JGraphT and guava graphs are not). It uses `float` for floating point values, so should be a little more compatible with libraries that use floats, such as [libgdx](https://github.com/libgdx/libgdx).


If you're looking for a broader, more powerful library and don't care about java 8 or GWT, I'd recommend [JGraphT](https://jgrapht.org/). It essentially does everything this library does, plus a lot more.
