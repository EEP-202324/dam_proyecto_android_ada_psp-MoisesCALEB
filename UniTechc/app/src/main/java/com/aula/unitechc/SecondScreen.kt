import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Switch

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aula.unitechc.Screen
import com.aula.unitechc.api.ApiService
import com.aula.unitechc.model.Universidad
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://10.0.2.2:8080/"
suspend fun getUniversities(navController: NavController, callback: (List<Universidad>?, Boolean) -> Unit) {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    // Realizar una llamada al método del API para obtener la lista de universidades
    apiService.getUniversidades().enqueue(object : Callback<List<Universidad>> {
        override fun onResponse(call: Call<List<Universidad>>, response: Response<List<Universidad>>) {
            if (response.isSuccessful) {
                val universities = response.body()
                if (universities != null) {
                    // Lista de universidades obtenida con éxito
                    callback(universities, true)
                } else {
                    // La respuesta fue exitosa pero la lista de universidades es nula
                    callback(null, false)
                }
            } else {
                // Error en la solicitud
                callback(null, false)
            }
        }

        override fun onFailure(call: Call<List<Universidad>>, t: Throwable) {
            // Error de red al realizar la solicitud
            callback(null, false)
        }
    })
}

fun deleteUniversityById(universityId: Long, callback: (Boolean) -> Unit) {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    apiService.deleteUniversity(universityId).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                callback(true)
            } else {
                callback(false)
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            callback(false)
        }
    })
}
@Composable
fun UniversityList(
    universities: List<Universidad>,
    onAssociationToggle: (Universidad, Boolean) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        universities.forEach { university ->
            UniversityCard(
                university = university,
                onEditClick = { /* Acción para modificar */ },
                onDeleteClick = { /* Acción para borrar */ },
                onAssociationToggle = { isChecked ->
                    onAssociationToggle(university, isChecked)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SecondScreen(navController: NavController) {
    var universities by remember { mutableStateOf<List<Universidad>>(emptyList()) }

    LaunchedEffect(key1 = true) {
        // Llamar a la función para obtener las universidades cuando se cargue la pantalla
        getUniversities(navController = navController) { receivedUniversities, success ->
            if (success) {
                universities = receivedUniversities ?: emptyList()
            } else {
                // Manejar el caso de error, como mostrar un mensaje de error
            }
        }
    }
    val h5 = TextStyle(
        fontSize = 20.sp,
        lineHeight = 28.sp
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Universidades",
            style = h5,
            modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp, top = 32.dp)
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(universities) { university ->
                UniversityCard(
                    university = university,
                    onEditClick = {
                        navController.navigate(
                            Screen.UniversityForm.route + "?id=${university.id}&nombre=${university.nombre}&direccion=${university.direccion}&enlace=${university.enlace}"
                        )
                    },
                    onDeleteClick = {
                            deleteUniversityById(university.id) { success ->
                                if (success) {
                                    universities = universities.filter { it.id != university.id }
                                } else {
                                    // Mostrar mensaje de error
                                }
                            }
                    },
                    onAssociationToggle = { isChecked ->
                        if (isChecked) {
                            // Acción para el Switch activado
                            // Por ejemplo, realizar una llamada POST para asociar la universidad
                        } else {
                            // Acción para el Switch desactivado
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        // Botón redondeado en la esquina inferior derecha
        FloatingActionButton(
            onClick = {
                // Acción al hacer clic en el botón
                // Por ejemplo, mostrar el formulario de la universidad
                navController.navigate(Screen.UniversityForm.route)
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End)
        ) {
            Text(text = "+", style = TextStyle(fontSize = 24.sp))
        }
    }
}

    @Composable
    fun UniversityCard(
        university: Universidad,
        onEditClick: (Universidad) -> Unit,
        onDeleteClick: () -> Unit,
        onAssociationToggle: (Boolean) -> Unit,
    ) {
        var isSelected by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isSelected = !isSelected },
            elevation = if (isSelected) 8.dp else 4.dp,
            backgroundColor = if (isSelected) Color.LightGray else Color.White
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = university.nombre, style = TextStyle(fontSize = 20.sp))
                Text(text = university.direccion, style = TextStyle(fontSize = 16.sp))

                ClickableText(
                    text = buildAnnotatedString {
                        append("Sitio web")
                        addStringAnnotation("URL", university.enlace, 0, 9)
                    },
                    onClick = { offset ->
                        val url = university.enlace
                        // Implementa la lógica para redirigir a la página web utilizando el NavController
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { onEditClick(university) }) {
                        Text("Modificar")
                    }
                    Button(onClick = onDeleteClick) {
                        Text("Borrar")
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    // Implementa el switch si es necesario
                }
            }
        }
    }

