# Informe técnico — Caso 3: Cadena de suministro

**Materia:** Algorítmica II — Redes de Flujo  
**Universidad Privada Boliviana**  
**Integrantes:** _Completar antes de entregar_  
**Fecha:** _Completar antes de entregar_

## 1. Resumen ejecutivo

Se modeló el traslado diario de productos desde una fábrica hacia un centro de distribución mediante una red de flujo dirigida. Las bodegas B1, B2 y B3 son puntos intermedios; cada tramo tiene una capacidad máxima diaria. Para encontrar la cantidad máxima transportable se implementó Ford–Fulkerson con selección BFS de caminos, conocido como Edmonds–Karp.

El flujo máximo calculado es **200 unidades/día**. El límite operativo no se encuentra en la capacidad de producción inicial (220 unidades/día), sino en los tres tramos que llegan al centro de distribución, cuya capacidad conjunta es 200.

## 2. Modelado del problema

La fuente `s` es la fábrica y el sumidero `t` es el centro final. B1, B2 y B3 representan bodegas regionales. Una arista `u → v` con capacidad `c` significa que como máximo `c` unidades por día pueden enviarse directamente de `u` a `v`.

| Tramo | Capacidad (unidades/día) | Interpretación |
|---|---:|---|
| s → B1 | 100 | Producción enviada a B1 |
| s → B2 | 120 | Producción enviada a B2 |
| B1 → B2 | 60 | Transferencia regional |
| B1 → B3 | 80 | Transferencia regional |
| B1 → t | 50 | Entrega directa al centro |
| B2 → B3 | 70 | Transferencia regional |
| B2 → t | 60 | Entrega directa al centro |
| B3 → t | 90 | Entrega final desde B3 |

El enunciado menciona `vertices = 6`, pero la matriz, el diagrama y el caso contienen cinco nodos: `s`, B1, B2, B3 y `t`. La implementación usa correctamente **5 vértices**.

## 3. Solución algorítmica

Para cada tramo físico se crean dos aristas: una directa y una inversa residual. La directa conserva la capacidad original; la inversa inicia con capacidad 0. Al enviar flujo, la arista inversa gana capacidad residual, que representa la posibilidad de cancelar o redirigir parte de una decisión anterior.

En cada iteración, BFS busca una ruta desde `s` hasta `t` que tenga capacidad residual positiva. El mínimo de las capacidades de la ruta es el **cuello de botella**. Se aumenta exactamente ese valor y se repite hasta que ya no exista un camino aumentante.

La implementación separa responsabilidades:

- `Edge`: capacidad, flujo, residual y aumento de flujo.
- `Graph`: lista de adyacencia y pares de aristas directa/inversa.
- `EdmondsKarp`: BFS, augmentación y registro de iteraciones.
- `SupplyChainApp`: configuración específica y salida legible.

## 4. Análisis de flujo

La ejecución BFS selecciona estos caminos aumentantes:

| Iteración | Camino | Cuello de botella | Flujo acumulado |
|---:|---|---:|---:|
| 1 | s → B1 → t | 50 | 50 |
| 2 | s → B2 → t | 60 | 110 |
| 3 | s → B1 → B3 → t | 50 | 160 |
| 4 | s → B2 → B3 → t | 40 | 200 |

El detalle de las capacidades residuales se documenta en [analisis_del_flujo.md](analisis_del_flujo.md). El flujo final satisface conservación en las bodegas: B1 recibe 100 y entrega 50 + 50; B2 recibe 100 y entrega 60 + 40; B3 recibe 90 y entrega 90.

## 5. Demostración de optimalidad: corte mínimo

Considérese el corte con `S = {s, B1, B2, B3}` y `T = {t}`. Las aristas que lo cruzan son B1→t, B2→t y B3→t. Su capacidad es:

`50 + 60 + 90 = 200` unidades/día.

Se encontró un flujo factible de valor 200 y existe un corte de capacidad 200. Por el teorema de flujo máximo–corte mínimo, ningún flujo puede ser mayor; por tanto, 200 es el flujo máximo.

## 6. Implicaciones prácticas

El centro de distribución es el principal punto de restricción: todos sus tramos de entrada quedan saturados. Incrementar únicamente s→B1 o s→B2 no eleva el flujo máximo. Para transportar más de 200 unidades/día se debe ampliar al menos una ruta de entrada al centro, por ejemplo B3→t, B2→t o B1→t, y contar con capacidad aguas arriba que alimente dicha ampliación.

## 7. Conclusión

El modelo convierte una decisión logística en un problema cuantificable: la red actual puede distribuir 200 unidades por día. Edmonds–Karp aporta un procedimiento reproducible, y la red residual garantiza que el algoritmo pueda revisar decisiones previas. La igualdad entre flujo encontrado y capacidad del corte demuestra rigurosamente la optimalidad del resultado.
