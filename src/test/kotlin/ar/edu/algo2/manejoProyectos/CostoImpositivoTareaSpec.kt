package ar.edu.algo2.manejoProyectos

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class CostoImpositivoTareaSpec: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Una tarea simple con ambos impuestos") {
        // arrange
        val tarea = TareaSimple(10)
        with (tarea) {
            agregarImpuesto(IMPUESTO_A)
            agregarImpuesto(IMPUESTO_B)
        }
        it("suman ambos al costo impositivo de dicha tarea") {
            tarea.costoImpositivo() shouldBe 20
        }
        it("suman ambos al costo total de dicha tarea") {
            tarea.costo() shouldBe 270
        }
        it("si eliminamos un impuesto solo suma el otro impuesto al costo impositivo de dicha tarea") {
            // act
            tarea.eliminarImpuesto(IMPUESTO_B)

            // assert
            tarea.costoImpositivo() shouldBe 7.5
        }
    }
})