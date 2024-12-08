package com.example.crmstore.modelo

data class Evento(
    var id: String = "",                      // Campo ID
    val titulo: String = "",                  // Campo título
    val fecha: String = "",                   // Campo fecha
    val hora: String = "",                    // Campo hora
    val descripcion: String = "",             // Campo descripción
    val participantes: List<String> = listOf() // Lista de participantes (por defecto, lista vacía)
) {
    // Constructor vacío necesario para la deserialización de Firestore
    constructor() : this("", "", "", "", "", listOf())
}
