package ar.edu.algo2.manejoProyectos

import ar.edu.algo2.manejoProyectos.exceptions.BusinessException
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows

class PorcentajeCompletitudTareaSpec: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Una tarea simple") {
        val tarea = TareaSimple(10)
        it("inicialmente no tiene porcentaje de completitud") {
            tarea.porcentajeCompletitud() shouldBe 0
        }
        it("al setear porcentaje de completitud queda resuelta") {
            tarea.porcentajeCompletitud = 100
            tarea.porcentajeCompletitud() shouldBe 100
        }
        it("al setear porcentaje de completitud fuera de rango tira error") {
            assertThrows<BusinessException>{ tarea.porcentajeCompletitud = 50 }
        }
    }
    describe("Una tarea compuesta") {
        val tarea = TareaCompuesta(20)
        val tareaSimpleInicial = TareaSimple(10)
        tareaSimpleInicial.porcentajeCompletitud = 100
        tarea.agregarSubtarea(tareaSimpleInicial)
        tarea.agregarSubtarea(TareaSimple(20))
        tarea.agregarSubtarea(TareaSimple(50))
        it("el porcentaje de completitud se obtiene de la completitud de las subtareas") {
            tarea.porcentajeCompletitud() shouldBe 33
        }
    }
})