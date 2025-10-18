package com.proyecto1.milsabores.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
    }

    override fun getItemCount(): Int = productos.size

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.txtId)
        val nombre: TextView = itemView.findViewById(R.id.txtNombre)
        val precio: TextView = itemView.findViewById(R.id.txtPrecio)
    }
}
