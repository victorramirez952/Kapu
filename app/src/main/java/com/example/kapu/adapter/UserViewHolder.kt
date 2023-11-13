package com.example.kapu.adapter

import android.content.DialogInterface.OnClickListener
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kapu.User
import com.example.kapu.databinding.UserLayoutBinding

class UserViewHolder(
    private val view: View,
    private val onItemEditClicked: (User) -> Unit,
    private val onItemDeleteClicked: (User) -> Unit
):RecyclerView.ViewHolder(view) {
    val binding = UserLayoutBinding.bind(view)
    fun render(userModel: User, onClickListener: (User) -> Unit){
        binding.tvUserEmail.setText(userModel.email)
        binding.tvUserPassword.setText(userModel.password)
        binding.tvUserFirstName.setText(userModel.first_name)
        binding.tvUserLastName.setText(userModel.last_name)
        binding.tvUserPhone.setText(userModel.phone)
        binding.cbCollaborator.isChecked = userModel.collaborator
        // Glide.with(binding.ivUser.context).load(userModel.img_profile).into(binding.ivUser)
        // itemView.setOnClickListener{ onClickListener(userModel) }

        binding.btnEdit.setOnClickListener {
            val email = binding.tvUserEmail.text.toString().trim()
            val password = binding.tvUserPassword.text.toString().trim()
            val first_name = binding.tvUserFirstName.text.toString().trim()
            val last_name = binding.tvUserLastName.text.toString().trim()
            val phone = binding.tvUserPhone.text.toString().trim()
            val collaborator = binding.cbCollaborator.isChecked
            if(email.isEmpty() || password.isEmpty() || first_name.isEmpty() || last_name.isEmpty() || phone.isEmpty()){
                Toast.makeText(view.context, "Ingresa correctamente los datos", Toast.LENGTH_SHORT).show()
            } else {
                var user = User(userModel.id_user, email, password, first_name, last_name, phone, collaborator)
                onItemEditClicked(user)
            }
        }

        binding.btnDelete.setOnClickListener {
            onItemDeleteClicked(userModel)
        }
    }
}