package com.example.kapu

import android.os.Bundle
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
        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.upper_fragment, EditProfile())
                .addToBackStack(null)
                .commit()
        }
        binding.btnDeleteAccount.setOnClickListener {
            Toast.makeText(context, "Testing: Has eliminado tu cuenta", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            DeleteAccount().apply {
//                arguments = Bundle().apply {
//
//                }
//            }
//    }
}