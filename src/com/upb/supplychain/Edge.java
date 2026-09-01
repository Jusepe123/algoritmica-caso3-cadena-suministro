package com.upb.supplychain;

/**
 * Arista dirigida de una red de flujo. Cada arista tiene una arista residual
 * inversa asociada para poder deshacer flujo durante Edmonds-Karp.
 */
public final class Edge {
    private final int from;
    private final int to;
    private final int capacity;
    private int flow;
    private Edge residual;

    Edge(int from, int to, int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("La capacidad no puede ser negativa");
        }
        this.from = from;
        this.to = to;
        this.capacity = capacity;
    }

    void setResidual(Edge residual) {
        this.residual = residual;
    }

    public int getFrom() { return from; }
    public int getTo() { return to; }
    public int getCapacity() { return capacity; }
    public int getFlow() { return flow; }
    public Edge getResidual() { return residual; }

    /** Capacidad disponible en la red residual. */
    public int getResidualCapacity() {
        return capacity - flow;
    }

    /**
     * Envía flujo por esta arista y actualiza la inversa residual.
     */
    public void augment(int amount) {
        if (amount <= 0 || amount > getResidualCapacity()) {
            throw new IllegalArgumentException("Aumento de flujo inválido: " + amount);
        }
        flow += amount;
        residual.flow -= amount;
    }

    @Override
    public String toString() {
        return from + " -> " + to + " | flujo " + flow + "/" + capacity
                + " | residual " + getResidualCapacity();
    }
}
