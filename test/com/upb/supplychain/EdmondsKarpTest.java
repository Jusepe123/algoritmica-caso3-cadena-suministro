package com.upb.supplychain;

/** Pruebas autocontenidas: no requieren descargar JUnit. */
public final class EdmondsKarpTest {
    public static void main(String[] args) {
        testSupplyChainMaximumFlow();
        testDisconnectedSink();
        testSingleEdge();
        testResidualReverseEdge();
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

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) throw new AssertionError(message + ": esperado " + expected + ", obtenido " + actual);
    }
}
