package com.aula.unitechc

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Button

import androidx.compose.material3.TextField
import androidx.compose.material3.Switch
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aula.unitechc.api.ApiService
import com.aula.unitechc.model.Universidad
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val BASE_URL = "http://10.0.2.2:8080/"

@Composable
fun UniversityFormScreen(navController: NavController, university: Universidad?, isEditMode: Boolean) {
    var nombre by remember { mutableStateOf(university?.nombre ?: "") }
    var direccion by remember { mutableStateOf(university?.direccion ?: "") }
    var enlace by remember { mutableStateOf(university?.enlace ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = if (isEditMode) "Editar Universidad" else "Nueva Universidad",
            style = TextStyle(fontSize = 24.sp, lineHeight = 32.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = enlace,
            onValueChange = { enlace = it },
            label = { Text("Enlace") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isEditMode) {
                    // Llamada a la función de actualizar universidad
                    updateUniversity(
                        Universidad(university!!.id, nombre, direccion, enlace)
                    ) { success ->
                        if (success) {
                            navController.popBackStack()
                        } else {
                            // Manejar el error
                        }
                    }
                } else {
                    // Llamada a la función de crear universidad
                    createUniversity(
                        Universidad(id = 0, nombre = nombre, direccion = direccion, enlace = enlace)
                    ) { success ->
                        if (success) {
                            navController.popBackStack()
                        } else {
                            // Manejar el error
                        }
                    }
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = if (isEditMode) "Actualizar" else "Crear")
        }
    }
}


fun updateUniversity(university: Universidad, callback: (Boolean) -> Unit) {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    apiService.updateUniversity(university.id, university).enqueue(object : Callback<Universidad> {
        override fun onResponse(call: Call<Universidad>, response: Response<Universidad>) {
            callback(response.isSuccessful)
        }

        override fun onFailure(call: Call<Universidad>, t: Throwable) {
            callback(false)
        }
    })
}
fun createUniversity(university: Universidad, callback: (Boolean) -> Unit) {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    apiService.createUniversity(university).enqueue(object : Callback<Universidad> {
        override fun onResponse(call: Call<Universidad>, response: Response<Universidad>) {
            callback(response.isSuccessful)
        }

        override fun onFailure(call: Call<Universidad>, t: Throwable) {
            callback(false)
        }
    })
}


