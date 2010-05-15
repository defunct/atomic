package com.goodworkalan.atomic;

import java.util.concurrent.atomic.AtomicReference;

public class CharacterTree<V> {
    /** The root node. */
    public AtomicReference<Node<V>> root = new AtomicReference<Node<V>>();
    
    /** The value associated with empty strings. */
    public AtomicReference<V> emptyValue = new AtomicReference<V>();

    public V getLast(CharSequence charSequence) {
        V last = emptyValue.get();
        Node<V> current = root.get();
        int i = 0, stop = charSequence.length();
        while (i < stop && current != null) {
            char ch = charSequence.charAt(i);
            char compare = current.ch;
            if (compare < ch) {
                Node<V> left = current.left.get();
                if (left == null) {
                    return null;
                }       
                current = left;
            } else if (compare > ch) {
                Node<V> right = current.right.get();
                if (right == null) {
                    return null;
                }
                current = right;
            } else {
                i++;
                V candidate = current.value.get();
                if (candidate != null) {
                    last = candidate;
                }
                current = current.center.get();
            }
        }
        return last;
    }

    public V get(CharSequence charSequence) {
        if (charSequence.length() == 0) {
            return emptyValue.get();
        } 
        Node<V> current = root.get();
        int i = 0, stop = charSequence.length();
        Node<V> node = null;
        while (i < stop && current != null) {
            char ch = charSequence.charAt(i);
            char compare = current.ch;
            if (compare < ch) {
                Node<V> left = current.left.get();
                if (left == null) {
                    return null;
                }       
                current = left;
            } else if (compare > ch) {
                Node<V> right = current.right.get();
                if (right == null) {
                    return null;
                }
                current = right;
            } else {
                i++;
                node = current;
                current = current.center.get();
            }
        }
        if (i == stop) {
            return node.value.get();
        }
        return null;
    }
    
    private AtomicReference<V> getValue(String key) {
        AtomicReference<V> found;
        if (key.length() == 0) {
            found = emptyValue;
        } else {
            Node<V> node = null;
            AtomicReference<Node<V>> current = root;
            int i = 0, stop = key.length();
            while (i < stop) {
                char ch = key.charAt(i);
                while ((node = current.get()) == null) {
                    current.set(new Node<V>(ch));
                }
                char compare = node.ch;
                if (compare < ch) {
                    current = node.left;
                } else if (compare > ch) {
                    current = node.right;
                } else {
                    current = node.center;
                    i++;
                }
            }
            found = node.value;
        }
        return found;
    }
       
    public boolean putIf(String key, V expected, V value) {
        return getValue(key).compareAndSet(expected, value);
    }
    
    public V put(String key, V value) {
        return getValue(key).getAndSet(value);
    }
    
    private final static class Node<V> {
        public AtomicReference<Node<V>> left = new AtomicReference<Node<V>>();
        
        public AtomicReference<Node<V>> right = new AtomicReference<Node<V>>();
        
        public AtomicReference<Node<V>> center = new AtomicReference<Node<V>>();
        
        public char ch;
        
        public AtomicReference<V> value = new AtomicReference<V>();
        
        public Node(char ch) {
            this.ch = ch;
        }
    }
}
