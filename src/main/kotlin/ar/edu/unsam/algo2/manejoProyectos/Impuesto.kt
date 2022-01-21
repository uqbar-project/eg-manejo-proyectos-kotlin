package ar.edu.unsam.algo2.manejoProyectos

data class Impuesto(var monto: Double) {
    fun costoImpositivo(tarea: Tarea) = this.monto * tarea.costoComplejidad() / 100
}

val IMPUESTO_A = Impuesto(3.0)
val IMPUESTO_B = Impuesto(5.0)