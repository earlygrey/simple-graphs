package space.earlygrey.simplegraphs;

interface Pooled {
    int getIndex();
    void free();
    boolean isFree();
}
