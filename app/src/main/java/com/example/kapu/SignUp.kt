package com.example.kapu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.kapu.databinding.ActivityLoginBinding
import com.example.kapu.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var first_name: String
    private lateinit var last_name: String
    private lateinit var phone: String
    private var db:DB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DB(this)
        sessionManager = SessionManager(this)

        binding.btnSignup.setOnClickListener {
            email = binding.etSignupEmail.text.toString().trim()
            password = binding.etSignupPassword.text.toString().trim()
            first_name = binding.etSignupFirstName.text.toString().trim()
            last_name = binding.etSignupLastName.text.toString().trim()
            phone = binding.etSignupPhone.text.toString().trim()
            if(email.isEmpty() || password.isEmpty() || first_name.isEmpty() || last_name.isEmpty() || phone.isEmpty() || !phone.matches("\\d+".toRegex())){
                Toast.makeText(this, "Ingresa correctamente los datos", Toast.LENGTH_SHORT).show()
            } else {
                val user = db?.InsertUser(email, password, first_name, last_name, phone)
                if(user != null){
                    Toast.makeText(this, "Acabas de registrarte ${user.first_name} ${user.last_name}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                } else{
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnToLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        if(sessionManager.isLogin() == true){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}