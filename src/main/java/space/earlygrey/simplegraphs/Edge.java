package space.earlygrey.simplegraphs;

public abstract class Edge<V> {


    Edge(){}

    public abstract V getA();
    public abstract V getB();

    public abstract float getWeight();
    public abstract void setWeight(float weight);

    abstract Node<V> getInternalNodeA();
    abstract Node<V> getInternalNodeB();

    abstract void set(Node<V> a, Node<V> b, float weight);
}
