package space.earlygrey.simplegraphs;

public class Errors {

    static void throwNullVertexException() {
        throw new IllegalArgumentException("Vertices cannot be null");
    }

    static void throwSameVertexException() {
        throw new IllegalArgumentException("Self loops are not allowed");
    }

    static void throwVertexNotInGraphVertexException() {
        throw new IllegalArgumentException("At least one vertex is not in the graph");
    }


}
