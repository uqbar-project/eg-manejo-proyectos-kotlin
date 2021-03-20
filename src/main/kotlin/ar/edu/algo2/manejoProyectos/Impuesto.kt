package ar.edu.algo2.manejoProyectos

data class Impuesto(var monto: Double) {

    fun costoImpositivo(tarea: Tarea): Double {
        return this.monto * tarea.costoComplejidad() / 100
    }
}

val IMPUESTO_A = Impuesto(3.0)
val IMPUESTO_B = Impuesto(5.0)