package space.earlygrey.simplegraphs;


import java.util.Objects;

public abstract class Connection<V> implements Suppliable {

    static final float DEFAULT_WEIGHT = 1;

    Node<V> a, b;
    float weight = DEFAULT_WEIGHT;
    final Edge<V> edge = new Edge<>(this);
    int index;
    boolean isFree;

    @Override
    public void free() {
        a = null;
        b = null;
        isFree = true;
    }

    void set(Node<V> a, Node<V> b, float weight) {
        this.a = a;
        this.b = b;
        this.weight = weight;
    }

    boolean isIncident(Node<V> v) {
        return a.equals(v) || b.equals(v);
    }

    float getWeight() {
        return weight;
    }

    void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public boolean isFree() {
        return isFree;
    }

    @Override
    public void setIndex(int i) {
        this.index = i;
    }

    static class DirectedConnection<V> extends Connection<V>  {

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
            return (int) (Objects.hashCode(a) * 0xC13FA9A902A6328FL + Objects.hashCode(b) * 0x91E10DA5C79E7B1DL >>> 32);
        }

        @Override
        public String toString() {
            return "{" + a + " -> " + b +'}';
        }

    }

    static class UndirectedConnection<V> extends Connection<V> {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Connection connection = (Connection) o;
            if ((a.equals(connection.a) && b.equals(connection.b))
                    || (a.equals(connection.b) && b.equals(connection.a))) return true;
            return false;
        }

        @Override
        public int hashCode() {
            return (int) ((Objects.hashCode(a) ^ Objects.hashCode(b)) * 0x9E3779B97F4A7C15L >>> 32);
        }

        @Override
        public String toString() {
            return "{" + a + ", " + b +'}';
        }
    }

}
