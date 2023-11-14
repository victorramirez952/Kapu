package com.example.kapu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kapu.Ong
import com.example.kapu.R
import com.example.kapu.User

class OngAdapter(private val ongList:List<Ong>,
                  private val onItemSelected: (Ong) -> Unit,
): RecyclerView.Adapter<OngViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OngViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.ong_layout, parent, false)
        return OngViewHolder(view, onItemSelected)
    }

    override fun onBindViewHolder(holder: OngViewHolder, position: Int) {
        val ong = ongList[position]
        holder.render(ong, onItemSelected)
    }

    override fun getItemCount(): Int = ongList.size
}