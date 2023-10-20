package com.example.project

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.room.ProjectDB
import com.example.project.room.UserEntity
import com.example.project.ui.theme.ProjectTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.compose.material3.Snackbar
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat.startActivity
import java.time.Duration

class SignUp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Generating a database instance
                    val context = LocalContext.current
                    val db = ProjectDB.getInstance(context)
                    SignUpScreen(db)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(db: ProjectDB) {

    var signUpSuccess by remember { mutableStateOf(false) }
    var currentContext = LocalContext.current

    var usernameInput by remember {
        mutableStateOf("")
    }

    var passwordInput by remember {
        mutableStateOf("")
    }

    Column() {
        Text(text = "Sign up page")

        TextField(
            value = usernameInput,
            onValueChange = { et -> usernameInput = et },
            label = { Text(text = "Enter a new User name") },
            placeholder = { Text(text = "User Name") })

        TextField(
            value = passwordInput,
            onValueChange = { et -> passwordInput = et },
            label = { Text(text = "Enter a password") },
            placeholder = { Text(text = "Password123...") })

        Button(onClick = {

            if(usernameInput.length < 5){

            }else if(passwordInput.length < 7){

            }else{
                GlobalScope.launch {
                    val user = db.userDAO().addUser(UserEntity(0, usernameInput, passwordInput))

                    val successCheck: Long = -1

                    if(user != successCheck){
                        currentContext.startActivity(Intent(currentContext, SignIn::class.java))
                    }
                }
            }
        }) {
            Text(text = "Create User")

        }
    }

    if(signUpSuccess){
        Snackbar {
            Text(text = "Success")

        }
    }
}
