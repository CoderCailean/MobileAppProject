package com.example.project

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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

    var userCheck by remember{
        mutableStateOf(false)
    }

    var showErrors by remember{
        mutableStateOf(false)
    }

    var usernameInput by remember {
        mutableStateOf("")
    }

    var passwordInput by remember {
        mutableStateOf("")
    }

    Column {
        Text(text = "Sign in page")

        TextField(
            value = usernameInput,
            onValueChange = { et -> usernameInput = et },
            label = { Text(text = "Enter your username") },
            placeholder = {
                Text(
                    text = "Username.."
                )
            })

        TextField(
            value = passwordInput,
            onValueChange = { et -> passwordInput = et },
            label = { Text(text = "Enter your password") },
            placeholder = {
                Text(
                    text = "Password.."
                )
            })

        if(showErrors){
            Text(text = "You have entered an incorrect username/password combination.", color = Color.Red)
        }

        Button(onClick = {
            GlobalScope.launch {
                var user = db.userDAO().checkUser(usernameInput)

                if(user == 0){
                    Log.d("check", user.toString())
                    showErrors = true
                }else{
                    showErrors = false
                    var user = db.userDAO().getUser(usernameInput)

                    if(user.username == usernameInput && user.password == passwordInput){
                        // Creating intent object, adding user data to it, then starting new activity
                        var navigate = Intent(currentContext, Home::class.java)
                        navigate.putExtra("username", user.username)
                        navigate.putExtra("userId", user.userId)
                        currentContext.startActivity(navigate)
                    }else{
                        showErrors = true
                    }

                }

            }
            userCheck = true
        }) {
            Text(text = "Sign-In")
        }
    }
    if(userCheck){




        userCheck = false
//        val auth = UserCheck(db, usernameInput)
//
//        if(auth == 0){
//            Log.d("check", "user not found")
//            userCheck = false
//        }else{
//            Log.d("check", "user found")
//        }

//        if(auth){
//            Log.d("check", "User not found")
//            userCheck = false
//        }
//        else if(auth.isNotEmpty()){
//            Log.d("check", "User found")
//        }
    }
}

@Composable
fun UserCheck(db: ProjectDB, username: String): Int {

    return db.userDAO().checkUser(username)
}