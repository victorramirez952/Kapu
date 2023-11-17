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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kapu.adapter.DonationAdapter
import com.example.kapu.adapter.OngAdapter
import com.example.kapu.databinding.FragmentDonationsBinding
import com.example.kapu.databinding.FragmentMapBinding

class Donations : Fragment() {
    private lateinit var binding: FragmentDonationsBinding
    private lateinit var sessionManager: SessionManager
    private var db: DB? = null
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
        if (currentUser?.collaborator == false) {
            binding.btnEditOng.visibility = View.GONE
            binding.btnAddDonation.visibility = View.GONE
        } else {
            binding.btnEditOng.visibility = View.VISIBLE
            binding.btnEditOng.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.upper_fragment, EditOngDesc())
                    .addToBackStack(null)
                    .commit()
            }
            binding.btnAddDonation.setOnClickListener {
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
        initRecyclerView()
    }

    private fun checkLogin() {
        try {
            if (sessionManager.isLogin() == false) {
                val intent = Intent(context, Login::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                currentUser = db?.GetUser(sessionManager.getUserEmail())
                if (currentUser == null) {
                    Toast.makeText(
                        context,
                        "Un error en informacion buscada, parece haber",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            Log.d("Voltorn", "Error: ${e.message}")
        }
    }

    private fun initRecyclerView() {
        if(currentOng != null){
            val manager = LinearLayoutManager(requireContext())
            val decoration = DividerItemDecoration(requireContext(), manager.orientation)
            binding.rvDonations.layoutManager = LinearLayoutManager(requireContext())
            try {
                val query = "SELECT * FROM donations WHERE id_ong=${currentOng?.id_ong}"
                db?.FireQuery(query)?.use {
                    if (it.count > 0) {
                        val donationList = mutableListOf<Donation>()
                        do {
                            var id_donation = it.getInt(it.getColumnIndexOrThrow("id_donation"))
                            var title = it.getString(it.getColumnIndexOrThrow("title"))
                            // var imagen = it.getString(it.getColumnIndexOrThrow("image"))
                            var id_ong = it.getInt(it.getColumnIndexOrThrow("id_ong"))

                            var donation = Donation(id_donation, title, id_ong)
                            donationList.add(donation)

                        } while (it.moveToNext())
                        val donationAdapter = DonationAdapter(donationList,
                            currentUser,
                            onItemSelected = { donation -> onItemSelected(donation) },
                            onEditItem = { donation -> onEditItem(donation) },
                            onDeleteItem = { donation -> onDeleteItem(donation) }
                        )
                        Log.d("Voltorn", "Exito when donationAdapter")
                        binding.rvDonations.adapter = donationAdapter
                    }
                }
            } catch (e: Exception) {
                Log.d("Error", "Error: ${e.message}", e)
            }
            binding.rvDonations.addItemDecoration(decoration)
        }
    }

    private fun getOng() {
        try {
            currentOng = db?.GetOng(sessionManager.getOngId())
            if (currentOng != null) {
                binding.tvNameOng.text = currentOng?.name
                binding.tvDescription.text = currentOng?.description
            }
        } catch (e: Exception) {
            Log.d("Voltorn", "Error: ${e.message}")
        }
    }

    fun onItemSelected(donation: Donation) {
        Toast.makeText(requireContext(), donation.title, Toast.LENGTH_SHORT).show()
    }

    fun onEditItem(donation: Donation) {
        val editDonationFragment = EditDonation()
        val bundle = Bundle()
        bundle.putInt("id_donation", donation.id_donation)
        editDonationFragment.arguments = bundle
        Log.d("Voltorn", "Editing: ${donation.title}")
        parentFragmentManager.beginTransaction()
            .replace(R.id.upper_fragment, editDonationFragment)
            .addToBackStack(null)
            .commit()
    }

    fun onDeleteItem(donation: Donation) {
        var result = db?.DeleteDonation(donation)
        if(result != null){
            Toast.makeText(context, "Volunteering ${donation.title} eliminado", Toast.LENGTH_SHORT).show()
            initRecyclerView()
        } else {
            Toast.makeText(context, "No se pudo eliminar el usuario", Toast.LENGTH_SHORT).show()
        }
    }
}