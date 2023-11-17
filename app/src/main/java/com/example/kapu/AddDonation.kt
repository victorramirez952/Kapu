package com.example.kapu

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kapu.databinding.FragmentAddDonationBinding
import com.example.kapu.databinding.FragmentEditOngDescBinding

class AddDonation : Fragment() {
    private lateinit var binding: FragmentAddDonationBinding
    private lateinit var sessionManager: SessionManager
    private var db:DB? = null
    private var currentOng: Ong? = null
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
        binding = FragmentAddDonationBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(context)
        db = DB(requireContext())
        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Donations())
                .addToBackStack(null)
                .commit()
        }
        binding.btnAdd.setOnClickListener {
            addDonation()
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Donations())
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getOng()
    }

    private fun getOng() {
        try {
            currentOng = db?.GetOng(sessionManager.getOngId())
            if (currentOng == null) {
                Toast.makeText(
                    context,
                    "Un error en informacion buscada, parece haber",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Log.d("Voltorn", "Error: ${e.message}")
        }
    }

    private fun addDonation(){
        if(currentOng != null){
            val title = binding.etDonation.text.toString().trim()
            // val image = binding.ivDonation.text.toString().trim()
            val id_ong = currentOng?.id_ong
            val donation = Donation(0, title, currentOng!!.id_ong)
            try {
                val auxDonation = db?.AddDonation(donation)
                if(auxDonation != null) {
                    Toast.makeText(context, "Donativo: ${auxDonation.title} agregado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "No se pudo editar los datos", Toast.LENGTH_SHORT).show()
                }
            } catch (e:Exception){
                Log.d("Voltorn", "Error: ${e.message}", e)
            }
        }
    }
}