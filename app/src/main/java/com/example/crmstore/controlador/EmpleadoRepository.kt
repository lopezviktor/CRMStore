package com.example.crmstore.controlador

import com.example.crmstore.modelo.Empleado
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EmpleadoRepository {

    private val db = FirebaseFirestore.getInstance()

    fun obtenerEmpleados(onComplete: (List<Pair<String, Empleado>>) -> Unit) {
        db.collection("empleados").addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Error al obtener los empleados: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val empleados = snapshot.documents.mapNotNull { document ->
                    val empleado = document.toObject(Empleado::class.java)
                    empleado?.let { document.id to it }
                }
                onComplete(empleados)
            }
        }
    }

    suspend fun eliminarEmpleado(idEmpleado: String) {
        try {
            db.collection("empleados").document(idEmpleado).delete().await()
            println("Empleado eliminado con éxito")
        } catch (e: Exception) {
            println("Error al eliminar empleado: ${e.message}")
        }
    }

    suspend fun agregarEmpleado(empleado: Empleado): String {
        return try {
            val docRef = db.collection("empleados").add(empleado).await()
            docRef.id
        } catch (e: Exception) {
            throw Exception("Error al agregar empleado: ${e.message}")
        }
    }

    suspend fun actualizarEmpleado(idDocumento: String, empleado: Empleado) {
        try {
            db.collection("empleados").document(idDocumento).set(empleado).await()
        } catch (e: Exception) {
            throw Exception("Error al actualizar empleado: ${e.message}")
        }
    }


    // Método para calcular el coste total del empleado
    fun calcularCosteTotalEmpleado(empleado: Empleado): Double {
        val salarioBase = empleado.salarioBase
        val complementosSalariales = empleado.complementos.sumOf { it.valor }

        // Cálculo de la Seguridad Social (aproximado según la información proporcionada)
        val seguridadSocial = calcularSeguridadSocial(salarioBase)

        return salarioBase + complementosSalariales + seguridadSocial
    }

    // Método para calcular la Seguridad Social
    private fun calcularSeguridadSocial(salarioBase: Double): Double {
        // Fórmula simplificada basada en el ejemplo de los search results
        return (salarioBase * 0.236) +
                (salarioBase * 0.055) +
                (salarioBase * 0.035) +
                (salarioBase * 0.002) +
                (salarioBase * 0.006)
    }

    // Método para calcular salario neto
    fun calcularSalarioNeto(empleado: Empleado): Double {
        val salarioBruto = empleado.salarioBase
        val complementos = empleado.complementos.sumOf { it.valor }

        // Cálculo aproximado de retenciones
        val retencionIRPF = calcularRetencionIRPF(salarioBruto)
        val seguridadSocial = calcularSeguridadSocial(salarioBruto)

        return salarioBruto + complementos - retencionIRPF - seguridadSocial
    }

    // Método simplificado de cálculo de retención de IRPF
    private fun calcularRetencionIRPF(salarioBase: Double): Double {
        // Cálculo simplificado de retención de IRPF
        return when {
            salarioBase < 12000 -> salarioBase * 0.10
            salarioBase < 20000 -> salarioBase * 0.15
            salarioBase < 35000 -> salarioBase * 0.20
            else -> salarioBase * 0.25
        }
    }
}