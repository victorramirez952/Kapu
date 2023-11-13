package com.example.kapu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kapu.R
import com.example.kapu.User

class UserAdapter(private val usersList:List<User>,
                  private val onItemSelected: (User) -> Unit,
                  private val onItemEditClicked: (User) -> Unit,
                  private val onItemDeleteClicked: (User) -> Unit
): RecyclerView.Adapter<UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view, onItemEditClicked, onItemDeleteClicked)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = usersList[position]
        holder.render(user, onItemSelected)
    }

    override fun getItemCount(): Int = usersList.size
}