package com.example.kapu.adapter

import android.content.DialogInterface.OnClickListener
import android.graphics.BitmapFactory
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kapu.Donation
import com.example.kapu.Ong
import com.example.kapu.User
import com.example.kapu.databinding.DonationLayoutBinding
import com.example.kapu.databinding.OngLayoutBinding
import com.example.kapu.databinding.UserLayoutBinding

class DonationViewHolder(
    private val view: View,
    private val onItemSelected: (Donation) -> Unit,
    private val onEditItem: (Donation) -> Unit,
    private val onDeleteItem: (Donation) -> Unit
):RecyclerView.ViewHolder(view) {
    val binding = DonationLayoutBinding.bind(view)
    fun render(donationModel: Donation, currrentUser: User?, onItemSelected: (Donation) -> Unit) {
        binding.tvDonation.setText(donationModel.title)
        if(currrentUser?.collaborator == true){
            binding.llBtnsDonation.visibility = View.VISIBLE
        } else {
            binding.llBtnsDonation.visibility = View.GONE
        }
        itemView.setOnClickListener { onItemSelected(donationModel) }
        binding.btnEditDonation.setOnClickListener {
            onEditItem(donationModel)
        }
        binding.btnDeleteDonation.setOnClickListener {
            onDeleteItem(donationModel)
        }
        donationModel.image?.let { imgByteArray ->
            val bitmap = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.size)
            binding.ivDonation.setImageBitmap(bitmap)
        }
    }
}