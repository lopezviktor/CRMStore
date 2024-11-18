package com.example.crmstore.modelo

data class Cliente(
    val nombre: String = "",
    val apellidos: String = "",
    val mail: String = "",
    val telefono: String? = null,
    val direccion: String? = null
)