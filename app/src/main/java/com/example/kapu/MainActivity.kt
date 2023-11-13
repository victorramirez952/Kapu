package com.example.kapu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private var db:DB?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = DB(this)
        db?.openDatabase()
        sessionManager = SessionManager(this)
        checkLogin()
        // openHomeActivity()
    }

    private fun openHomeActivity(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun checkLogin(){
        if(sessionManager.isLogin() == false){
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            openHomeActivity()
        }
    }

    override fun onStart() {
        super.onStart()
        if(sessionManager.isLogin() == true){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}