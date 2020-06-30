package space.earlygrey.simplegraphs;

class Connection<V> extends Edge<V> implements Suppliable {

    static final float DEFAULT_WEIGHT = 1;

    Node<V> a, b;
    float weight = DEFAULT_WEIGHT;
    int index;
    boolean isFree;

    @Override
    public void free() {
        a = null;
        b = null;
        isFree = true;
    }

    @Override
    void set(Node<V> a, Node<V> b, float weight) {
        this.a = a;
        this.b = b;
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

    @Override
    public V getA() {
        return a.object;
    }

    @Override
    public V getB() {
        return b.object;
    }

    @Override
    public float getWeight() {
        return weight;
    }

    @Override
    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    Node<V> getNodeA() {
        return a;
    }

    @Override
    Node<V> getNodeB() {
        return b;
    }

    static class DirectedConnection<V> extends Connection<V> {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Connection edge = (Connection) o;
            // this assumes a and b are non-null when equals() is called.
            return a.equals(edge.a) && b.equals(edge.b);
        }

        @Override
        public int hashCode() {
            return (int) (a.hashCode() * 0xC13FA9A902A6328FL + b.hashCode() * 0x91E10DA5C79E7B1DL >>> 32);
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
            Connection edge = (Connection) o;
            // this assumes a and b are non-null when equals() is called.
            return (a.equals(edge.a) && b.equals(edge.b))
                    || (a.equals(edge.b) && b.equals(edge.a));
        }

        @Override
        public int hashCode() {
            return (int) ((a.hashCode() + b.hashCode()) * 0x9E3779B97F4A7C15L >>> 32);
        }

        @Override
        public String toString() {
            return "{" + a + " <> " + b +'}';
        }
    }
}
