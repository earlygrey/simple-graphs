
[![](https://jitpack.io/v/earlygrey/simple-graphs.svg)](https://jitpack.io/#space.earlygrey/simple-graphs)

*note: still in "alpha"*

# Simple Graphs

Simple graphs is a java library containing basic graph data structures and algorithms.

It has two types of graph data structures representing weighted undirected and directed graphs. Each of these provides methods for adding and removing vertices and edges, and accessing collections of its vertices and edges.

Algorithms implemented are:
- [breadth first search](https://en.wikipedia.org/wiki/Breadth-first_search)
- [depth first search](https://en.wikipedia.org/wiki/Depth-first_search)
- shortest path using the [A* search algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm) (which is [Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) when no heuristic is provided)
- [cycle detection](https://en.wikipedia.org/wiki/Cycle_(graph_theory)#Cycle_detection) for directed graphs
- [topological sort](https://en.wikipedia.org/wiki/Topological_sorting) for directed graphs

You might be wondering what the point of another graph library is, since there are already some very [powerful](https://jgrapht.org/) and [versatile](https://github.com/google/guava/wiki/GraphsExplained) libraries out there, which are made by much better programmers than me. Well the idea behind simple graphs is that it's first and foremost user-friendly and simple to use. Just instantiate a graph object, add some vertices and edges and you're ready, algorithms are called directly on the graph object. Simple graphs has no dependencies.
