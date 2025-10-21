package com.example.roboranger.ui.views.form

enum class WeatherOptions(val emoji: String, val label: String) {
    SOLEADO("☀️", "Soleado"),
    NUBLADO("☁️", "Nublado"),
    LLUVIOSO("🌧️", "Lluvioso")
}

enum class SeasonOptions(val label: String) {
    PRIMAVERA("Primavera"),
    VERANO("Verano"),
    OTONO("Otoño"),
    INVIERNO("Invierno")
}

enum class AnimalOptions(val emoji: String, val label: String) {
    MAMIFERO("🦌", "Mamífero"),
    AVE("🦜", "Ave"),
    REPTIL("🐍", "Reptil"),
    ANFIBIO("🐸", "Anfibio"),
    INSECTO("🐝", "Insecto")
}

enum class ObservationsTypeOptions(val label: String) {
    LA_VIO("La Vió"),
    HUELLA("Huella"),
    RASTRO("Rastro"),
    CACERIA("Cacería"),
    LE_DIJERON("Le Dijeron")
}

enum class ZoneOptions(val label: String) {
    BOSQUE("Bosque"),
    ARREGLO_AGROFORESTAL("Arreglo Agroforestal"),
    CULTIVOS_TRANSITORIOS("Cultivos Transitorios"),
    CULTIVOS_PERMANENTES("Cultivos Permanentes")
}

enum class FormType { NONE, FORM1, FORM7 }