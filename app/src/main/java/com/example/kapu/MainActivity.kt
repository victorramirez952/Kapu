package com.example.kapu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var db:DB?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = DB(this)
        db?.openDatabase()
        openLogin()
        // openHomeActivity()
    }

    private fun openLogin(){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun openHomeActivity(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}