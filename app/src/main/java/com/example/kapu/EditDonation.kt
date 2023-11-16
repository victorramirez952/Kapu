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
    private var current_id_donation: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {1
        super.onCreate(savedInstanceState)
        arguments?.let {
            current_id_donation = it.getInt("id_donation", 0)
            getDonation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditDonationBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(context)
        db = DB(requireContext())
        getDonation()
        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Donations())
                .addToBackStack(null)
                .commit()
        }
        binding.btnSaveChanges.setOnClickListener {
            Toast.makeText(context, "Testing: Has editado la info del donativo", Toast.LENGTH_SHORT).show()
            editDonation()
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Donations())
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    private fun getDonation(){
        if(current_id_donation != 0){
            try {
                val auxCurrentDonation = db?.GetDonation(current_id_donation)
                if(auxCurrentDonation != null) {
                    currentDonation = Donation(auxCurrentDonation.id_donation, auxCurrentDonation.title, auxCurrentDonation.id_ong)
                    binding.etDonation.text = Editable.Factory.getInstance().newEditable(currentDonation?.title)
                }
            } catch (e:Exception){
                Log.d("Voltorn", "Error: ${e.message}", e)
            }
        }
    }

    private fun editDonation(){
        if(currentDonation != null && current_id_donation != 0){
            val title = binding.etDonation.text.toString().trim()
            currentDonation.title = title
            // val image = binding.ivDonation.text.toString().trim()

            try {
                val auxDonation = db?.EditDonation(currentDonation)
                if(auxDonation != null) {
                    Toast.makeText(context, "Donativo: ${auxDonation.title} editado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "No se pudo editar los datos", Toast.LENGTH_SHORT).show()
                }
            } catch (e:Exception){
                Log.d("Voltorn", "Error: ${e.message}", e)
            }
        }
    }
}