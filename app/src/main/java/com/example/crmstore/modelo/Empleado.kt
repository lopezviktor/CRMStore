package com.example.crmstore.modelo

data class Empleado(
    val nombre: String = "",
    val apellidos: String = "",
    val mail: String = "",
    val telefono: String? = null,
    val puesto: String? = null,
    val salarioBase: Double = 0.0,
    val complementos: List<Complemento> = emptyList(),
    val pagas: Int = 14,
    val tipoContrato: TipoContrato = TipoContrato.INDEFINIDO
)

enum class TipoContrato {
    INDEFINIDO,
    TEMPORAL,
    PRACTICAS,
    PARCIAL
}

data class Complemento(
    val tipo: TipoComplemento = TipoComplemento.ANTIGUEDAD,
    val valor: Double = 0.0
) {
    constructor() : this(TipoComplemento.ANTIGUEDAD, 0.0)
}
enum class TipoComplemento {
    ANTIGUEDAD,
    PRODUCTIVIDAD,
    NOCTURNIDAD,
    PELIGROSIDAD,
    IDIOMAS,
    RESPONSABILIDAD
}