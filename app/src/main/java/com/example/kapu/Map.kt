package com.example.kapu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kapu.databinding.FragmentMapBinding

class Map : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var sessionManager: SessionManager
    private var db:DB? = null
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
        binding = FragmentMapBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(context)
        db = DB(requireContext())
        // val view = inflater.inflate(R.layout.fragment_map, container, false)
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
        if(sessionManager.isLogin() == false){
            val intent = Intent(context, Login::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else {
            var currentUser = db?.GetUser(sessionManager.getUserEmail())
            if(currentUser != null){
                val fullName = currentUser?.first_name + " " + currentUser?.last_name
                binding.tvHomeTesting.text = "Welcome, $fullName"
            } else{
                Toast.makeText(context, "Hubo un error en la busqueda de informacion", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    companion object {
//        fun newInstance(param1: String, param2: String) =
//            Map().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}