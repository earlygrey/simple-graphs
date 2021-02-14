package space.earlygrey.simplegraphs.utils;

import space.earlygrey.simplegraphs.Edge;

public interface WeightFunction<V> {

    float getWeight(Edge<V> edge);

}
