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
import com.example.kapu.databinding.FragmentDonationsBinding
import com.example.kapu.databinding.FragmentMapBinding

class Donations : Fragment() {
    private lateinit var binding: FragmentDonationsBinding
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
        // Inflate the layout for this fragment
        binding = FragmentDonationsBinding.inflate(inflater, container, false)
        db = DB(requireContext())
        sessionManager = SessionManager(context)
        checkLogin()
        binding.btnGo2Map.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Map())
                .addToBackStack(null)
                .commit()
        }
        if(currentUser?.collaborator == false){
            binding.btnEditOng.visibility = View.GONE
            binding.btnAddDonation.visibility = View.GONE
        } else {
            binding.btnEditOng.visibility = View.VISIBLE
            binding.btnEditOng.setOnClickListener{
                parentFragmentManager.beginTransaction()
                    .replace(R.id.upper_fragment, EditOngDesc())
                    .addToBackStack(null)
                    .commit()
            }
            binding.btnAddDonation.setOnClickListener{
                parentFragmentManager.beginTransaction()
                    .replace(R.id.upper_fragment, AddDonation())
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

//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            Donations().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}