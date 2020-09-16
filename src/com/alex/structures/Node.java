package com.alex.structures;

import java.io.Serializable;

public class Node<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    // PROPIEDADES DE NODO SIMPLE
    LinkedList<T> list;
    Node<T> next;
    T value;

    // VALORES SIMPLES
    public Node(T item) {
        this.value = item;
        list = new LinkedList<>();
    }
}