package com.example.crmstore.modelo

data class Empleado(
    val nombre: String = "",
    val apellidos: String = "",
    val mail: String = "",
    val telefono: String? = null,
    val puesto: String? = null,
    val salarioBase: Double = 0.0,
    val complementos: List<Complemento> = emptyList(),
// Si contamos con 14 pagas de 12 mensualidades + 2 extras
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
    val tipo: TipoComplemento,
    val valor: Double
)
enum class TipoComplemento{
    ANTIGUEDAD,
    PRODUCTIVIDAD,
    NOCTURNIDAD,
    PELIGROSIDAD,
    IDIOMAS,
    RESPONSABILIDAD
}
