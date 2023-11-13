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
        if(currentUser?.collaborator == false){
            binding.tvMyParticipations.visibility = View.VISIBLE
            binding.rvMyParticipations.visibility = View.VISIBLE
        } else {
            binding.tvMyParticipations.visibility = View.GONE
            binding.rvMyParticipations.visibility = View.GONE
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
                var currentUser = db?.GetUser(sessionManager.getUserEmail())
                if(currentUser != null){
                    val fullName = currentUser?.first_name + " " + currentUser?.last_name
                    binding.tvUsername.text = fullName
                    binding.etEmail.text = Editable.Factory.getInstance().newEditable(currentUser.email)
                    binding.etFirstName.text = Editable.Factory.getInstance().newEditable(currentUser.first_name)
                    binding.etLastName.text = Editable.Factory.getInstance().newEditable(currentUser.last_name)
                    binding.etPhone.text = Editable.Factory.getInstance().newEditable(currentUser.phone ?: "")
                    binding.etPassword.text = Editable.Factory.getInstance().newEditable(currentUser.password)
                } else{
                    Toast.makeText(context, "Hubo un error en la busqueda de informacion", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e:Exception){
            Log.d("Voltorn", "Error: ${e.message}")
        }
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            UserProfile().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}