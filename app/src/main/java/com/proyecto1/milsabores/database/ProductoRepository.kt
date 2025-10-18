package com.proyecto1.milsabores.database

import android.content.Context
import com.proyecto1.milsabores.model.Producto

class ProductoRepository(context: Context) {

    private val dbHelper = DBHelper(context)

    fun insertar(nombre: String, precio: Double): Boolean {
        return dbHelper.insertarProducto(nombre, precio)
    }

    fun obtenerTodos(): List<Producto> {
        return dbHelper.obtenerProductos()
    }
}
