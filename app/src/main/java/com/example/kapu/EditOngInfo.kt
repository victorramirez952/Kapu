package com.example.kapu

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kapu.databinding.FragmentEditOngInfoBinding

class EditOngInfo : Fragment() {
    private lateinit var binding: FragmentEditOngInfoBinding
    private lateinit var sessionManager: SessionManager
    private var db:DB? = null
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
        binding = FragmentEditOngInfoBinding.inflate(inflater, container, false)
        db = DB(requireContext())
        sessionManager = SessionManager(context)
        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Volunteering())
                .addToBackStack(null)
                .commit()
        }
        binding.btnSaveChanges.setOnClickListener {
            editOngInfo()
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Volunteering())
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getOng()
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
                binding.tvNameOng.text = "${currentOng?.name}"
                binding.etTitle.text = Editable.Factory.getInstance().newEditable(currentOng?.name)
                binding.etEmail.text = Editable.Factory.getInstance().newEditable(currentOng?.email)
                binding.etPhone.text = Editable.Factory.getInstance().newEditable(currentOng?.phone ?: "")
                binding.etAddress.text = Editable.Factory.getInstance().newEditable(currentOng?.address)
            }
        } catch (e:Exception){
            Log.d("Voltorn", "Error: ${e.message}")
        }
    }

    private fun editOngInfo() {
        if (currentOng != null) {
            val name = binding.etTitle.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()
            currentOng?.phone = phone
            currentOng?.email = email
            currentOng?.address = address

            try {
                val query = "UPDATE ongs SET name = '$name',  phone = '$phone', email = '$email', address = '$address' WHERE id_ong = ${currentOng?.id_ong}"

                val rowsAffected = db?.FireQueryWithRowsAffected(query)
                if (rowsAffected != -1L) {
                    Toast.makeText(context, "Ong ${currentOng?.name} editada", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.upper_fragment, Donations())
                        .addToBackStack(null)
                        .commit()
                } else {
                    Toast.makeText(context, "Error editando la ong", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.d("Voltorn", "Error: ${e.message}", e)
            }
        }
    }
}