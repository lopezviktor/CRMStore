package com.example.crmstore.modelo

data class Evento(
    val id: String = "",
    val titulo: String,
    val fecha: String,
    val hora: String,
    val descripcion: String,
    val participantes: List<String> // Lista de IDs de empleados
)