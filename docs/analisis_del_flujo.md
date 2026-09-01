# Análisis manual del flujo y la red residual

Esta hoja acompaña la demostración en pizarra. Se usa `f/c` para representar **flujo/capacidad**. Las aristas inversas omitidas inicialmente tienen capacidad residual cero; después de enviar `x`, aparece una arista inversa con residual `x`.

## Estado inicial

Todas las aristas tienen flujo 0. Las rutas más cortas hacia el centro son `s → B1 → t` y `s → B2 → t`.

## Iteración 1

**Camino:** `s → B1 → t`  
**Residuales del camino:** 100, 50  
**Cuello de botella:** `min(100, 50) = 50`

Se envían 50 unidades. Flujo acumulado: **50**.

Cambios de red residual:

| Arista | Antes | Después | Inversa habilitada |
|---|---:|---:|---:|
| s → B1 | 100 | 50 | B1 → s: 50 |
| B1 → t | 50 | 0 | t → B1: 50 |

## Iteración 2

**Camino:** `s → B2 → t`  
**Residuales del camino:** 120, 60  
**Cuello de botella:** `min(120, 60) = 60`

Se envían 60 unidades. Flujo acumulado: **110**.

Cambios relevantes: `s → B2` queda con residual 60; `B2 → t` queda saturada y aparece `t → B2` con residual 60.

## Iteración 3

**Camino:** `s → B1 → B3 → t`  
**Residuales del camino:** 50, 80, 90  
**Cuello de botella:** `min(50, 80, 90) = 50`

Se envían 50 unidades. Flujo acumulado: **160**.

Cambios relevantes: `s → B1` se satura; `B1 → B3` conserva residual 30; `B3 → t` conserva residual 40.

## Iteración 4

**Camino:** `s → B2 → B3 → t`  
**Residuales del camino:** 60, 70, 40  
**Cuello de botella:** `min(60, 70, 40) = 40`

Se envían 40 unidades. Flujo acumulado: **200**.

Ahora `B3 → t` queda saturada. También lo estaban `B1 → t` y `B2 → t`. No hay ninguna arista residual positiva que entre al centro desde un nodo alcanzable, por lo cual BFS no puede encontrar otro camino aumentante.

## Flujo final factible

| Tramo | Flujo/capacidad | Residual directa |
|---|---:|---:|
| s → B1 | 100/100 | 0 |
| s → B2 | 100/120 | 20 |
| B1 → B2 | 0/60 | 60 |
| B1 → B3 | 50/80 | 30 |
| B1 → t | 50/50 | 0 |
| B2 → B3 | 40/70 | 30 |
| B2 → t | 60/60 | 0 |
| B3 → t | 90/90 | 0 |

Comprobación de conservación:

- B1: entrada 100; salida 50 + 50 = 100.
- B2: entrada 100; salida 40 + 60 = 100.
- B3: entrada 50 + 40 = 90; salida 90.

## Corte mínimo

Al finalizar, tomamos `S = {s, B1, B2, B3}` y `T = {t}`. El corte contiene:

`B1 → t (50), B2 → t (60), B3 → t (90)`.

Su capacidad es 200, idéntica al flujo logrado. Esta igualdad demuestra que el flujo es máximo.

## Interpretación de una arista inversa

Una arista inversa no equivale necesariamente a un camión que vuelve con mercancía. Es una herramienta de contabilidad del algoritmo: si se enviaron 50 unidades por B1→B3 y después se descubre una ruta más conveniente, B3→B1 permite cancelar hasta 50 de esa asignación. Esto mantiene abiertas las alternativas y evita que una elección temprana bloquee la solución óptima.
