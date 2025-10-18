package com.proyecto1.milsabores.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.proyecto1.milsabores.model.Producto

class DBHelper(context: Context) : SQLiteOpenHelper(context, "PasteleriaDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE productos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                precio REAL
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS productos")
        onCreate(db)
    }

    fun insertarProducto(nombre: String, precio: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("precio", precio)
        }
        return db.insert("productos", null, values) != -1L
    }

    fun obtenerProductos(): List<Producto> {
        val productos = mutableListOf<Producto>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM productos", null)
        while (cursor.moveToNext()) {
            productos.add(
                Producto(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2)
                )
            )
        }
        cursor.close()
        return productos
    }
}
