package com.example.kapu.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.kapu.VolunteeringClass
import com.example.kapu.databinding.ParticipationLayoutBinding

class ParcipationViewHolder(
    private val view: View,
    private val onParticipateItem: (VolunteeringClass) -> Unit
):RecyclerView.ViewHolder(view) {
    val binding = ParticipationLayoutBinding.bind(view)
    fun render(volunteeringModel: VolunteeringClass){
        binding.tvParticipation.text = volunteeringModel.title
        binding.btnDelete.setOnClickListener {
            onParticipateItem(volunteeringModel)
        }
    }
}