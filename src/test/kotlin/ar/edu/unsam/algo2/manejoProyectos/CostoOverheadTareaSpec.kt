package ar.edu.unsam.algo2.manejoProyectos

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class CostoOverheadTareaSpec:DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Dada una tarea simple") {
        val tarea = TareaSimple(10)
        it("no tiene costo por overhead") {
            tarea.costoPorOverhead() shouldBe 0
        }
    }
    describe("Dada una tarea compuesta de pocas subtareas") {
        val tarea = TareaCompuesta(10)
        with (tarea) {
            agregarSubtarea(TareaSimple(5))
            agregarSubtarea(TareaSimple(15))
            agregarSubtarea(TareaSimple(25))
        }
        it("no tiene costo por overhead") {
            tarea.costoPorOverhead() shouldBe 0
        }
        it("si pasa a tener muchas subtareas tiene costo por overhead") {
            tarea.agregarSubtarea(TareaSimple(40))
            tarea.costoPorOverhead() shouldBe 7.5
        }
    }
})