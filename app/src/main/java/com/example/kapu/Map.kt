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
import com.example.kapu.adapter.UserAdapter
import com.example.kapu.databinding.FragmentMapBinding

class Map : Fragment() {
    private lateinit var binding: FragmentMapBinding
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
        initRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(context)
        db = DB(requireContext())
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
        binding.btnHomeGoUsers.setOnClickListener {
            val intent = Intent(context, UsersView::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    private fun checkLogin(){
        try{
            if(sessionManager.isLogin() == false){
                val intent = Intent(context, Login::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                currentUser = db?.GetUser(sessionManager.getUserEmail())
                if(currentUser != null){
                    val fullName = currentUser?.first_name + " " + currentUser?.last_name
                    binding.tvHomeTesting.text = "Welcome, $fullName"
                } else{
                    Toast.makeText(context, "Hubo un error en la busqueda de informacion", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e:Exception){
            Log.d("Voltorn", "Error: ${e.message}")
        }

    }

    private fun initRecyclerView() {
        binding.rvOngs.adapter = null
        if (currentUser != null) {
            val manager = LinearLayoutManager(requireContext())
            val decoration = DividerItemDecoration(requireContext(), manager.orientation)
            binding.rvOngs.layoutManager = LinearLayoutManager(requireContext())

            try {
                val query = if (currentUser?.collaborator == true) {
                    "SELECT * FROM ongs WHERE id_user = ${currentUser?.id_user}"
                } else {
                    "SELECT * FROM ongs"
                }
                db?.FireQuery(query)?.use {
                    if (it.count > 0) {
                        val ongList = mutableListOf<Ong>()
                        do {
                            var id_ong = it.getInt(it.getColumnIndexOrThrow("id_ong"))
                            var name = it.getString(it.getColumnIndexOrThrow("name"))
                            var description = it.getString(it.getColumnIndexOrThrow("description"))
                            var address = it.getString(it.getColumnIndexOrThrow("address"))
                            var phone = it.getString(it.getColumnIndexOrThrow("phone"))
                            var email = it.getString(it.getColumnIndexOrThrow("email"))
                            var id_user = it.getInt(it.getColumnIndexOrThrow("id_user"))

                            var ong = Ong(id_ong, name, description, address, phone, email, id_user)
                            ongList.add(ong)
                        } while (it.moveToNext())

                        val ongAdapter = OngAdapter(
                            ongList,
                            onItemSelected = { ong -> onItemSelected(ong) }
                        )
                        binding.rvOngs.adapter = ongAdapter
                    }
                }
            } catch (e: Exception) {
                Log.d("Voltorn", "Error: ${e.message}", e)
            }

            binding.rvOngs.addItemDecoration(decoration)
        }
    }

    fun onItemSelected(ong: Ong){
        sessionManager.setOng(ong.id_ong)
        parentFragmentManager.beginTransaction()
            .replace(R.id.upper_fragment, Donations())
            .addToBackStack(null)
            .commit()
    }
}