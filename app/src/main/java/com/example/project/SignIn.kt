package com.example.project

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project.room.ProjectDB
import com.example.project.room.UserEntity
import com.example.project.ui.theme.ProjectTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule

class SignIn : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val context = LocalContext.current
                    val db = ProjectDB.getInstance(context)

                    var fromPage = intent.getStringExtra("FromPage")
                    if (fromPage == "SignUp") {
                        Toast.makeText(context, "Sign up successful", Toast.LENGTH_LONG).show()
                    }

                    SignInPage(db)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInPage(db: ProjectDB) {

    var currentContext = LocalContext.current

    var showPassword by remember {
        mutableStateOf(value = false)
    }

    var userCheck by remember {
        mutableStateOf(false)
    }

    var showErrors by remember {
        mutableStateOf(false)
    }

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
                        "Sign In",
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = usernameInput,
                onValueChange = { et -> usernameInput = et },
                label = { Text(text = "Enter your username") },
                modifier = Modifier.fillMaxWidth(0.75f),
                shape = RoundedCornerShape(percent = 20),
                placeholder = {
                    Text(
                        text = "Username.."
                    )
                })

            OutlinedTextField(
                value = passwordInput,
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { et -> passwordInput = et },
                label = { Text(text = "Enter your password") },
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(percent = 20),
                placeholder = { Text(text = "Password..") },
                trailingIcon = {
                    if (showPassword) {
                        IconButton(onClick = { showPassword = false }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "hide_password"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { showPassword = true }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "hide_password"
                            )
                        }
                    }
                }
            )

            if (showErrors) {
                Text(
                    text = "You have entered an incorrect username/password combination.",
                    modifier = Modifier.fillMaxWidth(0.75f),
                    color = Color.Red
                )
            }

            Button(modifier = Modifier.fillMaxWidth(0.50f), onClick = {
                GlobalScope.launch {
                    var user = db.userDAO().checkUser(usernameInput)

                    if (user == 0) {
                        showErrors = true
                    } else {
                        showErrors = false
                        var user = db.userDAO().getUser(usernameInput)

                        if (user.username == usernameInput && user.password == passwordInput) {
                            // Creating intent object, adding user data to it, then starting new activity
                            var navigate = Intent(currentContext, Home::class.java)
                            navigate.putExtra("username", user.username)
                            navigate.putExtra("userId", user.userId)
                            currentContext.startActivity(navigate)
                        } else {
                            showErrors = true
                        }

                    }

                }
                userCheck = true
            }) {
                Text(text = "Sign-In")
            }
        }
    }
}