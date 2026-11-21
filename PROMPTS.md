# PROMPTS.md

Registro textual y en orden de todos los prompts enviados a la IA.

- **Herramienta:** Claude Code (aplicación de escritorio de Claude, pestaña Code)
- **Modelo:** Claude Opus 5.5 (`claude-opus-5-5`)
- **Pareja:** Santiago Portuguez Hernández, José Miguel Alfaro Chan
- **Versión del examen:** B — Salida Segura

Las fotografías adjuntas al prompt 1 (diseño a mano F1/F2/F3, alas, tabla de pasillos y ocupantes actuales) se transcriben debajo del prompt, como datos que acompañaron el mensaje.

---

## Prompt 1

```
Ayudame a planear la creacion de un sistema de evacuacion de un edificio universitario. 

1. Sigues estas indicaciones con respecto al uso de AI:

Se le debe indicar a la IA que no sugiera mejoras ni cambios a los requerimientos. Si aun así
lo hace, la pareja debe ignorar esas sugerencias: si se modifican los requerimientos o la
solución con base en sugerencias de la IA, el examen quedará anulado.
o Se recomienda pedirle a la IA que desarrolle la solución con un enfoque de TDD (Test-Driven
Development — Desarrollo Guiado por Pruebas); ver más en la Parte 3.
￼
2
o Se puede usar Java 17 o superior, o C#. La solución debe implementar sus propias
estructuras de datos y algoritmos, con estas restricciones:
▪ Permitido: arreglos (por ejemplo int[] o Parada[]) y String.
▪ Prohibido en Java: cualquier colección de java.util (ArrayList, LinkedList, HashMap,
HashSet, TreeMap, TreeSet, PriorityQueue, Stack, ArrayDeque, entre otras), así
como Arrays.sort, Collections.sort, List.sort y sorted() de Streams.
▪ Prohibido en C#: List, LinkedList, Dictionary, HashSet, SortedDictionary, SortedSet,
SortedList, PriorityQueue, Queue y Stack, así como Array.Sort, List.Sort y los
ordenamientos de LINQ (OrderBy, ThenBy).
▪ Prohibido en ambos lenguajes: bibliotecas que calculen caminos más cortos o
que implementen grafos.
▪ Las pruebas unitarias pueden usar, además, la biblioteca de pruebas (JUnit o
NUnit).
o Estas restricciones deben indicársele explícitamente a la IA. Si no se le indicaron, la sección
de implementación tendrá una nota de cero.
o Todos los prompts enviados a la IA deben quedar registrados, en orden y de forma textual,
en un archivo PROMPTS.md en la raíz del repositorio, indicando la herramienta utilizada. Si
falta PROMPTS.md, o si en él no aparecen las restricciones anteriores, la sección de
implementación tendrá una nota de cero. Se le puede pedir a la IA automatice este paso.
o Mientras se disponga de tiempo, se pueden hacer ajustes o modificaciones a la solución,
pero no a los requerimientos.

2. El desarrollo de cada feature y los datos que se van a usar van en las fotografias adjuntas
3. 

Ejecute su programa con:
• Personas por ala y en todo el edificio.
• El reporte para la brigada.
• La guía de evacuación para las seis salas.
```

### Adjuntos del prompt 1 (transcripción)

**Foto 1 — Diseño a mano (Versión B: Salida Segura)**

```
Examen 2
Santiago Portuguez Hernández 2025072534
José Miguel Alfaro Chan 2022437485

Versión B: Salida Segura (Diseño)

F1. Directorio del edificio
Nombre estructura o algoritmo: Árbol n-ario
Complejidad temporal: O(h) cada sala tiene que actualizar, sea h=400
Alternativa descartada: Lista simple O(n), para no revisar todos los datos
Requerimientos: RF-01: Seguridad debe poder listar las salas de un ala. RF-02: Actualizar
ocupantes de una sala. RF-03: Obtener cantidad de personas en cualquier ala o en el edificio.
RNF-01: Ninguna sala puede tener más de 80 personas.

F2. Reporte para la brigada
Nombre estructura o alg: Radix sort
Complejidad temporal: O(n)
Alt descartada: Merge sort, se descarta porque es O(n log n)
Requerimientos: RF-01: se activa la alarma. RF-02: lista las 400 salas de más a menos ocupantes.
RNF-01: en caso de empate, se listan por sala de A-Z. RNF-02: debe durar menos de 1s.

F3. Guía de evacuación
Nombre de estructura o algoritmo: Dijkstra (no usa caminos cerrados), two-way
Complejidad temporal: O(V²)
Alt descartada: BFS, no toma en cuenta el costo del tiempo
Requerimientos: RF-01: para cada sala indica a los ocupantes qué salida usar y qué pasillos
seguir. RNF-01: Tienen que salir lo más rápido posible. RF-02: indicar los segundos estimados.
RNF-02: Debe descartar rutas con pasillos cerrados o reportados como bloqueados.
RNF-03: Solo puede usar alguna de las 3 salidas disponibles.
```

**Foto 2 — Alas**

```
Alas: Oeste = LAB-101, LAB-102. Central = RM-103, RM-104. Este = OFF-105, LIB-001.
Pasillos (se pueden recorrer en ambos sentidos; tiempos en segundos):
```

**Foto 3 — Pasillos y ocupantes**

| Desde | Hacia | Segundos | Estado |
|---|---|---|---|
| Salida Norte | Pasillo A | 20 | Abierto |
| Pasillo A | LAB-101 | 10 | Abierto |
| Pasillo A | LAB-102 | 15 | Abierto |
| LAB-101 | LAB-102 | 6 | Abierto |
| LAB-102 | Pasillo B | 12 | Abierto |
| Pasillo B | RM-103 | 8 | Abierto |
| Pasillo B | Salida Este | 20 | Bloqueado (humo) |
| Pasillo B | Pasillo C | 14 | Abierto |
| Pasillo B | LIB-001 | 16 | Abierto |
| RM-103 | RM-104 | 10 | Abierto |
| RM-104 | Pasillo C | 9 | Abierto |
| Pasillo C | Salida Sur | 18 | Abierto |
| Pasillo C | OFF-105 | 7 | Abierto |
| OFF-105 | LIB-001 | 12 | Abierto |
| LIB-001 | Salida Este | 25 | Abierto |

```
Ocupantes actuales:
codigo,nombre,ala,ocupantes
LAB-101,Laboratorio de química,Oeste,24
LAB-102,Laboratorio de física,Oeste,30
RM-103,Sala de clases,Central,45
RM-104,Sala de seminarios,Central,30
OFF-105,Oficina de docentes,Este,6
LIB-001,Biblioteca,Este,45
```

---

## Prompt 2

Respuestas a las preguntas de la IA.

Pregunta: "¿En qué lenguaje se implementa el sistema de evacuación (Salida Segura)?"

```
Java 17+ (Recomendado)
```

Pregunta: "¿Dónde se crea el repositorio del examen?"

```
https://github.com/Saportu/Examen-2-AED1 utiliza la carpeta de github donde este repo ya esta clonado.
```

---

## Prompt 3

Aprobación del plan de implementación y elección de la rama de trabajo (`salida-segura`, nueva).

```
[Plan aprobado en la herramienta — sin texto adicional]
```

---

## Prompt 4

```
[Botón "Create PR" en la herramienta — sin texto adicional]
```

---

## Prompt 5

```
Se le debe solicitar a la IA que implemente pruebas unitarias que permitan comprobar cada
requerimiento. Las pruebas no deben limitarse a verificar verdadero/falso; por cada
requerimiento funcional se debe incluir como mínimo:
▪ Una prueba que compare el valor exacto esperado (por ejemplo, assertEquals(11,
ruta.getMinutos())).
▪ Casos límite (por ejemplo, una zona o un ala vacía, origen igual al destino,
empates).
▪ Entradas inválidas (por ejemplo, una parada o sala inexistente, o un dato mal
formado), verificando el error o el resultado esperado.
o Se recomienda JUnit para Java o NUnit para C#; el repositorio base ya incluye ambos
configurados.
o La solución debe subirse a GitHub, en un repositorio creado a partir del repositorio base, y
el enlace se debe compartir en la sección designada para ello en el apartado del examen en
la plataforma del TEC Digital.
o Se le debe solicitar a la IA que genere un archivo README con las siguientes secciones:
▪ Nombre del examen-proyecto.
▪ Nombre de los autores.
▪ Requerimientos del sistema: la lista completa de requerimientos de la Parte 1, tal
como fueron presentados, sin modificaciones.
▪ Instrucciones de instalación y ejecución, incluido cómo ejecutar las pruebas.
▪ Descripción de la jerarquía de carpetas del proyecto y de lo que hace cada módulo
o clase.
3
▪ Requerimientos desarrollados por los estudiantes: cuáles requerimientos se
implementaron y cuáles se excluyeron, con el motivo de cada exclusión. Un
requerimiento excluido no suma los puntos de implementación ni de pruebas
correspondientes.
▪ Diagrama de flujo, diagrama de componentes y diagrama del grafo que emplea el
sistema, escritos en Mermaid dentro del README.
```
