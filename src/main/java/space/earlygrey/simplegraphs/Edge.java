package space.earlygrey.simplegraphs;

import java.util.Objects;

public class Edge<V> {

    final Connection<V> connection;

    Edge(Connection<V> connection){
        this.connection = connection;
    }

    public V getA() {
        return connection.a.object;
    }

    public V getB() {
        return connection.b.object;
    }

    public float getWeight() {
        return connection.weight;
    }

    public void setWeight(float weight) {
        connection.setWeight(weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge<?> edge = (Edge<?>) o;
        return connection.equals(edge.connection);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(connection);
    }

    @Override
    public String toString() {
        return connection.toString();
    }
}
