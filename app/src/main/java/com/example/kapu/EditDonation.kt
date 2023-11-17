package com.example.kapu

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.bumptech.glide.Glide
import com.example.kapu.adapter.VolunteeringAdapter
import com.example.kapu.databinding.FragmentDonationsBinding
import com.example.kapu.databinding.FragmentEditDonationBinding
import com.example.kapu.databinding.FragmentEditOngDescBinding
import java.io.ByteArrayOutputStream

class EditDonation : Fragment() {
    private lateinit var binding: FragmentEditDonationBinding
    private lateinit var sessionManager: SessionManager
    private var db:DB? = null
    private lateinit var currentDonation: Donation
    private var current_id_donation: Int = 0
    private var isImageLoaded = false
    override fun onCreate(savedInstanceState: Bundle?) {1
        super.onCreate(savedInstanceState)
        arguments?.let {
            current_id_donation = it.getInt("id_donation", 0)
            getDonation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditDonationBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(context)
        db = DB(requireContext())
        getDonation()
        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Donations())
                .addToBackStack(null)
                .commit()
        }
        binding.btnSaveChanges.setOnClickListener {
            editDonation()
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Donations())
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    private fun getDonation(){
        if(current_id_donation != 0){
            try {
                val auxCurrentDonation = db?.GetDonation(current_id_donation)
                if(auxCurrentDonation != null) {
                    currentDonation = Donation(auxCurrentDonation.id_donation, auxCurrentDonation.title, null, auxCurrentDonation.id_ong)
                    binding.etDonation.text = Editable.Factory.getInstance().newEditable(currentDonation?.title)
                    if (!isImageLoaded) {
                        // Load the image only if it has not been loaded before
                        currentDonation?.image?.let { imgByteArray ->
                            val bitmap = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.size)
                            binding.ivDonation.setImageBitmap(bitmap)
                        }
                        isImageLoaded = true
                    }
                    binding.ivDonation.setOnClickListener{
                        val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                        changeImage.launch(pickImg)
                    }
                }
            } catch (e:Exception){
                Log.d("Voltorn", "Error: ${e.message}", e)
            }
        }
    }

    private fun editDonation(){
        if(currentDonation != null && current_id_donation != 0){
            val title = binding.etDonation.text.toString().trim()
            currentDonation.title = title
            if(title.isEmpty()){
                Toast.makeText(context, "Llene todos los campos", Toast.LENGTH_SHORT).show()
                return
            }
            try {
                val auxDonation = db?.EditDonation(currentDonation)
                if(auxDonation != null) {
                    Toast.makeText(context, "Donativo: ${auxDonation.title} editado", Toast.LENGTH_SHORT).show()
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
                val imageInByte = stream.toByteArray()

                // Assign the byte array to the img_profile property of currentUser
                currentDonation?.image = imageInByte
            }
        }
}