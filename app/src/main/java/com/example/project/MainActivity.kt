package com.example.project

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.project.ui.theme.ProjectTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

class MainActivity : ComponentActivity() {
    // on below line we are creating
    // a variable for shared preferences.
    lateinit var sharedPreferences: SharedPreferences

    // on below line we are creating a variable
    // for prefs key and email key and pwd key.
    var PREFS_KEY = "prefs"
    var USER_KEY = "username"
    var PWD_KEY = "password"

    // on below line we are creating variable
    // for email as e and password as p.
    var e = ""
    var p = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    /*
     * Overrides the onStart event of the application to check if a current user is saved
     * in the sharedPreferences variable, if a user exists, they are pushed to the main user
     * screen, if not, the main activity screen is displayed.
     */
    override fun onStart() {
        super.onStart()
        // on below line we are creating a variable for activity.
        val activity = (this as? Activity)

        // on below line we are initializing our shared preferences.
        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

        // on below line we are initializing our email and pwd
        // variable setting values from shared preferences.
        val username = sharedPreferences.getString(USER_KEY, "").toString()
        val pwd = sharedPreferences.getString(PWD_KEY, "").toString()

        // on below line we are checking if email and pwd are empty or not.
        if (username != "" && pwd != "") {
            // if email and pwd are not empty we are opening
            // a new activity on below line.
            val navigate = Intent(this, Home::class.java)

            // on below line we are starting our new activity
            // and finishing our current activity.
            startActivity(navigate)
            activity?.finish()
        }
    }
}

/*
 * Provides the main activity screen, giving the user the option to sign in or sign up.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var currentContext = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        "Flesh & Blood Companion",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 30.sp
                    )
                },
                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                    rememberTopAppBarState()
                ),
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(color = Color.DarkGray, fontSize = 20.sp, text = "Sign in or create a new account")

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(maxOf(0.50f)),
                onClick = {
                    currentContext.startActivity(Intent(currentContext, SignIn::class.java))
                }) {
                Text(text = "Sign In")
            }

            Button(colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(maxOf(0.50f)), onClick = {
                    currentContext.startActivity(Intent(currentContext, SignUp::class.java))
                }) {
                Text(text = "Sign Up")
            }
        }
    }
}