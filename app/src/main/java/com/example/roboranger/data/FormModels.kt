package com.example.roboranger.data

enum class Clima {
    SOLEADO, NUBLADO, LLUVIOSO
}

enum class Temporada {
    SECA, LLUVIOSA, TRANSICION
}

enum class TipoAnimal {
    MAMIFERO, AVE, REPTIL, ANFIBIO, INSECTO
}

enum class TipoObservacion {
    DIRECTA, HUELLA, RASTRO, CACERIA, INDIRECTA, AUDITIVA
}

enum class Zona {
    BOSQUE, ARREGLO_AGROFORESTAL, CULTIVOS_TRANSITORIOS, CULTIVOS_PERMANENTES
}

enum class AlturaObservacion {
    BAJA, MEDIA, ALTA
}

enum class Cobertura {
    BD, RA, RB, PA, PL, CP, CT, VH, TD, IF
}

enum class Disturbio {
    INUNDACION, QUEMA, TALA, EROSION, MINERIA, CARRETERA, MAS_PLANTAS_ACUATICAS, OTRO
}

enum class HabitoDeCrecimiento {
    ARBUSTO, ARBOLITO, ARBOL
}

/**
 * Formulario 1: Fauna en Transectos
 */
data class Formulario1(
    val transecto: String = "",
    val clima: Clima? = null,
    val temporada: Temporada? = null,
    val tipoAnimal: TipoAnimal? = null,
    val nombreComun: String = "",
    val nombreCientifico: String = "",
    val numeroIndividuos: String = "",
    val tipoObservacion: TipoObservacion? = null,
    val observaciones: String = ""
)

/**
 * Formulario 2: Fauna en Punto de Conteo
 */
data class Formulario2(
    val zona: String = "",
    val clima: Clima? = null,
    val temporada: Temporada? = null,
    val tipoAnimal: TipoAnimal? = null,
    val nombreComun: String = "",
    val nombreCientifico: String = "",
    val numeroIndividuos: String = "",
    val tipoObservacion: TipoObservacion? = null,
    val alturaObservacion: String = "",
    val observaciones: String = ""
)

/**
 * Formulario 3: Validación de Cobertura
 */
data class Formulario3(
    val codigo: String = "",
    val clima: Clima? = null,
    val temporada: Temporada? = null,
    val seguimiento: Boolean = false,
    val cambio: Boolean = false,
    val cobertura: String = "",
    val tipoCultivo: String = "",
    val disturbio: String = "",
    val observaciones: String = ""
)

/**
 * Formulario 4: Parcela de Vegetación
 */
data class Formulario4(
    val codigo: String = "",
    val clima: Clima? = null,
    val temporada: Temporada? = null,
    val quad_a: String = "",
    val quad_b: String = "",
    val sub_quad: String = "",
    val habitoDeCrecimiento: String = "",
    val nombreComun: String = "",
    val nombreCientifico: String = "",
    val placa: String = "",
    val circunferencia: String = "",
    val distancia: String = "",
    val estatura: String = "",
    val altura: String = "",
    val observaciones: String = ""
)

/**
 * Formulario 5: Fauna Búsqueda Libre
 * (Es idéntico al Formulario 2)
 */
data class Formulario5(
    val zona: String = "",
    val clima: Clima? = null,
    val temporada: Temporada? = null,
    val tipoAnimal: TipoAnimal? = null,
    val nombreComun: String = "",
    val nombreCientifico: String = "",
    val numeroIndividuos: String = "",
    val tipoObservacion: TipoObservacion? = null,
    val alturaObservacion: String = "",
    val observaciones: String = ""
)

/**
 * Formulario 6: Cámaras Trampa
 */
data class Formulario6(
    val codigo: String = "",
    val clima: Clima? = null,
    val temporada: Temporada? = null,
    val zona: String = "",
    val nombreCamara: String = "",
    val placaCamara: String = "",
    val placaGuaya: String = "",
    val anchoCamino: String = "",
    val fechaInstalacion: String = "",
    val distanciaObjetivo: String = "",
    val alturaLente: String = "",
    val checklist: String = "",
    val observaciones: String = ""
)

/**
 * Formulario 7: Variables Climáticas
 */
data class Formulario7(
    val clima: Clima? = null,
    val temporada: Temporada? = null,
    val zona: String = "",
    val pluviosidad: String = "",
    val temperaturaMaxima: String = "",
    val humedadMaxima: String = "",
    val temperaturaMinima: String = "",
    val nivelQuebrada: String = ""
)