package com.example.kapu

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kapu.adapter.VolunteeringAdapter
import com.example.kapu.databinding.FragmentDonationsBinding
import com.example.kapu.databinding.FragmentEditDonationBinding
import com.example.kapu.databinding.FragmentEditOngDescBinding

class EditDonation : Fragment() {
    private lateinit var binding: FragmentEditDonationBinding
    private lateinit var sessionManager: SessionManager
    private var db:DB? = null
    private lateinit var currentDonation: Donation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditDonationBinding.inflate(inflater, container, false)
        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Donations())
                .addToBackStack(null)
                .commit()
        }
        binding.btnSaveChanges.setOnClickListener {
            Toast.makeText(context, "Testing: Has editado la info del donativo", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Donations())
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }
}