package com.example.kapu

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kapu.databinding.FragmentAddDonationBinding
import com.example.kapu.databinding.FragmentEditOngDescBinding
import java.io.ByteArrayOutputStream

class AddDonation : Fragment() {
    private lateinit var binding: FragmentAddDonationBinding
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
        binding = FragmentAddDonationBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(context)
        db = DB(requireContext())
        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Donations())
                .addToBackStack(null)
                .commit()
        }
        binding.btnAdd.setOnClickListener {
            addDonation()
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Donations())
                .addToBackStack(null)
                .commit()
        }
        binding.ivDonation.setOnClickListener{
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getOng()
    }

    private fun getOng() {
        try {
            currentOng = db?.GetOng(sessionManager.getOngId())
            if (currentOng == null) {
                Toast.makeText(
                    context,
                    "Un error en informacion buscada, parece haber",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Log.d("Voltorn", "Error: ${e.message}")
        }
    }

    private fun addDonation(){
        if(currentOng != null){
            val title = binding.etDonation.text.toString().trim()
            if(title.isEmpty()){
                Toast.makeText(context, "Llene todos los campos", Toast.LENGTH_SHORT).show()
                return
            }
            val bitmap = (binding.ivDonation.drawable as? BitmapDrawable)?.bitmap
            val stream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val imageInByte = stream.toByteArray()
            val donation = Donation(0, title, imageInByte, currentOng!!.id_ong)
            try {
                val auxDonation = db?.AddDonation(donation)
                if(auxDonation != null) {
                    Toast.makeText(context, "Donativo: ${auxDonation.title} agregado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "No se pudo editar los datos", Toast.LENGTH_SHORT).show()
                }
            } catch (e:Exception){
                Log.d("Voltorn", "Error: ${e.message}", e)
            }
        }
    }

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                binding.ivDonation.setImageURI(imgUri)

                // Convert the image to a byte array (BLOB)
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imgUri)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }
    }
}