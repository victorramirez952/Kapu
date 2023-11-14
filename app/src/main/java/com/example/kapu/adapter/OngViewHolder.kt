package com.example.kapu.adapter

import android.content.DialogInterface.OnClickListener
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kapu.Ong
import com.example.kapu.User
import com.example.kapu.databinding.OngLayoutBinding
import com.example.kapu.databinding.UserLayoutBinding

class OngViewHolder(
    private val view: View,
    private val onItemSelected: (Ong) -> Unit
):RecyclerView.ViewHolder(view) {
    val binding = OngLayoutBinding.bind(view)
    fun render(ongModel: Ong, onItemSelected: (Ong) -> Unit) {
        binding.tvOng.setText(ongModel.name)
        itemView.setOnClickListener { onItemSelected(ongModel) }
    }
}