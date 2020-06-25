package space.earlygrey.simplegraphs;


import java.util.Objects;

public class Connection<T extends Object> {

    static final float DEFAULT_WEIGHT = 1;

    protected final Node<T> a, b;
    protected float weight = DEFAULT_WEIGHT;
    protected final Edge edge = new Edge(this);

    protected Connection(Node<T> a, Node<T> b) {
        this.a = a;
        this.b = b;
    }
    protected Connection(Node<T> a, Node<T> b, float weight) {
        this.a = a;
        this.b = b;
        this.weight = weight;
    }

    public T getA() {
        return a.object;
    }

    public T getB() {
        return b.object;
    }

    public boolean isIncident(Node<T> v) {
        return a.equals(v) || b.equals(v);
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "{" + a + ", " + b +'}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection connection = (Connection) o;
        if (a.equals(connection.a) && b.equals(connection.b)) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
