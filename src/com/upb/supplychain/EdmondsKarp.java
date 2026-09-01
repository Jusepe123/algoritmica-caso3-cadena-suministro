package com.upb.supplychain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

/** Implementación de Ford-Fulkerson que usa BFS (Edmonds-Karp). */
public final class EdmondsKarp {
    private final Graph graph;
    private final int source;
    private final int sink;
    private final List<Augmentation> augmentations = new ArrayList<>();

    public EdmondsKarp(Graph graph, int source, int sink) {
        this.graph = graph;
        if (source < 0 || source >= graph.getVertices() || sink < 0 || sink >= graph.getVertices()
                || source == sink) {
            throw new IllegalArgumentException("Fuente o sumidero inválidos");
        }
        this.source = source;
        this.sink = sink;
    }

    public int maxFlow() {
        int total = 0;
        Edge[] parent = new Edge[graph.getVertices()];
        while (bfs(parent)) {
            int bottleneck = Integer.MAX_VALUE;
            for (Edge edge = parent[sink]; edge != null; edge = parent[edge.getFrom()]) {
                bottleneck = Math.min(bottleneck, edge.getResidualCapacity());
            }

            List<Integer> path = new ArrayList<>();
            path.add(sink);
            for (Edge edge = parent[sink]; edge != null; edge = parent[edge.getFrom()]) {
                edge.augment(bottleneck);
                path.add(edge.getFrom());
            }
            Collections.reverse(path);
            total += bottleneck;
            augmentations.add(new Augmentation(path, bottleneck, total));
        }
        return total;
    }

    private boolean bfs(Edge[] parent) {
        Arrays.fill(parent, null);
        boolean[] visited = new boolean[graph.getVertices()];
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(source);
        visited[source] = true;

        while (!queue.isEmpty()) {
            int current = queue.remove();
            for (Edge edge : graph.getEdgesFrom(current)) {
                if (!visited[edge.getTo()] && edge.getResidualCapacity() > 0) {
                    parent[edge.getTo()] = edge;
                    visited[edge.getTo()] = true;
                    if (edge.getTo() == sink) return true;
                    queue.add(edge.getTo());
                }
            }
        }
        return false;
    }

    public List<Augmentation> getAugmentations() {
        return Collections.unmodifiableList(augmentations);
    }

    public static final class Augmentation {
        private final List<Integer> path;
        private final int bottleneck;
        private final int totalFlow;

        Augmentation(List<Integer> path, int bottleneck, int totalFlow) {
            this.path = Collections.unmodifiableList(new ArrayList<>(path));
            this.bottleneck = bottleneck;
            this.totalFlow = totalFlow;
        }
        public List<Integer> getPath() { return path; }
        public int getBottleneck() { return bottleneck; }
        public int getTotalFlow() { return totalFlow; }
    }
}
