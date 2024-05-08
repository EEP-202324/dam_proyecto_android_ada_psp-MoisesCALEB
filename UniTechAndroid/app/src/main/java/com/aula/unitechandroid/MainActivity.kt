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

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:8080/") // URL base de tu servidor Spring
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun LogInUser(email: String, password: String) {
        // Llama al servicio de inicio de sesión
        val call = apiService.login(email, password)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Inicio de sesión exitoso
                    // Aquí puedes manejar la respuesta exitosa, como iniciar una nueva actividad
                    Toast.makeText(applicationContext, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                } else {
                    // Error en el inicio de sesión
                    // Aquí puedes manejar el error, como mostrar un mensaje de error al usuario
                    Toast.makeText(applicationContext, "Error en el inicio de sesión", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Manejo de errores de red
                // Aquí puedes manejar el caso en que la solicitud falle debido a problemas de red
                Toast.makeText(applicationContext, "Error de red al iniciar sesión", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
