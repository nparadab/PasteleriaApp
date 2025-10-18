package com.proyecto1.milsabores.model

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val categoria: String,
    val stock: Int,
    val fechaCreacion: String,
    val imagenUri: String? = null
)
