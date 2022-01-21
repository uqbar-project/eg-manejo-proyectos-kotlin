package ar.edu.unsam.algo2.manejoProyectos

import ar.edu.unsam.algo2.manejoProyectos.exceptions.BusinessException

abstract class Tarea(var tiempo: Int) {
    var complejidad = ComplejidadMinima()
    val impuestos: MutableList<Impuesto> = mutableListOf()

    fun costo() = costoComplejidad() + costoImpositivo() + costoPorOverhead()

    abstract fun costoPorOverhead(): Double

    fun costoComplejidad() = complejidad.costo(this)

    fun costoImpositivo() = impuestos.sumOf { it.costoImpositivo(this) }

    fun complejidadMedia() {
        complejidad = ComplejidadMedia()
    }

    fun complejidadMaxima() {
        complejidad = ComplejidadMaxima()
    }

    fun agregarImpuesto(impuesto: Impuesto) {
        this.impuestos.add(impuesto)
    }

    fun eliminarImpuesto(impuesto: Impuesto) {
        this.impuestos.remove(impuesto)
    }

    abstract fun porcentajeCompletitud(): Int

    fun diasMaximoAtraso() = complejidad.diasMaximoAtraso(this)
}

class TareaSimple(tiempo: Int) : Tarea(tiempo) {
    var porcentajeCompletitud = 0
        set(porcentaje) {
            if (porcentaje !in listOf(0, 100)) {
                throw BusinessException("Solo puede asignar 0 ó 100 al % de completitud")
            }
            field = porcentaje
        }

    override fun porcentajeCompletitud(): Int = porcentajeCompletitud

    override fun costoPorOverhead() = 0.0
}

class TareaCompuesta(tiempo: Int) : Tarea(tiempo) {
    var subtareas: MutableList<Tarea> = mutableListOf()

    override fun porcentajeCompletitud() = this.subtareas.sumOf { it.porcentajeCompletitud() } / this.subtareas.size

    override fun costoPorOverhead() = this.costoComplejidad() * (if (tieneMuchasSubtareas()) 0.03 else 0.0)

    fun tieneMuchasSubtareas() = this.subtareas.size > 3

    fun agregarSubtarea(subtarea: Tarea) {
        this.subtareas.add(subtarea)
    }
}

open class ComplejidadMinima {
    open fun costo(tarea: Tarea) = tarea.tiempo * 25.0
    open fun diasMaximoAtraso(tarea: Tarea) = 5.0
}

class ComplejidadMedia : ComplejidadMinima() {
    override fun costo(tarea: Tarea) = 1.05 * super.costo(tarea)
    override fun diasMaximoAtraso(tarea: Tarea) = tarea.tiempo * 0.1
}

class ComplejidadMaxima : ComplejidadMinima() {
    override fun costo(tarea: Tarea): Double = (1.07 * super.costo(tarea)) + this.costoExtra(tarea.tiempo)

    fun costoExtra(tiempo: Int) = Math.max(0, tiempo - 10) * 10
    override fun diasMaximoAtraso(tarea: Tarea) = tarea.tiempo * 0.2 + 8
}
