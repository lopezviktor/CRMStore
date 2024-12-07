package com.example.crmstore.modelo

data class Empleado(
    val id: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val mail: String = "",
    val telefono: String? = null,
    val puesto: String? = null,
    val salarioBase: Double = 0.0,
    val pagas: Int = 14,
    val eventos: List<String> = emptyList() // Lista de IDs de eventos
)