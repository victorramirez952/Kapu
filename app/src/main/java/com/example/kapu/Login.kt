package com.example.kapu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.kapu.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var email: String
    private lateinit var password: String
    private var db:DB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DB(this)

        binding.btnLogin.setOnClickListener {
            email = binding.etLoginEmail.text.toString().trim()
            password = binding.etLoginPassword.text.toString().trim()
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Ingresa nombre y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("Voltorn", "Validando usuario")
                val user = db?.CheckUser(email, password)
                if(user != null){
                    Toast.makeText(this, "Bienvenido ${user.first_name} ${user.last_name}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else{
                    Log.d("Voltorn", "Hubo un error")
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnOmega.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
}