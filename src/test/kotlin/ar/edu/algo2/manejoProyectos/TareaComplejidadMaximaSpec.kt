package ar.edu.algo2.manejoProyectos

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TareaComplejidadMaximaSpec: DescribeSpec({
    describe("Una tarea simple de complejidad maxima") {
        val tarea = TareaSimple(10)
        with (tarea) {
            complejidadMaxima()
        }
        it("tiene el costo del cálculo base con un adicional sin extra de días") {
            tarea.costo() shouldBe 267.5
        }
        it("tiene el costo del cálculo base con un adicional con extra de días") {
            tarea.tiempo = 14
            tarea.costo() shouldBe 414.5
        }
    }
})