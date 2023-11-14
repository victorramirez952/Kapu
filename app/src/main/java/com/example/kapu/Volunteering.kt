package com.example.kapu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            if(currentOng == null) {
                Toast.makeText(
                    context,
                    "Un error en informacion buscada, parece haber",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.tvNameOng.text = currentOng?.name
            }
        } catch (e:Exception){
            Log.d("Voltorn", "Error: ${e.message}")
        }
    }
}