package space.earlygrey.simplegraphs;

/*
 NOTE - This class was adapted from the original, which appears as the BinaryHeap class found in libgdx.
 Original appears here: https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/utils/BinaryHeap.java
 Generics and the BinaryHeap.Node class have been removed and replaced with the simple-graphs Node class.
 Safety checks and exceptions have been removed.
 */

/*
 *******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUNode WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

/** A binary heap that stores nodes which each have a float value and are sorted either lowest first or highest first.
 * @author Nathan Sweet */
public class BinaryHeap<V> {
    public int size;

    private Node<V>[] nodes;

    public BinaryHeap() {
        this(16);
    }

    public BinaryHeap(int capacity) {
        nodes = (Node<V>[]) (new Object[capacity]);
    }

    /**
     * Adds the node to the heap using its current value. The node should not already be in the heap.
     */
    public Node<V> add(Node<V> node) {
        // Expand if necessary.
        if (size == nodes.length) {
            Node<V>[] newNodes = (Node<V>[]) (new Object[size << 1]);
            System.arraycopy(nodes, 0, newNodes, 0, size);
            nodes = newNodes;
        }
        // Insert at end and bubble up.
        node.heapIndex = size;
        nodes[size] = node;
        up(size++);
        return node;
    }

    /**
     * Sets the node's value and adds it to the heap. The node should not already be in the heap.
     */
    public Node<V> add(Node<V> node, float value) {
        node.heapValue = value;
        return add(node);
    }

    /**
     * Returns true if the heap contains the specified node.
     *
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     */
    public boolean contains(Node<? extends V> node, boolean identity) {
        if (identity) {
            for (Node<? extends V> n : nodes)
                if (n == node) return true;
        } else {
            for (Node<? extends V> other : nodes)
                if (other.equals(node)) return true;
        }
        return false;
    }

    /**
     * Returns the first item in the heap. This is the item with the lowest value (or highest value if this heap is configured as
     * a max heap).
     */
    public Node<V> peek() {
        return nodes[0];
    }

    /**
     * Removes the first item in the heap and returns it. This is the item with the lowest value (or highest value if this heap is
     * configured as a max heap).
     */
    public Node<V> pop() {
        return remove(0);
    }

    public Node<V> remove(Node<V> node) {
        return remove(node.heapIndex);
    }

    private Node<V> remove(int index) {
        Node<V>[] nodes = this.nodes;
        Node<V> removed = nodes[index];
        nodes[index] = nodes[--size];
        nodes[size] = null;
        if (size > 0 && index < size) down(index);
        return removed;
    }

    /**
     * Returns true if the heap has one or more items.
     */
    public boolean notEmpty() {
        return size > 0;
    }

    /**
     * Returns true if the heap is empty.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        Node<V>[] nodes = this.nodes;
        for (int i = 0, n = size; i < n; i++)
            nodes[i] = null;
        size = 0;
    }

    /**
     * Changes the value of the node, which should already be in the heap.
     */
    public void setValue(Node<V> node, float value) {
        float oldValue = node.heapValue;
        node.heapValue = value;
        if (value < oldValue)
            up(node.heapIndex);
        else
            down(node.heapIndex);
    }

    private void up(int index) {
        Node<V>[] nodes = this.nodes;
        Node<V> node = nodes[index];
        float value = node.heapValue;
        while (index > 0) {
            int parentIndex = (index - 1) >> 1;
            Node<V> parent = nodes[parentIndex];
            if (value < parent.heapValue) {
                nodes[index] = parent;
                parent.heapIndex = index;
                index = parentIndex;
            } else
                break;
        }
        nodes[index] = node;
        node.heapIndex = index;
    }

    private void down(int index) {
        Node<V>[] nodes = this.nodes;
        int size = this.size;

        Node<V> node = nodes[index];
        float value = node.heapValue;

        while (true) {
            int leftIndex = 1 + (index << 1);
            if (leftIndex >= size) break;
            int rightIndex = leftIndex + 1;

            // Always has a left child.
            Node<V> leftNode = nodes[leftIndex];
            float leftValue = leftNode.heapValue;

            // May have a right child.
            Node<V> rightNode;
            float rightValue;
            if (rightIndex >= size) {
                rightNode = null;
                rightValue = Float.POSITIVE_INFINITY;
            } else {
                rightNode = nodes[rightIndex];
                rightValue = rightNode.heapValue;
            }

            // The smallest of the three values is the parent.
            if (leftValue < rightValue) {
                if (leftValue == value || leftValue > value) break;
                nodes[index] = leftNode;
                leftNode.heapIndex = index;
                index = leftIndex;
            } else {
                if (rightValue >= value) break;
                nodes[index] = rightNode;
                rightNode.heapIndex = index;
                index = rightIndex;
            }
        }

        nodes[index] = node;
        node.heapIndex = index;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BinaryHeap)) return false;
        BinaryHeap<V> other = (BinaryHeap<V>) obj;
        if (other.size != size) return false;
        Node<V>[] nodes1 = this.nodes, nodes2 = other.nodes;
        for (int i = 0, n = size; i < n; i++)
            if (nodes1[i].heapValue != nodes2[i].heapValue) return false;
        return true;
    }

    public int hashCode() {
        int h = 1 + 31 * size;
        for (int i = 0, n = size; i < n; i++)
            h = (h ^ h >>> 11) + Float.floatToIntBits(nodes[i].heapValue);
        return h;
    }

    public String toString() {
        if (size == 0) return "[]";
        Node<V>[] nodes = this.nodes;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append('[');
        buffer.append(nodes[0].heapValue);
        for (int i = 1; i < size; i++) {
            buffer.append(", ");
            buffer.append(nodes[i].heapValue);
        }
        buffer.append(']');
        return buffer.toString();
    }
}

