package com.proyecto1.milsabores.model

import java.io.Serializable

data class ProductoDTO(
    val id: Long? = null, // null al crear, obligatorio al editar
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val stock: Int,
    val categoria: CategoriaDTO
) : Serializable
