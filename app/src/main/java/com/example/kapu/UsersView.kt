package com.example.kapu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kapu.adapter.UserAdapter
import com.example.kapu.databinding.ActivityUsersViewBinding

class UsersView : AppCompatActivity() {
    private lateinit var binding: ActivityUsersViewBinding
    private var db:DB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_view)
        binding = ActivityUsersViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DB(this)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        val manager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(this, manager.orientation)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        try {
            val query = "SELECT * FROM users"
            db?.FireQuery(query)?.use {
                if (it.count > 0) {
                    val userList = mutableListOf<User>()
                    do {
                        var id = it.getInt(it.getColumnIndexOrThrow("id_user"))
                        var email = it.getString(it.getColumnIndexOrThrow("email"))
                        var password = it.getString(it.getColumnIndexOrThrow("password"))
                        var firstName = it.getString(it.getColumnIndexOrThrow("first_name"))
                        var lastName = it.getString(it.getColumnIndexOrThrow("last_name"))
                        var phone = it.getString(it.getColumnIndexOrThrow("phone"))
                        var collaborator = it.getInt(it.getColumnIndexOrThrow("collaborator")) == 1

                        var user = User(id, email, password, firstName, lastName, phone, collaborator)
                        userList.add(user)

                    } while (it.moveToNext())
                    binding.rvUsers.adapter = UserAdapter(userList) { user ->
                        onItemSelected(user)
                    }
                }
            }
        } catch (e:Exception){
            Log.d("Error", "Error: ${e.message}", e)
        }
        binding.rvUsers.addItemDecoration(decoration)
    }

    fun onItemSelected(user: User){
        Toast.makeText(this, user.first_name + " " + user.last_name, Toast.LENGTH_SHORT).show()
    }
}