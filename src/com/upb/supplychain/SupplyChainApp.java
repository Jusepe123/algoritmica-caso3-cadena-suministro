package com.upb.supplychain;

import java.util.List;
import java.util.stream.Collectors;

/** Punto de entrada para el Caso 3: cadena de suministro. */
public final class SupplyChainApp {
    public static final int FACTORY = 0;
    public static final int B1 = 1;
    public static final int B2 = 2;
    public static final int B3 = 3;
    public static final int DISTRIBUTION_CENTER = 4;
    private static final String[] NAMES = {"Fábrica (s)", "B1", "B2", "B3", "Centro (t)"};

    private SupplyChainApp() { }

    public static Graph buildSupplyChain() {
        Graph network = new Graph(5);
        network.addEdge(FACTORY, B1, 100);
        network.addEdge(FACTORY, B2, 120);
        network.addEdge(B1, B2, 60);
        network.addEdge(B1, B3, 80);
        network.addEdge(B1, DISTRIBUTION_CENTER, 50);
        network.addEdge(B2, B3, 70);
        network.addEdge(B2, DISTRIBUTION_CENTER, 60);
        network.addEdge(B3, DISTRIBUTION_CENTER, 90);
        return network;
    }

    public static void main(String[] args) {
        Graph network = buildSupplyChain();
        EdmondsKarp algorithm = new EdmondsKarp(network, FACTORY, DISTRIBUTION_CENTER);
        int maximum = algorithm.maxFlow();

        System.out.println("=== Caso 3: Cadena de Suministro ===");
        System.out.println("\nCaminos aumentantes encontrados (BFS):");
        int iteration = 1;
        for (EdmondsKarp.Augmentation augmentation : algorithm.getAugmentations()) {
            System.out.printf("%d. %s | cuello de botella: %d | flujo acumulado: %d%n",
                    iteration++, formatPath(augmentation.getPath()), augmentation.getBottleneck(),
                    augmentation.getTotalFlow());
        }

        System.out.println("\nFlujo final por tramo:");
        for (Edge edge : network.getOriginalEdges()) {
            System.out.printf("%-20s %d/%d (residual: %d)%n",
                    NAMES[edge.getFrom()] + " -> " + NAMES[edge.getTo()], edge.getFlow(),
                    edge.getCapacity(), edge.getResidualCapacity());
        }
        System.out.println("\nFlujo máximo: " + maximum + " unidades/día");
        System.out.println("Corte mínimo: B1->t (50) + B2->t (60) + B3->t (90) = 200.");
    }

    private static String formatPath(List<Integer> path) {
        return path.stream().map(vertex -> NAMES[vertex]).collect(Collectors.joining(" -> "));
    }
}
