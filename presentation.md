# Guía de presentación — 12 minutos

> **Versión complementaria.** Para la exposición final sin código se debe usar
> [presentacion_piense.md](presentacion_piense.md), centrada únicamente en el
> razonamiento y en el ejemplo de la cadena de suministro.

> Sustituir los nombres de expositores y ensayar con cronómetro. Cada integrante debe poder cubrir cualquier sección, en especial las preguntas del final.

## 0:00–2:00 — Problema y modelado

**Expositor/a:** _Nombre_

“Nuestro caso representa una fábrica que debe llevar productos a un centro de distribución usando tres bodegas regionales. Cada flecha es una ruta de transporte y el número encima es su capacidad máxima diaria.”

Mostrar el diagrama del README o dibujar:

```text
             100           50
      s  ------------> B1 ------> t
      |                 |  \80
   120|              60 |    v
      v                 v    B3 --90--> t
      B2 ----70-------->      
       \----------------60----------> t
```

“La fuente es la fábrica, el sumidero es el centro, y B1, B2 y B3 son nodos intermedios. Buscamos cuántas unidades por día pueden llegar como máximo a t sin superar ninguna capacidad.”

## 2:00–4:00 — Flujo factible inicial

**Expositor/a:** _Nombre_

“Primero aplicamos BFS, porque Edmonds–Karp es Ford–Fulkerson usando búsqueda en anchura. El primer camino es s–B1–t. Su cuello de botella es min(100, 50) = 50; enviamos 50.”

“El siguiente camino es s–B2–t, con mínimo min(120, 60) = 60. El flujo acumulado es 110.”

Escribir en pizarra:

| Ruta | Aumento | Acumulado |
|---|---:|---:|
| s–B1–t | 50 | 50 |
| s–B2–t | 60 | 110 |

## 4:00–6:00 — Red residual

**Expositor/a:** _Nombre_

“Después de enviar 50 por s–B1, no quedan 100 sino 50 unidades de capacidad directa. Además aparece B1–s con capacidad residual 50. Esa arista inversa permite deshacer, total o parcialmente, una decisión si más adelante encontramos una mejor combinación.”

“No representa un transporte físico inverso obligatorio: representa la posibilidad matemática de cancelar flujo previamente asignado.”

Mostrar dos ejemplos: `s→B1 residual 50 / B1→s residual 50`; `B1→t residual 0 / t→B1 residual 50`.

## 6:00–8:00 — Caminos restantes y cuellos de botella

**Expositor/a:** _Nombre_

“Las rutas directas a t desde B1 y B2 ya están saturadas. BFS encuentra s–B1–B3–t. El mínimo entre 50, 80 y 90 es 50; acumulamos 160.”

“Luego encuentra s–B2–B3–t. El mínimo entre 60, 70 y 40 es 40; acumulamos 200.”

| Ruta | Residuales antes de aumentar | Cuello | Acumulado |
|---|---|---:|---:|
| s–B1–B3–t | 50, 80, 90 | 50 | 160 |
| s–B2–B3–t | 60, 70, 40 | 40 | 200 |

## 8:00–10:00 — ¿Por qué es máximo?

**Expositor/a:** _Nombre_

“Al terminar, B1–t, B2–t y B3–t están saturadas. Todo producto debe entrar al centro por una de esas tres rutas.”

Escribir el corte:

`S = {s, B1, B2, B3}` y `T = {t}`

`capacidad del corte = 50 + 60 + 90 = 200`

“Hemos construido un flujo de 200 y encontramos un corte de capacidad 200. Por el teorema de flujo máximo–corte mínimo, el flujo no puede ser mayor. Entonces 200 unidades por día es óptimo.”

## 10:00–12:00 — Implementación, conclusiones y preguntas

**Expositor/a:** _Nombre_

“El programa tiene una clase `Edge`, una clase `Graph` que construye automáticamente aristas residuales, y `EdmondsKarp`, que ejecuta BFS y actualiza el flujo. Incluimos pruebas del caso principal, una red desconectada, una arista única y la corrección mediante una arista inversa.”

“La conclusión logística es que ampliar la salida de la fábrica no basta: las entradas al centro son el cuello de botella. Para superar 200, se debe ampliar alguna de las rutas B1–t, B2–t o B3–t.”

### Preguntas que deben dominar

1. **¿Por qué BFS?** Porque Edmonds–Karp define Ford–Fulkerson con BFS; garantiza terminación y complejidad `O(VE²)`.
2. **¿Qué es el cuello de botella?** La menor capacidad residual del camino; es la cantidad máxima adicional que puede enviarse por él.
3. **¿Por qué existen aristas inversas?** Para poder cancelar flujo y corregir elecciones anteriores sin perder factibilidad.
4. **¿Cómo sabemos que 200 es máximo?** Hay un flujo factible de 200 y un corte s–t de capacidad 200.
5. **¿Cuál es el cuello de botella real del negocio?** Las entradas al centro de distribución: 50 + 60 + 90.
