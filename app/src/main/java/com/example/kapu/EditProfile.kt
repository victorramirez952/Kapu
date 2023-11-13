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
import com.example.kapu.databinding.FragmentEditProfileBinding
import com.example.kapu.databinding.FragmentUserProfileBinding

class EditProfile : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
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
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(context)
        db = DB(requireContext())
        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, UserProfile())
                .addToBackStack(null)
                .commit()
        }
        binding.btnSaveChanges.setOnClickListener {
            Toast.makeText(context, "Testing: Has editado tu cuenta", Toast.LENGTH_SHORT).show()
        }
        binding.btnDeleteAccount.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, DeleteAccount())
                .addToBackStack(null)
                .commit()
        }
        checkLogin()
        Log.d("Voltorn", "User logged: ${currentUser}")
        if(currentUser?.collaborator == true){
            binding.llAskForCollaborator.visibility = View.GONE
            binding.tvAskForCollaborator.visibility = View.GONE
            binding.btnAskForCollaborator.visibility = View.GONE
        } else {
            binding.llAskForCollaborator.visibility = View.VISIBLE
            binding.tvAskForCollaborator.visibility = View.VISIBLE
            binding.btnAskForCollaborator.visibility = View.VISIBLE
            binding.btnAskForCollaborator.setOnClickListener {
                Toast.makeText(context, "Testing: Has solicitado ser colaborador", Toast.LENGTH_SHORT).show()
            }
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
                    binding.etEmail.text = Editable.Factory.getInstance().newEditable(currentUser?.email)
                    binding.etFirstName.text = Editable.Factory.getInstance().newEditable(currentUser?.first_name)
                    binding.etLastName.text = Editable.Factory.getInstance().newEditable(currentUser?.last_name)
                    binding.etPhone.text = Editable.Factory.getInstance().newEditable(currentUser?.phone ?: "")
                    binding.etPassword.text = Editable.Factory.getInstance().newEditable(currentUser?.password)
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
//            EditProfile().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}