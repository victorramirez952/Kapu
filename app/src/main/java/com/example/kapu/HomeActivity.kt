package com.example.kapu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.kapu.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sessionManager: SessionManager
    private var db:DB? = null
    private var currentUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DB(this)
        sessionManager = SessionManager(this)
        checkLogin()
        if(savedInstanceState == null){
            if(currentUser?.collaborator == false){
                supportFragmentManager.beginTransaction()
                    .add(R.id.upper_fragment, Map())
                    .add(R.id.lower_fragment, Menu())
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.upper_fragment, Map())
                    .add(R.id.lower_fragment, Menu())
                    .commit()
            }

        }

    }

    override fun onStart() {
        super.onStart()
        checkLogin()
    }

    private fun checkLogin(){
        if(sessionManager.isLogin() == false){
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            currentUser = db?.GetUser(sessionManager.getUserEmail())
            if(currentUser != null){
                val fullName = currentUser?.first_name + " " + currentUser?.last_name
            } else{
                Toast.makeText(this, "Hubo un error en la búsqueda de informacion", Toast.LENGTH_SHORT).show()
            }
        }
    }



 /*   private fun getData(){
        try {
            val sqlQuery = "SELECT email FROM users"
            db?.FireQuery(sqlQuery)?.use {
                if(it.count > 0){
                    do {
                        val col = it.getColumnIndex("email")
                        val values = it.getString(col)
                        Log.i("Omega", "User email: $values")
                    } while (it.moveToNext())
                }

            }
        } catch (e:Exception){
            e.printStackTrace()
        }
    }*/
}