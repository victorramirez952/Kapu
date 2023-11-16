package com.example.kapu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kapu.databinding.FragmentAddVolunteeringBinding
import com.example.kapu.databinding.FragmentEditVolunteeringBinding

class AddVolunteering : Fragment() {
    private lateinit var binding: FragmentAddVolunteeringBinding
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
        binding = FragmentAddVolunteeringBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(context)
        db = DB(requireContext())
        binding.btnAdd.setOnClickListener {
            addVolunteering()
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Volunteering())
                .addToBackStack(null)
                .commit()
        }
        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Volunteering())
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getOng()
    }

    private fun addVolunteering(){
        if(currentOng != null){
            val title = binding.etVoluntariado.text.toString().trim()
            val startDate = binding.etStartDate.text.toString().trim()
            val endDate = binding.etEndDate.text.toString().trim()
            val startTime = binding.etStartTime.text.toString().trim()
            val endTime = binding.etEndTime.text.toString().trim()
            var volunteering_class = VolunteeringClass(0, title, startDate, endDate, startTime, endTime, currentOng!!.id_ong)
            var dayList = mutableListOf<Boolean>()
            dayList.add(binding.cbMonday.isChecked)
            dayList.add(binding.cbTuesday.isChecked)
            dayList.add(binding.cbWednesday.isChecked)
            dayList.add(binding.cbThursday.isChecked)
            dayList.add(binding.cbFriday.isChecked)
            dayList.add(binding.cbSaturday.isChecked)
            dayList.add(binding.cbSunday.isChecked)

            try {
                val auxVolunteering = db?.AddVolunteering(volunteering_class, dayList)
                if(auxVolunteering != null) {
                    Toast.makeText(context, "Voluntariado: ${auxVolunteering.title} agregado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "No se pudo editar los datos", Toast.LENGTH_SHORT).show()
                }
            } catch (e:Exception){
                Log.d("Voltorn", "Error: ${e.message}", e)
            }
        }
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
}