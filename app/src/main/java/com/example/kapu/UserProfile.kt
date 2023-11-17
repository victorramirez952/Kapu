package com.example.kapu

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kapu.adapter.ParticipationAdapter
import com.example.kapu.adapter.VolunteeringAdapter
import com.example.kapu.databinding.FragmentMapBinding
import com.example.kapu.databinding.FragmentUserProfileBinding

class UserProfile : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private lateinit var sessionManager: SessionManager
    private var db:DB? = null
    private var currentUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onStart() {
        super.onStart()
        checkLogin()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(context)
        binding.etEmail.isEnabled = false
        binding.etFirstName.isEnabled = false
        binding.etLastName.isEnabled = false
        binding.etPhone.isEnabled = false
        binding.etPassword.isEnabled = false
        db = DB(requireContext())
        checkLogin()
        binding.btnLogout.setOnClickListener {
            try {
                sessionManager.removeData()
                val intent = Intent(context, Login::class.java)
                startActivity(intent)
                requireActivity().finish()
            } catch (e:Exception){
                Log.d("Voltorn", "Error: ${e.message}")
            }
        }
        binding.btnEditProfile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, EditProfile())
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    private fun checkLogin(){
        try {
            if(sessionManager.isLogin() == false){
                val intent = Intent(context, Login::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                currentUser = db?.GetUser(sessionManager.getUserEmail())
                if(currentUser != null){
                    val fullName = currentUser?.first_name + " " + currentUser?.last_name
                    binding.tvUsername.text = fullName
                    binding.etEmail.text = Editable.Factory.getInstance().newEditable(currentUser!!.email)
                    binding.etFirstName.text = Editable.Factory.getInstance().newEditable(currentUser!!.first_name)
                    binding.etLastName.text = Editable.Factory.getInstance().newEditable(currentUser!!.last_name)
                    binding.etPhone.text = Editable.Factory.getInstance().newEditable(currentUser!!.phone ?: "")
                    binding.etPassword.text = Editable.Factory.getInstance().newEditable(currentUser!!.password)
                } else{
                    Toast.makeText(context, "Hubo un error en la busqueda de informacion", Toast.LENGTH_SHORT).show()
                }

                if(currentUser?.collaborator == false){
                    binding.tvMyParticipations.visibility = View.VISIBLE
                    binding.rvMyParticipations.visibility = View.VISIBLE
                    initRecyclerView()
                } else {
                    binding.tvMyParticipations.visibility = View.GONE
                    binding.rvMyParticipations.visibility = View.GONE
                }
            }
        } catch (e:Exception){
            Log.d("Voltorn", "Error: ${e.message}")
        }
    }

    private fun initRecyclerView() {
        binding.rvMyParticipations.adapter = null
        if (currentUser != null && currentUser?.collaborator == false) {
            val manager = LinearLayoutManager(requireContext())
            val decoration = DividerItemDecoration(requireContext(), manager.orientation)
            binding.rvMyParticipations.layoutManager = LinearLayoutManager(requireContext())
            try {
                val query = "SELECT * FROM user_volunteering WHERE id_user=${currentUser?.id_user}"
                db?.FireQuery(query)?.use { cursor ->
                    if (cursor.count > 0) {
                        val volunteeringIds = mutableListOf<Int>()

                        do {
                            val idVolunteering = cursor.getInt(cursor.getColumnIndexOrThrow("id_volunteering"))
                            volunteeringIds.add(idVolunteering)
                        } while (cursor.moveToNext())

                        val volunteeringList = mutableListOf<VolunteeringClass>()
                        for (idVolunteering in volunteeringIds) {
                            val queryVolunteering = "SELECT * FROM volunteering WHERE id_volunteering=$idVolunteering"
                            db?.FireQuery(queryVolunteering)?.use { cursorVolunteering ->
                                if (cursorVolunteering.moveToFirst()) {
                                    val title = cursorVolunteering.getString(cursorVolunteering.getColumnIndexOrThrow("title"))
                                    val startDate = cursorVolunteering.getString(cursorVolunteering.getColumnIndexOrThrow("startDate"))
                                    val endDate = cursorVolunteering.getString(cursorVolunteering.getColumnIndexOrThrow("endDate"))
                                    val startTime = cursorVolunteering.getString(cursorVolunteering.getColumnIndexOrThrow("startTime"))
                                    val endTime = cursorVolunteering.getString(cursorVolunteering.getColumnIndexOrThrow("endTime"))
                                    val idOng = cursorVolunteering.getInt(cursorVolunteering.getColumnIndexOrThrow("id_ong"))

                                    val volunteering = VolunteeringClass(idVolunteering, title, startDate, endDate, startTime, endTime, idOng)
                                    volunteeringList.add(volunteering)
                                }
                            }
                        }

                        val participationAdapter = ParticipationAdapter(volunteeringList,
                            onDeleteItem = { volunteering -> onDeleteItem(volunteering) }
                        )
                        binding.rvMyParticipations.adapter = participationAdapter
                    }
                }
            } catch (e: Exception) {
                Log.d("Error", "Error: ${e.message}", e)
            }
            binding.rvMyParticipations.addItemDecoration(decoration)
        }
    }

    fun onDeleteItem(volunteering: VolunteeringClass) {
        if(currentUser != null){
            try {
                val result = db?.FireQueryWithRowsAffected("DELETE FROM user_volunteering WHERE id_user = ${currentUser?.id_user} AND id_volunteering = ${volunteering.id_volunteering}")

                if (result != null && result > 0) {
                    Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show()
                    initRecyclerView()
                } else {
                    Toast.makeText(context, "No se pudo eliminar el voluntariado", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.d("Error", "Error: ${e.message}", e)
            }
        }
    }
}