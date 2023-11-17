package com.example.kapu

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kapu.databinding.FragmentDonationsBinding
import com.example.kapu.databinding.FragmentEditOngDescBinding

class EditOngDesc : Fragment() {
    private lateinit var binding: FragmentEditOngDescBinding
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
        // Inflate the layout for this fragment
        binding = FragmentEditOngDescBinding.inflate(inflater, container, false)
        db = DB(requireContext())
        sessionManager = SessionManager(context)
        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Donations())
                .addToBackStack(null)
                .commit()
        }
        binding.btnSaveChanges.setOnClickListener {
            editOngDesc()
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Donations())
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
                binding.tvNameOng.text = currentOng?.name
                binding.etDescription.text = Editable.Factory.getInstance().newEditable(currentOng?.description ?: "")
            }
        } catch (e:Exception){
            Log.d("Voltorn", "Error: ${e.message}")
        }
    }

    private fun editOngDesc() {
        if (currentOng != null) {
            val description = binding.etDescription.text.toString().trim()
            currentOng?.description = description

            try {
                val query = "UPDATE ongs SET description = '$description' WHERE id_ong = ${currentOng?.id_ong}"
                val rowsAffected = db?.FireQueryWithRowsAffected(query)

                if (rowsAffected != -1L) {
                    Toast.makeText(context, "Ong ${currentOng?.name} editada", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.upper_fragment, Donations())
                        .addToBackStack(null)
                        .commit()
                } else {
                    Toast.makeText(context, "Error editando la descripcion de la ong", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.d("Voltorn", "Error: ${e.message}", e)
            }
        }
    }
}