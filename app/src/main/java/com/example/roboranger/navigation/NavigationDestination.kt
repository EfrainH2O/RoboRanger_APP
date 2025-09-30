package com.example.roboranger.navigation

interface NavigationDestination {
    // Identificador unico para definir la ruta de una vista
    val route: String
    // Id del string resource que contiene el titulo de la vista desplegada
    val titleRes: Int
}