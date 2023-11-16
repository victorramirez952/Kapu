package com.example.kapu

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kapu.databinding.FragmentEditDonationBinding
import com.example.kapu.databinding.FragmentEditVolunteeringBinding
import com.example.kapu.databinding.FragmentVolunteeringBinding

class EditVolunteering : Fragment() {
    private lateinit var binding: FragmentEditVolunteeringBinding
    private lateinit var sessionManager: SessionManager
    private var db:DB? = null
    private lateinit var currentVolunteering: VolunteeringClass
    private var current_id_volunteering: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            current_id_volunteering = it.getInt("id_volunteering", 0)
            getVolunteering()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditVolunteeringBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(context)
        db = DB(requireContext())
        getVolunteering()
        binding.btnEdit.setOnClickListener {
             editVolunteering()
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

    private fun getVolunteering(){
        if(current_id_volunteering != 0){
            try {
                val auxCurrentVolunteering = db?.GetVolunteering(current_id_volunteering)
                if(auxCurrentVolunteering != null) {
                    currentVolunteering = VolunteeringClass(auxCurrentVolunteering.id_volunteering, auxCurrentVolunteering.title, auxCurrentVolunteering.startDate, auxCurrentVolunteering.endDate, auxCurrentVolunteering.startTime, auxCurrentVolunteering.endTime, auxCurrentVolunteering.id_ong)
                    binding.etVoluntariado.text = Editable.Factory.getInstance().newEditable(currentVolunteering.title)
                    binding.etStartDate.text = Editable.Factory.getInstance().newEditable(currentVolunteering.startDate)
                    binding.etEndDate.text = Editable.Factory.getInstance().newEditable(currentVolunteering.endDate)
                    binding.etStartTime.text = Editable.Factory.getInstance().newEditable(currentVolunteering.startTime)
                    binding.etEndTime.text = Editable.Factory.getInstance().newEditable(currentVolunteering.endTime)

                    val weekdays = getWeekdaysFromCursor(currentVolunteering.id_volunteering)

                    binding.cbMonday.isChecked = 1 in weekdays
                    binding.cbTuesday.isChecked = 2 in weekdays
                    binding.cbWednesday.isChecked = 3 in weekdays
                    binding.cbThursday.isChecked = 4 in weekdays
                    binding.cbFriday.isChecked = 5 in weekdays
                    binding.cbSaturday.isChecked = 6 in weekdays
                    binding.cbSunday.isChecked = 7 in weekdays

                }
            } catch (e:Exception){
                Log.d("Voltorn", "Error: ${e.message}", e)
            }

        }
    }

    private fun editVolunteering(){
        if(currentVolunteering != null && current_id_volunteering != 0){
            val title = binding.etVoluntariado.text.toString().trim()
            val startDate = binding.etStartDate.text.toString().trim()
            val endDate = binding.etEndDate.text.toString().trim()
            val startTime = binding.etStartTime.text.toString().trim()
            val endTime = binding.etEndTime.text.toString().trim()
            var volunteering_class = VolunteeringClass(current_id_volunteering, title, startDate, endDate, startTime, endTime, currentVolunteering.id_ong)

            var dayList = mutableListOf<Boolean>()
            dayList.add(binding.cbMonday.isChecked)
            dayList.add(binding.cbTuesday.isChecked)
            dayList.add(binding.cbWednesday.isChecked)
            dayList.add(binding.cbThursday.isChecked)
            dayList.add(binding.cbFriday.isChecked)
            dayList.add(binding.cbSaturday.isChecked)
            dayList.add(binding.cbSunday.isChecked)

            try {
                val auxVolunteering = db?.EditVolunteering(volunteering_class, dayList)
                if(auxVolunteering != null) {
                    Toast.makeText(context, "Voluntariado: ${auxVolunteering.title} editado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "No se pudo editar los datos", Toast.LENGTH_SHORT).show()
                }
            } catch (e:Exception){
                Log.d("Voltorn", "Error: ${e.message}", e)
            }
        }
    }


    private fun getWeekdaysFromCursor(id_volunteering: Int): List<Int> {
        val weekdaysList = mutableListOf<Int>()

        val queryDates = "SELECT id_weekday FROM volunteering_weekday WHERE id_volunteering=$id_volunteering"
        val cursor = db?.FireQuery(queryDates)

        cursor?.use { c ->
            if (c.moveToFirst()) {
                do {
                    val weekday = c.getInt(c.getColumnIndexOrThrow("id_weekday"))
                    weekdaysList.add(weekday)
                } while (c.moveToNext())
            }
        }

        return weekdaysList
    }

    override fun onStart() {
        super.onStart()
        arguments?.let {
            current_id_volunteering = it.getInt("id_volunteering", 0)
            Log.d("Voltorn", "Id volunteering editVolunteering: ${current_id_volunteering}")
            getVolunteering()
        }
        getVolunteering()
    }
}