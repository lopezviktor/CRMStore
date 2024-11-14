package com.example.crmstore.modelo

data class Cliente (
    val id: Int,
    val nomrbe: String,
    val mail: String,
    val telefono: String? = null,
    val direccion: String? ? = null
)