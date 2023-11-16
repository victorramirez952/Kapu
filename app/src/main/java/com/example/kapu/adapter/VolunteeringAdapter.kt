package com.example.kapu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kapu.Donation
import com.example.kapu.R
import com.example.kapu.User
import com.example.kapu.Volunteering
import com.example.kapu.VolunteeringClass

class VolunteeringAdapter(private val volunteeringList:List<VolunteeringClass>,
                          private val user: User?,
                      private val onItemSelected: (VolunteeringClass) -> Unit,
                          private val onEditItem: (VolunteeringClass) -> Unit,
                          private val onDeleteItem: (VolunteeringClass) -> Unit,
                          private val weekdaysList:List<String>
): RecyclerView.Adapter<VolunteeringViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolunteeringViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.volunteering_layout, parent, false)
        return VolunteeringViewHolder(view, onItemSelected, onEditItem, onDeleteItem)
    }

    override fun onBindViewHolder(holder: VolunteeringViewHolder, position: Int) {
        val volunteering = volunteeringList[position]
        val weekdaysString = weekdaysList[position]
        holder.render(volunteering, user, weekdaysString, onItemSelected)
    }

    override fun getItemCount(): Int = volunteeringList.size
}