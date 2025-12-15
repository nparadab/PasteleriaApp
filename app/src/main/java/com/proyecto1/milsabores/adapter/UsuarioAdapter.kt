package com.proyecto1.milsabores.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.model.Usuario

class UsuarioAdapter(private val lista: List<Usuario>) :
    RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.tvNombreUsuario)
        val email: TextView = itemView.findViewById(R.id.tvEmailUsuario)
        val rol: TextView = itemView.findViewById(R.id.tvRolUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = lista[position]
        holder.nombre.text = usuario.nombre
        holder.email.text = usuario.email
        holder.rol.text = usuario.rol
    }

    override fun getItemCount(): Int = lista.size
}
