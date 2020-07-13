package space.earlygrey.simplegraphs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

class NodeMap<V> {

    final Graph<V> graph;
    Node<V>[] table;

    Node<V> head, tail;

    int size = 0;
    int threshold = -1;
    static final int MIN_SIZE = 32;

    VertexCollection<V> vertexCollection;
    NodeCollection<V> nodeCollection;

    static final String MODIFY_EXCEPTION = "You cannot modify this list - use the Graph object.";

    public NodeMap(Graph<V> graph) {
        this.graph = graph;
        table = new Node[MIN_SIZE / 2];
        checkLength();
        vertexCollection = new VertexCollection<>(this);
        nodeCollection = new NodeCollection<>(this);
    }

    Node<V> get(V v) {
        int objectHash = v.hashCode(), hash = hash(objectHash);
        int i = getIndex(hash);
        Node<V> bucketHead = table[i];
        if (bucketHead == null) {
            return null;
        }

        Node<V> currentNode = bucketHead;
        while (currentNode != null) {
            if (v.equals(currentNode.object)) return currentNode;
            currentNode = currentNode.nextInBucket;
        }

        return null;
    }

    boolean contains(V v) {
        return get(v) != null;
    }

    Node<V> put(V v) {
        int objectHash = v.hashCode(), hash = hash(objectHash);
        int i = getIndex(hash);
        Node<V> bucketHead = table[i];
        if (bucketHead == null) {
            if (checkLength()) i = getIndex(hash);
            bucketHead = new Node<>(v, graph, objectHash);
            bucketHead.mapHash = hash;
            table[i] = bucketHead;
            size++;
            addToList(bucketHead);
            return bucketHead;
        }

        Node<V> currentNode = bucketHead, previousNode = null;
        while (currentNode != null) {
            if (v.equals(currentNode.object)) return null;
            previousNode = currentNode;
            currentNode = currentNode.nextInBucket;
        }

        checkLength();
        currentNode = new Node<>(v, graph, objectHash);
        currentNode.mapHash = hash;
        previousNode.nextInBucket = currentNode;
        size++;
        addToList(currentNode);
        return currentNode;
    }

    void addToList(Node<V> node) {
        if (head == null) {
            head = node;
            tail = node;
            return;
        }
        node.prevInOrder = tail;
        tail.nextInOrder = node;
        tail = node;
    }

    Node<V> remove(V v) {
        int objectHash = v.hashCode(), hash = hash(objectHash);
        int i = getIndex(hash);
        Node<V> currentNode = table[i];

        // currentNode should not be null if v is in map

        if (currentNode != null && v.equals(currentNode.object)) {
            table[i] = null;
            size--;
            removeFromList(currentNode);
            return currentNode;
        }

        Node<V> previousNode = null;
        while (currentNode != null) {
            if (v.equals(currentNode.object)) {
                if (previousNode != null) previousNode.nextInBucket = currentNode.nextInBucket;
                size--;
                removeFromList(currentNode);
                return currentNode;
            }
            previousNode = currentNode;
            currentNode = currentNode.nextInBucket;
        }

        return null;
    }

    void removeFromList(Node<V> node) {
        if (head == node) {
            head = node.nextInOrder;
            if (head != null) head.prevInOrder = null;
            return;
        }
        if (tail == node) {
            tail = node.prevInOrder;
            if (tail != null) tail.nextInOrder = null;
            return;
        }
        node.prevInOrder.nextInOrder = node.nextInOrder;
        node.nextInOrder.prevInOrder = node.prevInOrder;
    }

    boolean checkLength() {
        if (size > threshold) {
            int newLength = 2 * table.length;
            Node<V>[] oldTable = table, newTable = new Node[newLength];
            for (int i = 0; i < oldTable.length; i++) {
                if (oldTable[i] != null) {
                    Node<V> tail1 = null, tail2 = null, current = oldTable[i];
                    while (current != null) {
                        int newIndex = getIndex(current.mapHash, newLength);
                        if (newIndex == i) {
                            if (tail1 == null) newTable[newIndex] = current;
                            else tail1.nextInBucket = current;
                            tail1 = current;
                        } else {
                            if (tail2 == null) newTable[newIndex] = current;
                            else tail2.nextInBucket = current;
                            tail2 = current;
                        }
                        Node<V> next = current.nextInBucket;
                        current.nextInBucket = null;
                        current = next;

                    }
                }
            }
            threshold = (int) (0.7 * newLength);
            table = newTable;
            return true;
        }
        return false;
    }

    void clear() {
        Arrays.fill(table, null);
        size = 0;
    }


    int getIndex(int hash) {
        return getIndex(hash, table.length);
    }
    static int getIndex(int hash,  int length) {
        return hash & (length - 1);
    }

    static int hash(Object v) {
        int hashcode = v.hashCode();
        return hashcode ^ (hashcode >>> 16);
    }

    static class NodeIterator<V> implements Iterator<Node<V>> {

        final NodeMap<V> nodeMap;
        Node<V> current;

        NodeIterator(NodeMap<V> nodeMap) {
            this.nodeMap = nodeMap;
            current = nodeMap.head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Node<V> next() {
            if(hasNext()){
                Node<V> next = current;
                current = current.nextInOrder;
                return next;
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("You cannot modify this list - use the Graph object.");
        }

    }



    // sorting
    // adapted from https://www.geeksforgeeks.org/merge-sort-for-linked-list/

    void sort(Comparator<V> comparator) {
        head = mergeSort(head, comparator);
    }

    Node<V> mergeSort(Node<V> h, Comparator<V> comparator) {

        if (h == null || h.nextInOrder == null) return h;

        Node<V> middle = getMiddle(h);
        Node<V> middleNext = middle.nextInOrder;

        middle.nextInOrder = null;

        Node<V> left = mergeSort(h, comparator);
        Node<V> right = mergeSort(middleNext, comparator);

        Node<V> newHead = sortedMerge(left, right, comparator);
        return newHead;
    }

    Node<V> sortedMerge(Node<V> a, Node<V> b, Comparator<V> comparator) {

        if (a == null) return b;
        if (b == null) return a;

        Node<V> newHead;

        if (comparator.compare(a.object, b.object) < 0) {
            newHead = a;
            newHead.nextInOrder = sortedMerge(a.nextInOrder, b, comparator);
        } else {
            newHead = b;
            newHead.nextInOrder = sortedMerge(a, b.nextInOrder, comparator);
        }
        return newHead;
    }

    Node<V> getMiddle(Node<V> head) {
        if (head == null) return null;

        Node<V> slow = head, fast = head;

        while (fast.nextInOrder != null && fast.nextInOrder.nextInOrder != null) {
            slow = slow.nextInOrder;
            fast = fast.nextInOrder.nextInOrder;
        }
        return slow;
    }


}