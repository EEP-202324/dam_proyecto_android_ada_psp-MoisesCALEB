
package com.aula.unitechc

import SecondScreen
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text

import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aula.unitechc.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.aula.unitechc.model.User
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.aula.unitechc.model.Universidad


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
                val navController = rememberNavController()
                AppNavigation(navController = navController)

        }
    }
}
sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Second : Screen("second_screen")

    object UniversityForm : Screen("university_form_screen")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    // Configurar la navegación en la aplicación
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            // Pantalla de inicio de sesión
            LoginScreen(navController = navController)
        }
        composable(Screen.Second.route) {
            // Segunda pantalla
            SecondScreen(navController = navController)
        }
        composable(
            route = Screen.UniversityForm.route + "?id={id}&nombre={nombre}&direccion={direccion}&enlace={enlace}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType; defaultValue = -1L },
                navArgument("nombre") { type = NavType.StringType; defaultValue = "" },
                navArgument("direccion") { type = NavType.StringType; defaultValue = "" },
                navArgument("enlace") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id")
            val nombre = backStackEntry.arguments?.getString("nombre")
            val direccion = backStackEntry.arguments?.getString("direccion")
            val enlace = backStackEntry.arguments?.getString("enlace")
            val university = if (id != null && id != -1L) Universidad(id, nombre ?: "", direccion ?: "", enlace ?: "") else null
            val isEditMode = id != null && id != -1L
            UniversityFormScreen(navController, university, isEditMode)
        }
    }
}
@Composable
fun LoginScreen(navController: NavController) {
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
        val context= LocalContext.current
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón de inicio de sesión
            Button(onClick = {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    // Llamar a la función performingLogin con los datos ingresados por el usuario
                    performLogin(username, password, context, navController) { success ->
                        if (success) {
                            // Si el inicio de sesión es exitoso, puedes realizar alguna acción adicional aquí
                            // Por ejemplo, navegar a la segunda pantalla
                            navController.navigate(Screen.Second.route)
                        } else {
                            // Si hay un error en el inicio de sesión, puedes manejarlo aquí
                            showToast(context, "Error en el inicio de sesión")
                        }
                    }
                } else {
                    // Mostrar un mensaje de error si algún campo está vacío
                    showToast(context, "Por favor, ingresa tu usuario y contraseña")
                }
            }) {
                Text("Login")
            }


            // Botón de registro
            Button(onClick = {

                // Cambiar al modo de registro o realizar el registro según el estado actual
                if (isRegistering) {
                    performRegistration(username, email, password, context, navController) { success ->
                        if (success) {
                            navController.navigate(Screen.Second.route)
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


fun performRegistration(username: String, email: String, password: String, context: Context, navController: NavController, callback: (Boolean) -> Unit) {
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


fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
fun performLogin(username: String, password: String, context: Context, navController: NavController, callback: (Boolean) -> Unit) {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    // Realizar una llamada al método del API para obtener el usuario por nombre de usuario
    apiService.getUserByUsername(username).enqueue(object : Callback<User> {
        override fun onResponse(call: Call<User>, response: Response<User>) {
            if (response.isSuccessful) {
                val user = response.body()
                // Verificar si se encontró un usuario y si la contraseña coincide
                if (user != null && user.password == password) {
                    navController.navigate(Screen.Second.route)
                    callback(true)
                } else {
                    // Usuario no encontrado o contraseña incorrecta
                    showToast(context, "Credenciales inválidas")
                    callback(false)
                }
            } else {
                // Error en la solicitud
                showToast(context, "Error en la solicitud")
                callback(false)
            }
        }

        override fun onFailure(call: Call<User>, t: Throwable) {
            // Error de red al realizar la solicitud
            showToast(context, "Error de red al iniciar sesión")
            t.printStackTrace()
            callback(false)
        }
    })
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}

