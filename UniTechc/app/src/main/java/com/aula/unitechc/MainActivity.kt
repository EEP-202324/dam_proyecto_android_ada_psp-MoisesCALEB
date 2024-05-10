package com.aula.unitechc

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Button

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aula.unitechc.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.aula.unitechc.model.User

private const val BASE_URL = "http://10.0.2.2:8080/"

class MainActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colors.background) {
                LoginScreen(apiService = apiService)
            }
        }
    }
}

@Composable
fun LoginScreen(apiService: ApiService) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }
    var users by remember { mutableStateOf<List<User>>(emptyList()) } // Lista de usuarios

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            label = { Text("Username") }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { /* Handle login here */ })
        )

        // Campo de correo electrónico solo visible en modo de registro
        if (isRegistering) {
            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                label = { Text("Email") }
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón de inicio de sesión
            Button(onClick = {
                // Aquí podrías llamar al método de tu ApiService para obtener los usuarios
                getUsersFromServer(apiService) { userList ->
                    users = userList
                }
            }) {
                Text("Login")
            }

            val context= LocalContext.current
            // Botón de registro
            Button(onClick = {

                // Cambiar al modo de registro o realizar el registro según el estado actual
                if (isRegistering) {
                    performRegistration(username, email, password, context) { success ->
                        if (success) {
                            // Registro exitoso, realizar alguna acción adicional si es necesario
                        } else {
                            // Error en el registro, manejar según corresponda
                        }
                    }

                } else {
                    isRegistering = true
                }
            }) {
                Text(if (isRegistering) "Register" else "Enter Email")
            }
        }

        // Mostrar usuarios debajo de los botones
        users.forEach { user ->
            Text(text = user.username) // Mostrar el nombre de usuario
        }
    }
}

private fun getUsersFromServer(apiService: ApiService, callback: (List<User>) -> Unit) {
    // Llamar al método correspondiente en tu ApiService para obtener los usuarios
    apiService.getUsers().enqueue(object : Callback<List<User>> {
        override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
            if (response.isSuccessful) {
                val userList = response.body() ?: emptyList()
                callback(userList)
            } else {
                // Manejar el caso en que la solicitud no sea exitosa
            }
        }
        override fun onFailure(call: Call<List<User>>, t: Throwable) {
            // Manejar el caso en que haya un fallo de red
            t.printStackTrace()
        }
    })
}
fun performRegistration(username: String, email: String, password: String, context: Context, callback: (Boolean) -> Unit) {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    val user = User(username, email, password)
    // Realizar la solicitud de registro de manera asíncrona
    apiService.registerUser(user).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                // Registro exitoso
                showToast(context, "Registro exitoso")
                callback(true)
            } else {
                // Error en el registro
                showToast(context, "Error en el registro")
                callback(false)
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            // Error de red al realizar la solicitud
            showToast(context, "Error de red al registrar")
            t.printStackTrace()
            callback(false)
        }
    })
}


private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}

