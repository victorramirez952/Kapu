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
import com.example.kapu.databinding.FragmentDeleteAccountBinding
import com.example.kapu.databinding.FragmentEditProfileBinding

class DeleteAccount : Fragment() {
    private lateinit var binding: FragmentDeleteAccountBinding
    private lateinit var sessionManager: SessionManager
    private var db:DB? = null
    private var currentUser: User? = null
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
        binding = FragmentDeleteAccountBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(context)
        db = DB(requireContext())
        checkLogin()
        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, EditProfile())
                .addToBackStack(null)
                .commit()
        }
        binding.btnDeleteAccount.setOnClickListener {
            var auxUser = db?.DeleteUser(currentUser!!)
            if(auxUser != null){
                try {
                    sessionManager.removeData()
                    Toast.makeText(context, "Usuario ${currentUser?.first_name} ${currentUser?.last_name}   eliminado", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, Login::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } catch (e:Exception){
                    Log.d("Voltorn", "Error: ${e.message}")
                }
            } else {
                Toast.makeText(context, "No se pudo eliminar el usuario", Toast.LENGTH_SHORT).show()
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
                } else{
                    Toast.makeText(context, "Hubo un error en la busqueda de informacion", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e:Exception){
            Log.d("Voltorn", "Error: ${e.message}")
        }
    }
}