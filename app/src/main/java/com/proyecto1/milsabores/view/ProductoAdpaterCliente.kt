package com.proyecto1.milsabores.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.model.ProductoDTO

class ProductoAdapterCliente(private var productos: List<ProductoDTO>) :
    RecyclerView.Adapter<ProductoAdapterCliente.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.tvNombreProducto)
        val precio: TextView = itemView.findViewById(R.id.tvPrecioProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_cliente, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.nombre.text = producto.nombre
        holder.precio.text = "$${producto.precio}"
    }

    override fun getItemCount(): Int = productos.size

    fun actualizarLista(nuevaLista: List<ProductoDTO>) {
        productos = nuevaLista
        notifyDataSetChanged()
    }
}
