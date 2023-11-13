package com.example.kapu

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kapu.databinding.FragmentMapBinding
import com.example.kapu.databinding.FragmentMenuBinding

class Menu : Fragment() {
    private lateinit var binding: FragmentMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        binding.btnDonations.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Donations())
                .addToBackStack(null)
                .commit()
        }
        binding.btnVolunteering.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Volunteering())
                .addToBackStack(null)
                .commit()
        }
        binding.btnProfile.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, UserProfile())
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

//    companion object {
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            Menu().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}