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
        Toast.makeText(this, "Omega", Toast.LENGTH_SHORT).show()
        Log.i("Voltorn", "Antes de DB(this)")
        db = DB(this)
        Log.i("Voltorn", "db.openDatabase()")
        db?.openDatabase()
        Log.i("Voltorn", "Finalizado")
        openHomeActivity()
    }

    private fun openHomeActivity(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}