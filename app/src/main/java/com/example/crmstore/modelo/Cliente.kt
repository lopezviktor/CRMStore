package com.example.crmstore.modelo

data class Cliente(
    var id: String = "",
    val dni: String = "",
    var nombre: String = "",
    var apellidos: String = "",
    val mail: String = "",
    val telefono: String? = null,
    val direccion: String? = null,

    )
