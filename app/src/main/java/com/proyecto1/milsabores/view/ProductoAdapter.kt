package com.proyecto1.milsabores.view

import android.app.Dialog
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.model.Producto

class ProductoAdapter : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    private var productos: List<Producto> = emptyList()

    fun actualizarLista(nuevaLista: List<Producto>) {
        productos = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.id.text = "ID: ${producto.id}"
        holder.nombre.text = producto.nombre
        holder.precio.text = "$${producto.precio}"
        holder.textStock.text = "Stock: ${producto.stock}" // ✅ Mostrar stock

        // Cargar imagen en la lista
        Glide.with(holder.itemView.context)
            .load(producto.imagenUri)
            .thumbnail(0.1f)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.error_imagen)
            .into(holder.imagen)

        // Iniciar animación si el drawable es animado
        holder.imagen.post {
            val drawable = holder.imagen.drawable
            if (drawable is AnimationDrawable) {
                drawable.start()
            }
        }

        // Mostrar diálogo expandido al tocar el producto
        holder.itemView.setOnClickListener {
            val dialog = Dialog(holder.itemView.context)
            dialog.setContentView(R.layout.dialog_detalle_producto)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val nombre = dialog.findViewById<TextView>(R.id.txtNombreDetalle)
            val precio = dialog.findViewById<TextView>(R.id.txtPrecioDetalle)
            val imagen = dialog.findViewById<ImageView>(R.id.imgDetalle)

            nombre.text = producto.nombre
            precio.text = "$${producto.precio}"

            Glide.with(dialog.context)
                .load(producto.imagenUri)
                .thumbnail(0.1f)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.error_imagen)
                .into(imagen)

            dialog.show()
        }
    }

    override fun getItemCount(): Int = productos.size

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.txtId)
        val nombre: TextView = itemView.findViewById(R.id.txtNombre)
        val precio: TextView = itemView.findViewById(R.id.txtPrecio)
        val imagen: ImageView = itemView.findViewById(R.id.imgProducto)
        val textStock: TextView = itemView.findViewById(R.id.textStock) // ✅ Nuevo campo
    }
}
