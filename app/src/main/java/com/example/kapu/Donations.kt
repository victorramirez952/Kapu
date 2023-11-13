package com.example.kapu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kapu.databinding.FragmentDonationsBinding
import com.example.kapu.databinding.FragmentMapBinding

class Donations : Fragment() {
    private lateinit var binding: FragmentDonationsBinding
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
        binding.btnGo2Map.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, Map())
                .addToBackStack(null)
                .commit()
        }
        return binding.root
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