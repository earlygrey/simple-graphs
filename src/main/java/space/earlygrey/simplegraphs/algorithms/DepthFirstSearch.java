package space.earlygrey.simplegraphs.algorithms;

import java.util.ArrayDeque;

import space.earlygrey.simplegraphs.Array;
import space.earlygrey.simplegraphs.Connection;
import space.earlygrey.simplegraphs.Node;
import space.earlygrey.simplegraphs.utils.SearchProcessor;

public class DepthFirstSearch<V> extends Algorithm<V> {

    private SearchProcessor<V> processor;
    private SearchStep<V> step = new SearchStep<>();

    private final ArrayDeque<Node<V>> queue;

    public DepthFirstSearch(int id, Node<V> start, SearchProcessor<V> processor) {
        super(id);
        this.processor = processor;
        queue = new ArrayDeque<>();
        start.resetAlgorithmAttribs(id);
        queue.add(start);
        start.setSeen(true);
    }

    @Override
    public boolean update() {
        if (isFinished()) return true;

        Node<V> v = queue.pollFirst();
        //System.out.println("poll " + v);
        if (processor != null) {
            step.prepare(v);
            processor.accept(step);
            if (step.terminate) {
                queue.clear();
                return true;
            }
            if (step.ignore) return isFinished();
        }
        Array<Connection<V>> outEdges = v.getOutEdges();
        for (Connection<V> e : outEdges) {
            Node<V> w = e.getNodeB();
            w.resetAlgorithmAttribs(id);
            if (!w.isSeen()) {
                w.setIndex(v.getIndex() + 1);
                w.setDistance(v.getDistance() + e.getWeight());
                w.setConnection(e);
                w.setSeen(true);
                queue.addFirst(w);
               // System.out.println("add " + w);
            }
        }
       // System.out.println("queue " + queue);
        return isFinished();
    }

    @Override
    public boolean isFinished() {
        return queue.isEmpty();
    }
/*
    void depthFirstSearch(final Node<V> v, final SearchProcessor<V> processor) {
        v.resetAlgorithmAttribs(id);
        v.setDistance(0);
        recursiveDepthFirstSearch(v, processor, 0, processor != null ? new SearchStep<>() : null);
    }

    boolean recursiveDepthFirstSearch(Node<V> v, SearchProcessor<V> processor, int depth, SearchStep<V> step) {
        if (processor != null) {
            step.prepare(v);
            processor.accept(step);
            if (step.terminate) return true;
            if (step.ignore) return false;
        }
        v.setProcessed(true);
        int n = v.getOutEdges().size();
        for (int i = 0; i < n; i++) {
            Connection<V> e = v.getOutEdges().get(i);
            Node<V> w = e.getNodeB();
            w.resetAlgorithmAttribs(id);
            if (!w.isProcessed()) {
                w.setIndex(depth + 1);
                w.setDistance(v.getDistance() + e.getWeight());
                w.setConnection(e);
                if (recursiveDepthFirstSearch(w, processor, depth + 1, step)) {
                    return true;
                }
            }
        }
        return false;
    }*/

}
