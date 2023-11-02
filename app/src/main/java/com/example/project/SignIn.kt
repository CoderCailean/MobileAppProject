package com.example.project

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.unit.sp
import com.example.project.room.ProjectDB
import com.example.project.room.UserEntity
import com.example.project.ui.theme.ProjectTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule

class SignIn : ComponentActivity() {
    // on below line we are creating
    // a variable for shared preferences.
    lateinit var sharedPreferences: SharedPreferences

    // on below line we are creating a variable
    // for prefs key and email key and pwd key.
    var PREFS_KEY = "prefs"
    var USER_KEY = "username"
    var PWD_KEY = "pwd"

    // on below line we are creating variable
    // for email as e and password as p.
    var e = ""
    var p = ""

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // on below line we are initializing our shared preferences.
            sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

            // on below line we are creating
            // variable for email and password.
            val email = remember {
                mutableStateOf("")
            }
            val pwd = remember {
                mutableStateOf("")
            }

            // on below line we are initializing values for both email and password.
            email.value = sharedPreferences.getString(USER_KEY, "").toString()
            pwd.value = sharedPreferences.getString(PWD_KEY, "").toString()

            // on below line we are setting value
            // from email and password to e and p
            e = email.value
            p = pwd.value

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
                    var activity = (context as? Activity)

                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                colors = TopAppBarDefaults.smallTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                title = {
                                    Text(
                                        "Sign In",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 30.sp
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        activity?.finish()

                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowBack,
                                            contentDescription = "Localized description",
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(120.dp)
                                        )
                                    }
                                },
                                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                                    rememberTopAppBarState()
                                ),
                            )
                        },
                    ) {
                        sessionManagement(db, context, sharedPreferences = sharedPreferences)
                    }

                }
            }
        }
    }
}

/*
 * Attempts user sign in, if successful, calls a function to save the user data
 *
 * @param db, an instance of the ProjectDB database
 * @param context, an instance of the current context
 * @param sharedPreferences, the current sharedPreferences object containing instance user values
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sessionManagement(db: ProjectDB, context: Context, sharedPreferences: SharedPreferences) {

    val context = context
    val activity = (context as? Activity)


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
    Column(
        Modifier
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
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary
            )
        )

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
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary
            )
        )

        if (showErrors) {
            Text(
                text = "You have entered an incorrect username/password combination.",
                modifier = Modifier.fillMaxWidth(0.75f),
                color = Color.Red
            )
        }

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth(0.50f),
            onClick = {
                GlobalScope.launch {
                    var user = db.userDAO().checkUser(usernameInput)

                    if (user == 0) {
                        showErrors = true
                    } else {
                        showErrors = false
                        var user = db.userDAO().getUser(usernameInput)

                        if (user.username == usernameInput && user.password == passwordInput) {
                            // on below line we are calling save data method
                            // to save data to shared preferences.
                            saveData(
                                usernameInput,
                                passwordInput,
                                user.userId,
                                sharedPreferences,
                                context
                            )

                            // on the below line we are calling finish to close the current activity.
                            activity?.finish()

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

/*
 * Saves the information of the user signing in
 *
 * @param username, the username of the user being signed in
 * @param password, the password of the user being signed in
 * @param userId, the userId of the user being signed in
 * @param sharedPreferences, the current sharedPreferences object containing instance user values
 * @param context, an instance of the current context
 */
fun saveData(
    username: String,
    password: String,
    userId: Int,
    sharedPreferences: SharedPreferences,
    context: Context
) {
    // on below line we are creating an editor and initializing
    // it with shared preferences.
    val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // on below line we are setting email and pwd value with key.
    editor.putString("username", username)
    editor.putString("password", password)
    editor.putInt("userId", userId)

    // on the below line we are applying
    // changes to our shared prefs.
    editor.apply()

    // on below line we are opening a new intent.
    val navigate = Intent(context, Home::class.java)
    navigate.putExtra("username", username)
    navigate.putExtra("userId", userId)
    context.startActivity(navigate)
}


//package com.example.project
//
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.compose.runtime.getValue
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
//import androidx.compose.material3.Button
//import androidx.compose.material3.CenterAlignedTopAppBar
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.material3.rememberTopAppBarState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.example.project.room.ProjectDB
//import com.example.project.room.UserEntity
//import com.example.project.ui.theme.ProjectTheme
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import java.util.Timer
//import kotlin.concurrent.schedule
//
//class SignIn : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            ProjectTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//
//                    val context = LocalContext.current
//                    val db = ProjectDB.getInstance(context)
//
//                    var fromPage = intent.getStringExtra("FromPage")
//                    if (fromPage == "SignUp") {
//                        Toast.makeText(context, "Sign up successful", Toast.LENGTH_LONG).show()
//                    }
//
//                    SignInPage(db)
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SignInPage(db: ProjectDB) {
//
//    var currentContext = LocalContext.current
//
//    var showPassword by remember {
//        mutableStateOf(value = false)
//    }
//
//    var userCheck by remember {
//        mutableStateOf(false)
//    }
//
//    var showErrors by remember {
//        mutableStateOf(false)
//    }
//
//    var usernameInput by remember {
//        mutableStateOf("")
//    }
//
//    var passwordInput by remember {
//        mutableStateOf("")
//    }
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                colors = TopAppBarDefaults.smallTopAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.primary
//                ),
//                title = {
//                    Text(
//                        "Sign In",
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                },
//                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
//                    rememberTopAppBarState()
//                ),
//            )
//        },
//    ) { paddingValues ->
//        Column(
//            Modifier
//                .padding(paddingValues)
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            OutlinedTextField(
//                value = usernameInput,
//                onValueChange = { et -> usernameInput = et },
//                label = { Text(text = "Enter your username") },
//                modifier = Modifier.fillMaxWidth(0.75f),
//                shape = RoundedCornerShape(percent = 20),
//                placeholder = {
//                    Text(
//                        text = "Username.."
//                    )
//                })
//
//            OutlinedTextField(
//                value = passwordInput,
//                visualTransformation = if (showPassword) {
//                    VisualTransformation.None
//                } else {
//                    PasswordVisualTransformation()
//                },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                onValueChange = { et -> passwordInput = et },
//                label = { Text(text = "Enter your password") },
//                modifier = Modifier
//                    .fillMaxWidth(0.75f)
//                    .padding(vertical = 10.dp),
//                shape = RoundedCornerShape(percent = 20),
//                placeholder = { Text(text = "Password..") },
//                trailingIcon = {
//                    if (showPassword) {
//                        IconButton(onClick = { showPassword = false }) {
//                            Icon(
//                                imageVector = Icons.Filled.Visibility,
//                                contentDescription = "hide_password"
//                            )
//                        }
//                    } else {
//                        IconButton(
//                            onClick = { showPassword = true }) {
//                            Icon(
//                                imageVector = Icons.Filled.VisibilityOff,
//                                contentDescription = "hide_password"
//                            )
//                        }
//                    }
//                }
//            )
//
//            if (showErrors) {
//                Text(
//                    text = "You have entered an incorrect username/password combination.",
//                    modifier = Modifier.fillMaxWidth(0.75f),
//                    color = Color.Red
//                )
//            }
//
//            Button(modifier = Modifier.fillMaxWidth(0.50f), onClick = {
//                GlobalScope.launch {
//                    var user = db.userDAO().checkUser(usernameInput)
//
//                    if (user == 0) {
//                        showErrors = true
//                    } else {
//                        showErrors = false
//                        var user = db.userDAO().getUser(usernameInput)
//
//                        if (user.username == usernameInput && user.password == passwordInput) {
//                            // Creating intent object, adding user data to it, then starting new activity
//                            var navigate = Intent(currentContext, Home::class.java)
//                            navigate.putExtra("username", user.username)
//                            navigate.putExtra("userId", user.userId)
//                            currentContext.startActivity(navigate)
//                        } else {
//                            showErrors = true
//                        }
//
//                    }
//
//                }
//                userCheck = true
//            }) {
//                Text(text = "Sign-In")
//            }
//        }
//    }
//}

