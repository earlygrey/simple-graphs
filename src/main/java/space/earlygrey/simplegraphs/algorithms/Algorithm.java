package space.earlygrey.simplegraphs.algorithms;

public abstract class Algorithm<V> {

    protected final int id;

    protected Algorithm(int id) {
        this.id = id;
    }

    public abstract boolean update();

    public abstract boolean isFinished();

    public void finish() {
        while (!update());
    }

}
