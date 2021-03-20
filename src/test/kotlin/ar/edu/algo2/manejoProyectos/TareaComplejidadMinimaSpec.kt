package ar.edu.algo2.manejoProyectos

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TareaComplejidadMinimaSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Una tarea simple de complejidad mínima") {
        val tarea = TareaSimple(10)
        it("tiene el costo del cálculo base") {
            tarea.costo() shouldBe 250
        }
    }

})

