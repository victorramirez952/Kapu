package com.example.kapu.adapter

import android.content.DialogInterface.OnClickListener
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kapu.Ong
import com.example.kapu.User
import com.example.kapu.Volunteering
import com.example.kapu.VolunteeringClass
import com.example.kapu.databinding.OngLayoutBinding
import com.example.kapu.databinding.UserLayoutBinding
import com.example.kapu.databinding.VolunteeringLayoutBinding

class VolunteeringViewHolder(
    private val view: View,
    private val onItemSelected: (VolunteeringClass) -> Unit,
    private val onEditItem: (VolunteeringClass) -> Unit,
    private val onDeleteItem: (VolunteeringClass) -> Unit
):RecyclerView.ViewHolder(view) {
    val binding = VolunteeringLayoutBinding.bind(view)
    fun render(volunteeringModel: VolunteeringClass, currentUser: User?, weekdaysString: String, onItemSelected: (VolunteeringClass) -> Unit) {
        binding.tvVolunteeringTitle.setText(volunteeringModel.title)
        binding.tvVolunteeringDate.setText(volunteeringModel.startDate + " - " + volunteeringModel.endDate)
        binding.tvVolunteeringWeekdays.setText(weekdaysString)
        binding.tvStartTime.setText(volunteeringModel.startTime)
        binding.tvEndTime.setText(volunteeringModel.endTime)
        if(currentUser?.collaborator == true){
            binding.llDonationBtns.visibility = View.VISIBLE
            binding.btnParticipate.visibility = View.GONE
        } else {
            binding.llDonationBtns.visibility = View.GONE
            binding.btnParticipate.visibility = View.VISIBLE
        }
        itemView.setOnClickListener { onItemSelected(volunteeringModel) }
        binding.btnEdit.setOnClickListener {
            onEditItem(volunteeringModel)

        }
        binding.btnDelete.setOnClickListener {
            onDeleteItem(volunteeringModel)
        }
    }
}