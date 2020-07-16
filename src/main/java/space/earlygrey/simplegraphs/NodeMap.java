package space.earlygrey.simplegraphs;

import java.util.Comparator;
import java.util.Iterator;

class NodeMap<V> {

    final Graph<V> graph;

    // array of "buckets"
    Node<V>[] table;

    // linked list of map entries
    Node<V> head, tail;

    int size = 0;
    int occupiedBuckets = 0;
    static final int MIN_TABLE_LENGTH = 32;
    static final float RESIZE_THRESHOLD = 0.7f;
    int threshold = (int) (RESIZE_THRESHOLD * MIN_TABLE_LENGTH);

    // collections for returning to the user
    VertexCollection<V> vertexCollection;
    NodeCollection<V> nodeCollection;



    public NodeMap(Graph<V> graph) {
        this.graph = graph;
        table = new Node[MIN_TABLE_LENGTH];
        vertexCollection = new VertexCollection<>(this);
        nodeCollection = new NodeCollection<>(this);
    }

    /**
     * Return the `Node<V>` to which the vertex v is mapped, or null if not in the map.
     */
    Node<V> get(V v) {
        int objectHash = v.hashCode(), hash = hash(objectHash);
        int i = getIndex(hash);
        Node<V> bucketHead = table[i];
        if (bucketHead == null) return null;

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

    /**
     * Create a Node<V> and associate the vertex v with it.
     * @return the `Node<V>` if v is not in the map, or null if it already is.
     */
    Node<V> put(V v) {
        // checking the size before adding might resize even if v is already in the map,
        // but it will only be off by one
        checkLength(1);

        int objectHash = v.hashCode(), hash = hash(objectHash);
        int i = getIndex(hash);
        Node<V> bucketHead = table[i];
        if (bucketHead == null) {
            bucketHead = new Node<>(v, graph, objectHash);
            bucketHead.mapHash = hash;
            table[i] = bucketHead;
            size++;
            occupiedBuckets++;
            addToList(bucketHead);
            return bucketHead;
        }

        Node<V> currentNode = bucketHead, previousNode = null;
        while (currentNode != null) {
            if (v.equals(currentNode.object)) return null;
            previousNode = currentNode;
            currentNode = currentNode.nextInBucket;
        }

        currentNode = new Node<>(v, graph, objectHash);
        currentNode.mapHash = hash;
        previousNode.nextInBucket = currentNode;
        size++;
        addToList(currentNode);
        return currentNode;
    }

    /**
     * Add the node to the tail of the linked list.
     */
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

    /**
     * Insert the node at a specific point in the linked list.
     */
    void insertIntoList(Node<V> v, Node<V> at, boolean before) {
        if (before) {
            v.nextInOrder = at;
            v.prevInOrder = at.prevInOrder;
            at.prevInOrder = v;
            if (v.prevInOrder != null) v.prevInOrder.nextInOrder = v;
            else head = v;
        } else {
            v.prevInOrder = at;
            v.nextInOrder = at.nextInOrder;
            at.nextInOrder = v;
            if (v.nextInOrder != null) v.nextInOrder.prevInOrder = v;
            else tail = v;
        }
    }
    void insertIntoListAfter(Node<V> v, Node<V> at) {
        insertIntoList(v, at, false);
    }
    void insertIntoListBefore(Node<V> v, Node<V> at) {
        insertIntoList(v, at, true);
    }

    /**
     * Remove the vertex V from the map.
     * @return the `Node<V>` that v was associated with, or null if v is not in the map.
     */
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

    /**
     * Remove the node from the linked list.
     */
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

    /**
     * Increase the length of the table if the size exceeds the capacity,
     * and recalculate the indices.
     */

    boolean checkLength(int sizeChange) {
        if (size + sizeChange > threshold) {
            occupiedBuckets = 0;
            int newLength = 2 * table.length;
            Node<V>[] oldTable = table, newTable = new Node[newLength];
            for (int i = 0; i < oldTable.length; i++) {
                if (oldTable[i] != null) {
                    Node<V> tail1 = null, tail2 = null, current = oldTable[i];
                    while (current != null) {
                        int newIndex = getIndex(current.mapHash, newLength);
                        if (newIndex == i) {
                            if (tail1 == null) {
                                newTable[newIndex] = current;
                                occupiedBuckets++;
                            }
                            else {
                                tail1.nextInBucket = current;
                            }
                            tail1 = current;
                        } else {
                            if (tail2 == null) {
                                newTable[newIndex] = current;
                                occupiedBuckets++;
                            }
                            else {
                                tail2.nextInBucket = current;
                            }
                            tail2 = current;
                        }
                        Node<V> next = current.nextInBucket;
                        current.nextInBucket = null;
                        current = next;

                    }
                }
            }
            threshold = (int) (RESIZE_THRESHOLD * newLength);
            table = newTable;
            return true;
        }
        return false;
    }

    void clear() {
        table = new Node[table.length];
        size = 0;
        occupiedBuckets = 0;
        head = null;
        tail = null;
    }

    /**
     * Get the table index of the Node<V> which has the given hash.
     */
    int getIndex(int hash) {
        return getIndex(hash, table.length);
    }

    static int getIndex(int hash,  int length) {
        return hash & (length - 1);
    }

    /**
     * Get the hash used to calculate the index in the table at which the Node<V> associated with
     * v would be held.
     */
    static int hash(Object v) {
        int hashcode = v.hashCode();
        return hashcode ^ (hashcode >>> 16);
    }

    /**
     * Iterates in the order of the linked list. Used by teh vertex and Node<V> collections.
     */
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
        if (size < 2) return;

        head = mergeSort(head, comparator);

        // reverse list (I could also toggle a flag in the iterator to flip direction instead)
        Iterator<Node<V>> iterator = nodeCollection.iterator();
        Node<V> node = null, prev = null;
        while (iterator.hasNext()) {
            node = iterator.next();
            node.prevInOrder = prev;
            prev = node;
        }
        tail = node;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NodeMap - size: "+size+", table length: "+table.length+", occupiedBuckets: "+occupiedBuckets+"\n");
        sb.append("--------------\n");

        for (int i = 0; i < table.length; i++) {
            sb.append(i+"]  ");
            Node<V> node = table[i];
            while (node != null) {
                sb.append(node);
                if (node.nextInBucket != null) sb.append(" -> ");
                node = node.nextInBucket;
            }
            sb.append("\n");
        }

        return sb.append("--------------").toString();
    }


}
