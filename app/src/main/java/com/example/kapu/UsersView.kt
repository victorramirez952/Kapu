package com.example.kapu

import android.content.Intent
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
                        Log.d("Voltorn", "Boolean for ${email} is: ${it.getInt(it.getColumnIndexOrThrow("collaborator")) == 1}")
                        var collaborator = it.getInt(it.getColumnIndexOrThrow("collaborator")) == 1

                        var user = User(id, email, password, firstName, lastName, phone, collaborator)
                        userList.add(user)

                    } while (it.moveToNext())
                    val userAdapter = UserAdapter(userList,
                        onItemSelected = { user -> onItemSelected(user) },
                        onItemEditClicked = { user -> onItemEditClicked(user) },
                        onItemDeleteClicked = { user -> onItemDeleteClicked(user) }
                    )
                    binding.rvUsers.adapter = userAdapter
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


    fun onItemEditClicked(user: User){
        var auxUser = db?.EditUser(user)
        Log.d("Voltorn", "User agarrado: ${auxUser}")
        if(auxUser != null){
            Toast.makeText(this, "Usuario ${user.first_name} ${user.last_name} editado", Toast.LENGTH_SHORT).show()
            initRecyclerView()
        } else {
            Toast.makeText(this, "No se pudo editar los datos", Toast.LENGTH_SHORT).show()
        }
    }

    fun onItemDeleteClicked(user: User){
        var auxUser = db?.DeleteUser(user)
        if(auxUser != null){
            Toast.makeText(this, "Usuario ${user.first_name} ${user.last_name} eliminado", Toast.LENGTH_SHORT).show()
            initRecyclerView()
        } else {
            Toast.makeText(this, "No se pudo eliminar el usuario", Toast.LENGTH_SHORT).show()
        }
    }
}