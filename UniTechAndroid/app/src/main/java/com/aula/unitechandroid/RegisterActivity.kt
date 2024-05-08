package com.aula.unitechandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RegisterActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:8080/") // URL base de tu servidor Spring
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        usernameEditText = findViewById(R.id.editTextUsername)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        registerButton = findViewById(R.id.buttonRegister)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            registerUser(username, email, password)
        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        // Llama al servicio de registro
        val call = apiService.register(username, email, password)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Registro exitoso
                    Toast.makeText(applicationContext, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    // Puedes agregar aquí la navegación a otra actividad si lo deseas
                } else {
                    // Error en el registro
                    Toast.makeText(applicationContext, "Error en el registro", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Manejo de errores de red
                Toast.makeText(applicationContext, "Error de red al registrar", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


