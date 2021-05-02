
## Manejo de Proyectos

[![build](https://github.com/uqbar-project/eg-manejo-proyectos-kotlin/actions/workflows/build.yml/badge.svg)](https://github.com/uqbar-project/eg-manejo-proyectos-kotlin/actions/workflows/build.yml) [![coverage](https://codecov.io/gh/uqbar-project/eg-manejo-proyectos-kotlin/branch/master/graph/badge.svg)](https://codecov.io/gh/uqbar-project/eg-manejo-proyectos-kotlin/branch/master/graph/badge.svg) [![BCH compliance](https://bettercodehub.com/edge/badge/uqbar-project/eg-manejo-proyectos-kotlin?branch=master)](https://bettercodehub.com/) 

Este ejercicio sirve como excusa para contar el proceso del diseño.

- [Link al apunte](https://docs.google.com/document/d/1yHzmqlJLFNwRGucz4wJgZmeq1zYddNgfQ6G1KcqRhdk/edit#)

## Detalles de la implementación

### Construcción de una tarea

La tarea tiene como constructor default el tiempo:

```kt
abstract class Tarea(var tiempo: Int) {
```

Las subclases a su vez deben recibir también el tiempo para poder invocar al constructor del padre:

```kt
class TareaSimple(tiempo: Int) : Tarea(tiempo) {

...

class TareaCompuesta(tiempo: Int) : Tarea(tiempo) {
```

Esto beneficia a los tests, que pueden construir tareas simples o compuestas indicando el tiempo, necesario para calcular el costo, los días máximos de atraso, y varias cosas más.

```kt
    describe("Una tarea simple de complejidad mínima") {
        val tarea = TareaSimple(10)
```

La alternativa hubiera sido dejar el constructor por defecto sin parámetros, lo que hubiera obligado a cada test a tener dos líneas para 1) crear una tarea, 2) asignarle el tiempo:

```kt
val tarea = TareaSimple()
tarea.tiempo = 10
```

### Diseño del porcentaje de completitud

Dado que solo las tareas simples pueden setear el porcentaje de completitud, nuestra decisión de diseño fue subir la responsabilidad de obtener dicho porcentaje en la superclase abstracta Tarea:

```kt
    abstract fun porcentajeCompletitud(): Int
```

Esto tiene ciertas consecuencias:

- no subimos la asignación, por lo que una tarea compuesta no puede setear su porcentaje
- si una tarea simple la definimos de tipo tarea, tampoco podremos asignarle la completitud, porque el tipo Tarea no define en su interfaz el seteo del %

```kt
    val tarea: Tarea = TareaSimple()
    tarea.porcentajeCompletitud = 100  // ERROR
```

La otra alternativa es subir la responsabilidad a la superclase, con lo cual podría pensarse mirando la interfaz que a cualquier tarea le podemos asignar el % de completitud.

A su vez, modificamos el comportamiento del setter de completitud para la tarea simple, de manera de asegurarnos que el usuario únicamente pueda cargar 0 ó 100 como valores posibles:

```kt
    var porcentajeCompletitud = 0
        set(porcentaje) {
            if (porcentaje !in listOf(0, 100)) {
                throw BusinessException("Solo puede asignar 0 ó 100 al % de completitud")
            }
            field = porcentaje
        }
```

Dentro de un getter o setter, debemos usar `field` como la referencia al atributo que estamos trabajando. Para más información pueden leer [este artículo](https://www.baeldung.com/kotlin/getters-setters).

Tenemos un test que prueba esta validación:

```kt
it("al setear porcentaje de completitud fuera de rango tira error") {
    assertThrows<BusinessException>{ tarea.porcentajeCompletitud = 50 }
}
```

### Definición de las complejidades como strategies

Las complejidades mínima, media y máxima son **strategies**, y en nuestra definición ubicamos como superclase a la ComplejidadMinima, ya que el cálculo base de las otras complejidades se toma en base a la definición original que hace ésta. Una alternativa válida es definir una clase abstracta que tenga el cálculo base `tiempo * 25` y definir tres subclases concretas, ya que por ejemplo para los días máximos de atraso no hay ningún comportamiento en común para reutilizar.

### Organización de los tests unitarios

Decidimos organizar clases específicas para

- complejidad mínima, media y máxima, que definen a su vez
  - el costo de una tarea
  - y los máximos días de atraso de un proyecto
- y luego generamos clases que prueban determinados requerimientos o casos de uso:
  - costo por overhead
  - porcentaje de completitud
  - costo impositivo

El motivo principal es que estas tres últimas funcionalidades se entrecruzan con las clases de equivalencia de las complejidades. Dicho de otra manera, el costo impositivo se puede dar tanto para una tarea de complejidad mínima, media o máxima y no afecta a dicho cálculo (lo que importa es cuántos impuestos tiene una tarea). Lo mismo con el costo por overhead o el porcentaje de completitud. 

### Detalles menores de implementación

- La lista de subtareas de una tarea tiene que soportar cambios, por eso utilizamos `MutableList<Tarea>` y no `List<Tarea>`. Y se crean mediante `mutableListOf()`.
- En cambio cuando comparamos los valores posibles para setear el porcentaje de completitud, esa lista es fija, por eso enviamos el mensaje `listOf(0, 100)`
- Al calcular el % de completitud en la tarea compuesta, estamos resolviéndolo así 
  
```kt
this.subtareas.sumBy { it.porcentajeCompletitud() } / this.subtareas.size
```

porque nos interesa calcular el total como un **entero**. En cambio para calcular el costo impositivo, hacemos

```kt
impuestos.sumByDouble { it.costoImpositivo(this) }
```

porque no queremos perder los decimales.

- Recordamos que los métodos que devuelven valores se definen con `=` y los que tienen efecto se encierran entre llaves `{}` (o bien cuando tenemos varias líneas y queremos devolver un valor lo hacemos mediante un `return`)
- No olvidarse de escribir en los test `isolationMode = IsolationMode.InstancePerTest` para evitar que el efecto colateral de un test afecte al siguiente