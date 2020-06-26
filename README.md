
[![](https://jitpack.io/v/earlygrey/simple-graphs.svg)](https://jitpack.io/#space.earlygrey/simple-graphs)

*note: still in "alpha"*

# Simple Graphs

Simple graphs is a java library containing basic graph data structures and algorithms.

It has two types of graph data structures - representing undirected and directed graphs, both weighted. Each of these provide methods for adding and removing vertices and edges, accessing collections of vertices and edges.

Algorithms implemented are:
- finding shortest paths using the [A* search algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm) (which defaults to [Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) when no heuristic is provided).
- [cycle detection](https://en.wikipedia.org/wiki/Cycle_(graph_theory)#Cycle_detection) for directed graphs
- [finding components](https://en.wikipedia.org/wiki/Component_(graph_theory)#Algorithms) in undirected graphs

You might be wondering what the point of another graph library is, since there are already some very [powerful](https://jgrapht.org/) and [versatile](https://github.com/google/guava/wiki/GraphsExplained) libraries out there, which are made by much better programmers than me. Well the idea behind simple graphs is that it's first and foremost user-friendly and simple to use - just instantiate a graph object, add some vertices and edges and you're ready. Algorithms are called directly on the graph object.
