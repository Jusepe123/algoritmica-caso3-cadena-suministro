# Documentación del Proyecto: Caso 3 - Cadena de Suministro

> **Nota:** este archivo conserva la planificación inicial. La versión final del
> proyecto está descrita en `README.md` y `docs/informe_tecnico.md`; la exposición
> final sin código se encuentra en `presentacion_piense.md`.

## Universidad Privada Boliviana
### Algorítmica II - Redes de Flujo

---

## 🎯 Visión General del Caso 3

**Cadena de Suministro: Fábrica a Centro de Distribución**

### Contexto del Problema
Una fábrica produce bienes que deben ser transportados a un centro de distribución final, pasando por bodegas regionales intermedias. El sistema de transporte tiene capacidades limitadas en cada tramo.

### Modelado de la Red

#### Nodos
- **Fuente (s)**: Fábrica principal de producción
- **Sumidero (t)**: Centro de distribución final
- **Nodos intermedios (bodegas)**: 3-4 bodegas regionales (definir)

#### Aristas y Capacidades Sugeridas

```
Estructura Propuesta:
s → B1: 100 unidades/día
s → B2: 120 unidades/día

B1 → B2: 60 unidades/día
B1 → B3: 80 unidades/día
B1 → t: 50 unidades/día

B2 → B3: 70 unidades/día
B2 → t: 60 unidades/día

B3 → t: 90 unidades/día
```

**Cuellos de botella identificados**:
- B1 → B2: capacidad limitada (60)
- B1 → t: capacidad limitada (50)

---

## 📊 Especificaciones Técnicas

### Implementación en Java

#### Estructura de Clases

```java
// Edge.java
class Edge {
    int from, to;
    int capacity;
    int flow;

    // Constructor y métodos getters/setters
    int getResidualCapacity();
    void updateFlow(int amount);
}

// Graph.java
class Graph {
    List<List<Edge>> adjacency;
    int vertices;

    // Métodos principales
    void addEdge(int from, int to, int capacity);
    void addResidualEdge();
    void printGraph();
}

// EdmondsKarp.java
class EdmondsKarp {
    Graph graph;
    int source, sink;

    // Algoritmo principal
    int maxFlow();
    boolean bfs(int[] parent);
    void augmentFlow(int[] parent, int bottleneck);
}

// SupplyChainApp.java
class SupplyChainApp {
    public static void main(String[] args) {
        // Configuración del caso específico
        Graph network = buildSupplyChain();
        EdmondsKarp algorithm = new EdmondsKarp(network, source, sink);
        int maxFlow = algorithm.maxFlow();
        // Output de resultados
    }
}
```

### Datos de Entrada
```java
// Formato de configuración
int vertices = 5; // s, B1, B2, B3, t
int[][] capacities = {
    // s  B1 B2 B3  t
    {0, 100, 120, 0, 0},   // s
    {0, 0, 60, 80, 50},    // B1
    {0, 0, 0, 70, 60},     // B2
    {0, 0, 0, 0, 90},      // B3
    {0, 0, 0, 0, 0}        // t
};
```

---

## 📝 Documentación del Código

### Sección de LLM

La declaración final sobre el uso de Codex y la validación realizada por el grupo
se encuentra en `docs/informe_tecnico.md`.
### Especificaciones de Implementación

#### 1. Algoritmo de Ford-Fulkerson/Edmonds-Karp
```java
public int maxFlow() {
    int maxFlow = 0;
    int[] parent = new int[vertices];

    while (bfs(parent)) {
        // Encontrar bottleneck
        int bottleneck = Integer.MAX_VALUE;
        for (int v = sink; v != source; v = parent[v]) {
            int u = parent[v];
            Edge edge = findEdge(u, v);
            bottleneck = Math.min(bottleneck, edge.getResidualCapacity());
        }

        // Actualizar flujo
        for (int v = sink; v != source; v = parent[v]) {
            int u = parent[v];
            Edge edge = findEdge(u, v);
            edge.updateFlow(bottleneck);
        }

        maxFlow += bottleneck;
    }

    return maxFlow;
}
```

#### 2. BFS para Camino Aumentante
```java
private boolean bfs(int[] parent) {
    boolean[] visited = new boolean[vertices];
    Queue<Integer> queue = new LinkedList<>();
    queue.add(source);
    visited[source] = true;
    parent[source] = -1;

    while (!queue.isEmpty()) {
        int u = queue.poll();
        for (Edge edge : graph.adjacency.get(u)) {
            if (!visited[edge.to] && edge.getResidualCapacity() > 0) {
                queue.add(edge.to);
                visited[edge.to] = true;
                parent[edge.to] = u;

                if (edge.to == sink) return true;
            }
        }
    }
    return false;
}
```

---

## 📋 Estructura de Entrega

### 1. Código Fuente
```
/project
├── src/com/upb/supplychain/
│   ├── Edge.java
│   ├── Graph.java
│   ├── EdmondsKarp.java
│   └── SupplyChainApp.java
├── test/com/upb/supplychain/
│   └── EdmondsKarpTest.java
├── docs/
│   ├── informe_tecnico.md
│   └── analisis_del_flujo.md
├── presentacion_piense.md
├── presentation.md
└── README.md
```

### 2. Informe Técnico
- **Resumen ejecutivo**: Descripción del caso y solución propuesta
- **Modelado**: Explicación detallada de nodos, aristas y capacidades
- **Análisis de flujo**:
  - Flujo inicial factible
  - Red residual después de cada iteración
  - Camino aumentante y cuello de botella identificado
- **Resultados**: Flujo máximo obtenido y análisis de corte s-t
- **Conclusión**: Implicaciones prácticas para la cadena de suministro

### 3. Presentación (12 minutos)
- **Parte 1 (2min)**: Modelado del caso - quién expone: [Nombre]
- **Parte 2 (2min)**: Flujo factible - quién expone: [Nombre]
- **Parte 3 (2min)**: Red residual - quién expone: [Nombre]
- **Parte 4 (2min)**: Camino aumentante - quién expone: [Nombre]
- **Parte 5 (2min)**: ¿Es máximo? - quién expone: [Nombre]
- **Preguntas (2min)**: Preparación grupal

---

## ✅ Checklist de Validación

### Código
- [ ] Clase Edge implementada correctamente
- [ ] Clase Graph con estructura de red residual
- [ ] Algoritmo Edmonds-Karp funcional
- [ ] Caso específico configurado
- [ ] Tests unitarios para casos básicos
- [ ] Documentación Javadoc completa

### Modelado
- [ ] Todos los nodos definidos y justificados
- [ ] Capacidades realistas asignadas
- [ ] Fuente y sumidero claramente identificados
- [ ] Flujo factible inicial propuesto
- [ ] Cuello de botella identificado

### Exposición
- [ ] Material visual preparado (diagramas en pizarra/hojas)
- [ ] Cada miembro puede explicar TODAS las partes
- [ ] Ejemplos alternativos preparados
- [ ] Tiempo de 12 minutos ensayado
- [ ] Preguntas frecuentes anticipadas

### Individual
- [ ] Preparación para quiz en plataforma
- [ ] Capacidad de explicar cualquier parte del algoritmo
- [ ] Entendimiento de conceptos teóricos
  - Red residual y su significado
  - Aristas inversas y su función
  - Teorema de flujo máximo/corte mínimo

---

## 🎓 Evaluación

### Criterios y Ponderación
| Criterio | Peso | Tipo |
|----------|------|------|
| Correctitud del algoritmo | 25% | Grupal |
| Modelado del caso | 15% | Grupal |
| Claridad de exposición | 20% | Grupal |
| Defensa individual | 25% | Individual |
| Quiz individual | 15% | Individual |

### Fechas Clave
- **Sorteo**: [Fecha en clase]
- **Entrega código**: [Fecha límite]
- **Exposición**: [Fecha asignada]
- **Quiz**: Inmediatamente después de exposición

---

## 📚 Recursos de Estudio

### Conceptos Clave
1. **Red de Flujo**: Grafo dirigido con capacidades en aristas
2. **Red Residual**: Capacidad remanente + aristas inversas para corrección
3. **Camino Aumentante**: Ruta de s a t con capacidad residual > 0
4. **Corte s-t**: Partición de vértices que separa fuente de sumidero
5. **Flujo Máximo**: Valor máximo de flujo transportable

### Preguntas de Preparación
1. ¿Cómo construyes la red residual manualmente?
2. ¿Qué representa una arista inversa en la realidad?
3. ¿Cómo identificas un camino aumentante?
4. ¿Cómo calculas el cuello de botella?
5. ¿Cómo justificas si un flujo es máximo?
6. ¿Qué relación existe entre flujo máximo y corte mínimo?

---

## 🔧 Configuración del Entorno

### Requisitos Técnicos
- Java 11 o superior
- Maven o Gradle (opcional)
- IDE recomendado: IntelliJ IDEA / Eclipse
- Las pruebas son autocontenidas y no requieren JUnit

### Comandos de Ejecución
```bash
# Compilar
javac -d bin src/com/upb/supplychain/*.java

# Ejecutar
java -cp bin com.upb.supplychain.SupplyChainApp

# Ejecutar tests
javac -d bin src/com/upb/supplychain/*.java test/com/upb/supplychain/EdmondsKarpTest.java
java -cp bin com.upb.supplychain.EdmondsKarpTest
```

---

## 📊 Análisis del Caso 3: Ejemplo Detallado

### Configuración del Grafo
```
s (Fábrica) → B1: 100
s (Fábrica) → B2: 120
B1 → B2: 60
B1 → B3: 80
B1 → t (Centro): 50
B2 → B3: 70
B2 → t: 60
B3 → t: 90
```

### Cálculo del Flujo Máximo (Iterativo)

#### Iteración 1: Camino s → B1 → t
- Capacidad residual s→B1: 100, B1→t: 50
- Bottleneck: min(100, 50) = 50
- Flujo total: 50

#### Iteración 2: Camino s → B2 → t
- Capacidad residual s→B2: 120, B2→t: 60
- Bottleneck: min(120, 60) = 60
- Flujo total: 110

#### Iteración 3: Camino s → B1 → B3 → t
- Capacidad residual s→B1: 50, B1→B3: 80, B3→t: 90
- Bottleneck: min(50, 80, 90) = 50
- Flujo total: 160

#### Iteración 4: Camino s → B2 → B3 → t
- Capacidad residual s→B2: 60, B2→B3: 70, B3→t: 40
- Bottleneck: min(60, 70, 40) = 40
- Flujo total: 200

#### Iteración 5: Verificar caminos alternativos
- s → B1 → B2 → t
- s → B2 → B1 → B3 → t

**Flujo máximo final: 200 unidades/día**

---

*Documento preparado para el trabajo grupal de Algorítmica II - Universidad Privada Boliviana*
