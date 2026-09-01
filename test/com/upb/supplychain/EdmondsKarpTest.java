package com.upb.supplychain;

/** Pruebas autocontenidas: no requieren descargar JUnit. */
public final class EdmondsKarpTest {
    public static void main(String[] args) {
        testSupplyChainMaximumFlow();
        testSupplyChainFlowProperties();
        testDisconnectedSink();
        testSingleEdge();
        testResidualReverseEdge();
        testAlgorithmUsesReverseEdge();
        System.out.println("Todas las pruebas pasaron.");
    }

    private static void testSupplyChainMaximumFlow() {
        Graph graph = SupplyChainApp.buildSupplyChain();
        int result = new EdmondsKarp(graph, SupplyChainApp.FACTORY,
                SupplyChainApp.DISTRIBUTION_CENTER).maxFlow();
        assertEquals(200, result, "El caso de suministro debe transportar 200");
    }

    private static void testDisconnectedSink() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 10);
        assertEquals(0, new EdmondsKarp(graph, 0, 2).maxFlow(), "Sin ruta el flujo es cero");
    }

    private static void testSupplyChainFlowProperties() {
        Graph graph = SupplyChainApp.buildSupplyChain();
        EdmondsKarp algorithm = new EdmondsKarp(graph, SupplyChainApp.FACTORY,
                SupplyChainApp.DISTRIBUTION_CENTER);
        assertEquals(200, algorithm.maxFlow(), "El flujo máximo debe ser 200");
        assertEquals(200, algorithm.maxFlow(), "Una segunda consulta debe conservar el resultado");

        for (Edge edge : graph.getOriginalEdges()) {
            assertTrue(edge.getFlow() >= 0 && edge.getFlow() <= edge.getCapacity(),
                    "Cada tramo debe respetar su capacidad");
        }

        for (int vertex = SupplyChainApp.B1; vertex <= SupplyChainApp.B3; vertex++) {
            int incoming = 0;
            int outgoing = 0;
            for (Edge edge : graph.getOriginalEdges()) {
                if (edge.getTo() == vertex) incoming += edge.getFlow();
                if (edge.getFrom() == vertex) outgoing += edge.getFlow();
            }
            assertEquals(incoming, outgoing, "Debe conservarse el flujo en la bodega " + vertex);
        }

        boolean[] sourceSide = algorithm.getReachableFromSource();
        int cutCapacity = 0;
        for (Edge edge : graph.getOriginalEdges()) {
            if (sourceSide[edge.getFrom()] && !sourceSide[edge.getTo()]) {
                cutCapacity += edge.getCapacity();
            }
        }
        assertEquals(200, cutCapacity, "El corte mínimo calculado debe valer 200");
    }

    private static void testSingleEdge() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1, 37);
        assertEquals(37, new EdmondsKarp(graph, 0, 1).maxFlow(), "Una arista limita el flujo");
    }

    /* Comprueba que una arista residual inversa permite deshacer flujo. */
    private static void testResidualReverseEdge() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1, 10);
        Edge forward = graph.getEdgesFrom(0).get(0);
        forward.augment(7);
        forward.getResidual().augment(7);
        assertEquals(0, forward.getFlow(), "La arista inversa debe cancelar el flujo");
        assertEquals(10, forward.getResidualCapacity(), "La capacidad debe restaurarse");
    }

    /* Fuerza al algoritmo a cancelar una elección anterior para alcanzar el máximo. */
    private static void testAlgorithmUsesReverseEdge() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1, 1); // s -> A
        graph.addEdge(0, 2, 1); // s -> B
        graph.addEdge(1, 3, 1); // A -> C: se usa y luego se cancela
        graph.addEdge(1, 4, 1); // A -> D
        graph.addEdge(2, 3, 1); // B -> C
        graph.addEdge(3, 5, 1); // C -> t
        graph.addEdge(4, 5, 1); // D -> t

        Edge correctedEdge = findOriginalEdge(graph, 1, 3);
        EdmondsKarp algorithm = new EdmondsKarp(graph, 0, 5);
        assertEquals(2, algorithm.maxFlow(), "La corrección residual debe permitir flujo 2");
        assertEquals(0, correctedEdge.getFlow(), "El flujo inicial A->C debe quedar cancelado");
    }

    private static Edge findOriginalEdge(Graph graph, int from, int to) {
        for (Edge edge : graph.getOriginalEdges()) {
            if (edge.getFrom() == from && edge.getTo() == to) return edge;
        }
        throw new AssertionError("No existe la arista " + from + " -> " + to);
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) throw new AssertionError(message + ": esperado " + expected + ", obtenido " + actual);
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
