package com.example.kapu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kapu.R
import com.example.kapu.VolunteeringClass

class ParticipationAdapter(private val volunteeringList:List<VolunteeringClass>,
                          private val onDeleteItem: (VolunteeringClass) -> Unit
): RecyclerView.Adapter<ParcipationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParcipationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.participation_layout, parent, false)
        return ParcipationViewHolder(view, onDeleteItem)
    }

    override fun onBindViewHolder(holder: ParcipationViewHolder, position: Int) {
        val volunteering = volunteeringList[position]
        holder.render(volunteering)
    }
    override fun getItemCount(): Int = volunteeringList.size
}