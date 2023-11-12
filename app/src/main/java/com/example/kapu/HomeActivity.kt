package com.example.kapu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class HomeActivity : AppCompatActivity() {
    private var db:DB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        db = DB(this)
//        getData()
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