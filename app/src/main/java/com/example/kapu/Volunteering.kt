package com.example.kapu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kapu.adapter.OngAdapter
import com.example.kapu.adapter.VolunteeringAdapter
import com.example.kapu.databinding.FragmentDonationsBinding
import com.example.kapu.databinding.FragmentVolunteeringBinding

class Volunteering : Fragment() {
    private lateinit var binding: FragmentVolunteeringBinding
    private lateinit var sessionManager: SessionManager
    private var db:DB? = null
    private var currentUser: User? = null
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
        binding = FragmentVolunteeringBinding.inflate(inflater, container, false)
        db = DB(requireContext())
        sessionManager = SessionManager(context)
        checkLogin()
        binding.btnGo2Map.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Map())
                .addToBackStack(null)
                .commit()
        }
        binding.btnEditOng.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, EditOngInfo())
                .addToBackStack(null)
                .commit()
        }
        if(currentUser?.collaborator == false){
            binding.btnEditOng.visibility = View.GONE
            binding.btnAddVolunteering.visibility = View.GONE
        } else {
            binding.btnEditOng.visibility = View.VISIBLE
            binding.btnAddVolunteering.visibility = View.VISIBLE
            binding.btnEditOng.setOnClickListener{
                parentFragmentManager.beginTransaction()
                    .replace(R.id.upper_fragment, EditOngInfo())
                    .addToBackStack(null)
                    .commit()
            }
            binding.btnAddVolunteering.setOnClickListener{
                parentFragmentManager.beginTransaction()
                    .replace(R.id.upper_fragment, AddVolunteering())
                    .addToBackStack(null)
                    .commit()
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        checkLogin()
        getOng()
        initRecyclerView()
    }

    private fun checkLogin(){
        try {
            if(sessionManager.isLogin() == false){
                val intent = Intent(context, Login::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                currentUser = db?.GetUser(sessionManager.getUserEmail())
                if(currentUser == null) {
                    Toast.makeText(
                        context,
                        "Un error en informacion buscada, parece haber",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e:Exception){
            Log.d("Voltorn", "Error: ${e.message}")
        }
    }

    private fun getOng(){
        try {
            currentOng = db?.GetOng(sessionManager.getOngId())
            if(currentOng != null) {
                binding.tvNameOng.text = currentOng?.name
                binding.tv2OngPhone.text = currentOng?.phone
                binding.tv2OngEmail.text = currentOng?.email
                binding.tv2OngAddress.text = currentOng?.address
            }
        } catch (e:Exception){
            Log.d("Voltorn", "Error: ${e.message}")
        }
    }

    private fun initRecyclerView(){
        binding.rvVolunteering.adapter = null
        if(currentOng != null){
            val manager = LinearLayoutManager(requireContext())
            val decoration = DividerItemDecoration(requireContext(), manager.orientation)
            binding.rvVolunteering.layoutManager = LinearLayoutManager(requireContext())
            try {
                val query = "SELECT * FROM volunteering WHERE id_ong=${currentOng?.id_ong}"
                db?.FireQuery(query)?.use {
                    if (it.count > 0) {
                        var weekdaysString: String
                        val weekdaysList = mutableListOf<String>()
                        val volunteeringList = mutableListOf<VolunteeringClass>()
                        do {
                            var id_volunteering = it.getInt(it.getColumnIndexOrThrow("id_volunteering"))
                            var title = it.getString(it.getColumnIndexOrThrow("title"))
                            var startDate = it.getString(it.getColumnIndexOrThrow("startDate"))
                            var endDate = it.getString(it.getColumnIndexOrThrow("endDate"))
                            var startTime = it.getString(it.getColumnIndexOrThrow("startTime"))
                            var endTime = it.getString(it.getColumnIndexOrThrow("endTime"))
                            var id_ong = it.getInt(it.getColumnIndexOrThrow("id_ong"))
                            val weekdaysString = getWeekdaysString(id_volunteering)
                            weekdaysList.add(weekdaysString)

                            var volunteering = VolunteeringClass(id_volunteering, title, startDate, endDate, startTime, endTime, id_ong)
                            volunteeringList.add(volunteering)

                        } while (it.moveToNext())
                        val volunteeringAdapter = VolunteeringAdapter(volunteeringList,
                            currentUser,
                            onItemSelected = { volunteering -> onItemSelected(volunteering) },
                            onEditItem = { volunteering -> onEditItem(volunteering) },
                            onDeleteItem = { volunteering -> onDeleteItem(volunteering) },
                            onParcipateItem = { volunteering -> onParcipateItem(volunteering) },
                            weekdaysList
                        )
                        binding.rvVolunteering.adapter = volunteeringAdapter
                    }
                }
            } catch (e:Exception){
                Log.d("Error", "Error: ${e.message}", e)
            }
            binding.rvVolunteering.addItemDecoration(decoration)
        }
    }

    private fun getWeekdaysString(idVolunteering: Int): String {
        val queryDates = "SELECT * FROM volunteering_weekday WHERE id_volunteering=$idVolunteering"
        val cursor = db?.FireQuery(queryDates)

        val weekdaysMap = mapOf(
            1 to "L", 2 to "M", 3 to "Mie", 4 to "J", 5 to "V", 6 to "S", 7 to "D"
        )

        val weekdaysList = mutableListOf<String>()

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val idWeekday = it.getInt(it.getColumnIndexOrThrow("id_weekday"))
                    weekdaysMap[idWeekday]?.let { weekdayString -> weekdaysList.add(weekdayString) }
                } while (it.moveToNext())
            }
        }

        val weekdaysString = weekdaysList.joinToString(separator = "")
        return weekdaysString
    }

    fun onItemSelected(volunteering: VolunteeringClass){
        Toast.makeText(requireContext(), volunteering.title, Toast.LENGTH_SHORT).show()
    }

    fun onEditItem(volunteering: VolunteeringClass) {
        val editVolunteeringFragment = EditVolunteering()
        val bundle = Bundle()
        bundle.putInt("id_volunteering", volunteering.id_volunteering)
        editVolunteeringFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.upper_fragment, editVolunteeringFragment)
            .addToBackStack(null)
            .commit()
    }

    fun onDeleteItem(volunteering: VolunteeringClass) {
        var result = db?.DeleteVolunteering(volunteering)
        if(result != null){
            Toast.makeText(context, "Volunteering ${volunteering.title} eliminado", Toast.LENGTH_SHORT).show()
            initRecyclerView()
        } else {
            Toast.makeText(context, "No se pudo eliminar el usuario", Toast.LENGTH_SHORT).show()
        }
    }

    fun onParcipateItem(volunteering: VolunteeringClass) {
        if (currentUser != null) {
            val result = db?.insertUserVolunteering(currentUser!!.id_user, volunteering.id_volunteering)

            if (result != null && result > 0) {
                Toast.makeText(context, "Guardado", Toast.LENGTH_SHORT).show()
                initRecyclerView()
            } else {
                Toast.makeText(context, "No se pudo agregar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}