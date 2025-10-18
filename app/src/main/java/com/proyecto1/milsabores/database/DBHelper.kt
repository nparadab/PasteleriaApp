package com.proyecto1.milsabores.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.proyecto1.milsabores.model.Producto

class DBHelper(context: Context) : SQLiteOpenHelper(context, "PasteleriaDB", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE productos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                precio REAL NOT NULL,
                descripcion TEXT,
                categoria TEXT,
                stock INTEGER,
                fechaCreacion TEXT,
                imagenUri TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS productos")
        onCreate(db)
    }

    fun insertarProducto(
        nombre: String,
        precio: Double,
        descripcion: String,
        categoria: String,
        stock: Int,
        fechaCreacion: String,
        imagenUri: String?
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("precio", precio)
            put("descripcion", descripcion)
            put("categoria", categoria)
            put("stock", stock)
            put("fechaCreacion", fechaCreacion)
            put("imagenUri", imagenUri)
        }

        val resultado = db.insert("productos", null, values)
        if (resultado != -1L) {
            Log.d("DBHelper", "✅ Producto insertado: $nombre, precio: $precio, categoría: $categoria")
            return true
        } else {
            Log.e("DBHelper", "❌ Error al insertar producto: $nombre")
            return false
        }
    }

    fun obtenerProductos(): List<Producto> {
        val productos = mutableListOf<Producto>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM productos", null)
        while (cursor.moveToNext()) {
            productos.add(
                Producto(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio")),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                    categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria")),
                    stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock")),
                    fechaCreacion = cursor.getString(cursor.getColumnIndexOrThrow("fechaCreacion")),
                    imagenUri = cursor.getString(cursor.getColumnIndexOrThrow("imagenUri"))
                )
            )
        }
        cursor.close()
        return productos
    }
}
