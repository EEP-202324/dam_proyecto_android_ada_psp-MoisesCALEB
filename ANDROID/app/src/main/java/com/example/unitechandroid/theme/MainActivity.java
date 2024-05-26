package com.example.unitechandroid.theme;

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

// Datos de usuario de prueba
private val usuarioCorrecto = "usuario"
private val contraseñaCorrecta = "contraseña"

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextUsuario = findViewById<EditText>(R.id.editTextUsuario)
        val editTextContraseña = findViewById<EditText>(R.id.editTextContraseña)
        val buttonIniciarSesion = findViewById<Button>(R.id.buttonIniciarSesion)

        buttonIniciarSesion.setOnClickListener {
        val usuario = editTextUsuario.text.toString()
        val contraseña = editTextContraseña.text.toString()

        if (usuario == usuarioCorrecto && contraseña == contraseñaCorrecta) {
        // Aquí podrías iniciar la actividad principal
        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
        } else {
        Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
        }
        }
        }
