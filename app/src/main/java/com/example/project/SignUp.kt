package com.example.project

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
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

    var signUpSuccess by remember {
        mutableStateOf(false)
    }
    var uniqueUserCheck by remember{
        mutableStateOf(false)
    }

    var usernameError by remember {
        mutableStateOf(false)
    }
    var passwordError by remember {
        mutableStateOf(false)
    }
    var currentContext = LocalContext.current

    var usernameInput by remember {
        mutableStateOf("")
    }

    var passwordInput by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(
                        "Sign Up",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                    rememberTopAppBarState()
                ),
            )
        },
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            OutlinedTextField(
                value = usernameInput,
                onValueChange = { et -> usernameInput = et },
                label = { Text(text = "Enter a username") },
                placeholder = { Text(text = "User Name") },
                shape = RoundedCornerShape(percent = 20),
                modifier = Modifier.fillMaxWidth(0.75f))

            if(usernameError){
                Text(color = Color.Red, text = "Username must be between 5-12 characters")
            }
            if(uniqueUserCheck){
                Text(color = Color.Red, text = "Username taken")
            }

            OutlinedTextField(
                value = passwordInput,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { et -> passwordInput = et },
                label = { Text(text = "Enter a password") },
                placeholder = { Text(text = "Password123...") },
                shape = RoundedCornerShape(percent = 20),
                modifier = Modifier.fillMaxWidth(0.75f).padding(vertical = 10.dp))

            if(passwordError){
                Text(color = Color.Red, text = "Password must be a 7 character minimum")
            }

            Button(modifier = Modifier.fillMaxWidth(0.50f), onClick = {

                usernameError = usernameInput.length < 5 || usernameInput.length > 12

                passwordError = passwordInput.length < 7

                if(!usernameError){
                    GlobalScope.launch{
                        var user = db.userDAO().checkUser(usernameInput)
                        uniqueUserCheck = user != 0
                    }
                }

                if(!usernameError && !passwordError && !uniqueUserCheck) {
                    GlobalScope.launch {
                        val user = db.userDAO().addUser(UserEntity(0, usernameInput, passwordInput))

                        val successCheck: Long = -1

                        if (user != successCheck) {

                            var navigate = Intent(currentContext, SignIn::class.java)
                            navigate.putExtra("FromPage", "SignUp")
                            currentContext.startActivity(navigate)
                        }
                    }
                }
            }) {
                Text(text = "Create User")

            }
        }

    }
}
