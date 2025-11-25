package com.proyecto1.milsabores.view

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.model.ProductoDTO

class ProductoAdapter(
    private val onItemClick: (ProductoDTO) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    private var productos: List<ProductoDTO> = emptyList()

    fun actualizarLista(nuevaLista: List<ProductoDTO>) {
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

        holder.txtId.text = "ID: ${producto.id ?: "-"}"
        holder.txtNombre.text = producto.nombre
        holder.txtPrecio.text = "$${producto.precio}"
        holder.txtStock.text = "Stock: ${producto.stock}"
        holder.txtCategoria.text = "Categoría: ${producto.categoria.nombre}"

        // Mostrar diálogo expandido al mantener presionado
        holder.itemView.setOnLongClickListener {
            val dialog = Dialog(holder.itemView.context)
            dialog.setContentView(R.layout.dialog_detalle_producto)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val nombre = dialog.findViewById<TextView>(R.id.txtNombreDetalle)
            val precio = dialog.findViewById<TextView>(R.id.txtPrecioDetalle)
            val categoria = dialog.findViewById<TextView>(R.id.txtCategoriaDetalle)

            nombre.text = producto.nombre
            precio.text = "$${producto.precio}"
            categoria.text = "Categoría: ${producto.categoria.nombre}"

            dialog.show()
            true
        }

        // Notificar selección al hacer clic
        holder.itemView.setOnClickListener {
            onItemClick(producto)
            Toast.makeText(holder.itemView.context, "Seleccionado: ${producto.nombre}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = productos.size

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtId: TextView = itemView.findViewById(R.id.txtId)
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecio)
        val txtStock: TextView = itemView.findViewById(R.id.txtStock)
        val txtCategoria: TextView = itemView.findViewById(R.id.txtCategoria)
    }
}
