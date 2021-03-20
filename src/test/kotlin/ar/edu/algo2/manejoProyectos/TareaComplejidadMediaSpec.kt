package ar.edu.algo2.manejoProyectos

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TareaComplejidadMediaSpec:DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Una tarea simple de complejidad media") {
        val tarea = TareaSimple(10)
        with (tarea) {
            complejidadMedia()
        }
        it("tiene el costo del cálculo base con un adicional") {
            tarea.costo() shouldBe 262.5
        }
        it("utiliza un porcentaje del tiempo para calcular los días máximos de atraso") {
            tarea.diasMaximoAtraso() shouldBe 1
        }
    }

})