package com.example.kapu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kapu.Donation
import com.example.kapu.R
import com.example.kapu.User

class DonationAdapter(private val donationList:List<Donation>,
                      private val currentUser: User?,
                    private val onItemSelected: (Donation) -> Unit,
                      private val onEditItem: (Donation) -> Unit,
                      private val onDeleteItem: (Donation) -> Unit
): RecyclerView.Adapter<DonationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.donation_layout, parent, false)
        return DonationViewHolder(view, onItemSelected, onEditItem, onDeleteItem)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        val donation = donationList[position]
        holder.render(donation, currentUser, onItemSelected)
    }

    override fun getItemCount(): Int = donationList.size
}