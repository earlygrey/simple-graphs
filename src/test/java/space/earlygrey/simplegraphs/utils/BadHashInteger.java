package space.earlygrey.simplegraphs.utils;

public class BadHashInteger {

    int i;

    public BadHashInteger(int i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadHashInteger that = (BadHashInteger) o;
        return i == that.i;
    }

    public int value() {
        return i;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return i + "";
    }
}
