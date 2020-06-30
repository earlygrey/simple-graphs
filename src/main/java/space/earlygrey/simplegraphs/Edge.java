package space.earlygrey.simplegraphs;

public abstract class Edge<V> {


    Edge(){}

    public abstract V getA();
    public abstract V getB();
    public abstract float getWeight();
    public abstract void setWeight(float weight);

    abstract Node<V> getNodeA();
    abstract Node<V> getNodeB();
}
