package com.example.kapu.adapter

import android.content.DialogInterface.OnClickListener
import android.text.Editable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.kapu.User
import com.example.kapu.databinding.UserLayoutBinding

class UserViewHolder(view: View):RecyclerView.ViewHolder(view) {
    val binding = UserLayoutBinding.bind(view)
    fun render(userModel: User, onClickListener: (User) -> Unit){
        binding.tvUserEmail.setText(userModel.email)
        binding.tvUserPassword.setText(userModel.password)
        binding.tvUserFirstName.setText(userModel.first_name)
        binding.tvUserLastName.setText(userModel.last_name)
        binding.tvUserPhone.setText(userModel.phone)
        // Glide.with(binding.ivUser.context).load(userModel.img_profile).into(binding.ivUser)
        itemView.setOnClickListener{ onClickListener(userModel) }
    }
}