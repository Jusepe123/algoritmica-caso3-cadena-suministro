package com.upb.supplychain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Red dirigida con aristas residuales explícitas. */
public final class Graph {
    private final List<List<Edge>> adjacency;
    private final List<Edge> originalEdges;

    public Graph(int vertices) {
        if (vertices <= 0) {
            throw new IllegalArgumentException("El grafo debe tener al menos un vértice");
        }
        adjacency = new ArrayList<>();
        originalEdges = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjacency.add(new ArrayList<>());
        }
    }

    public int getVertices() { return adjacency.size(); }

    public void addEdge(int from, int to, int capacity) {
        validateVertex(from);
        validateVertex(to);
        Edge forward = new Edge(from, to, capacity);
        Edge backward = new Edge(to, from, 0);
        forward.setResidual(backward);
        backward.setResidual(forward);
        adjacency.get(from).add(forward);
        adjacency.get(to).add(backward);
        originalEdges.add(forward);
    }

    public List<Edge> getEdgesFrom(int vertex) {
        validateVertex(vertex);
        return Collections.unmodifiableList(adjacency.get(vertex));
    }

    /** Solo las aristas que modelan tramos físicos, sin las inversas residuales. */
    public List<Edge> getOriginalEdges() {
        return Collections.unmodifiableList(originalEdges);
    }

    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= getVertices()) {
            throw new IllegalArgumentException("Vértice inválido: " + vertex);
        }
    }
}
