package com.example.project

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.ui.theme.ProjectTheme

class Account : ComponentActivity() {
    // on below line we are creating a variable for our shared preferences.
    lateinit var sharedPreferences: SharedPreferences

    // on below line we are creating a variable
    // for prefs key and email key and pwd key.
    var PREFS_KEY = "prefs"
    var USER_KEY = "username"
    var USER_ID_KEY = "userId"

    // on below line we are creating a variable for email
    var username = ""
    var userId = 0

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
                    username = sharedPreferences.getString(USER_KEY, "").toString()
                    userId = sharedPreferences.getInt(USER_ID_KEY, 0)

                    var context = LocalContext.current
                    val activity = (context as? Activity)
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                colors = TopAppBarDefaults.smallTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                title = {
                                    Text(
                                        "Account Details",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 30.sp
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        var navigate = Intent(context, Home::class.java)
                                        context.startActivity(navigate)

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
                        bottomBar = {
                            BottomAppBar(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primary)
                                    .height(75.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(-10.dp)
                                    ) {
                                        IconButton(onClick = {
                                            val navigate = Intent(context, DeckBuilder::class.java)
                                            context.startActivity(navigate)
                                            activity?.finish()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Create,
                                                contentDescription = "New Deck",
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                        Text(fontSize = 15.sp, text = "Build")
                                    }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(-10.dp)
                                    ) {
                                        IconButton(onClick = {
                                            val navigate = Intent(context, Home::class.java)
                                            context.startActivity(navigate)
                                            activity?.finish()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Home,
                                                contentDescription = "Home",
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                        Text(fontSize = 15.sp, text = "Home")
                                    }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(-10.dp)
                                    ) {
                                        IconButton(onClick = {
                                            // Nothing to program here
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.AccountCircle,
                                                contentDescription = "Account",
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                        Text(fontSize = 15.sp, text = "Account")
                                    }
                                }
                            }
                        },
                    ) { paddingValues ->
                        AccountInformation(
                            Modifier.padding(paddingValues),
                            username,
                            userId,
                            sharedPreferences
                        )
                    }
                }
            }
        }
    }
}

/*
 * Lists account information and offers sign out option
 *
 * @param paddingValues, the padding values passed from the top bar
 * @param username, the user name of the instance user
 * @param userId, the userId of the current sharedPreferences object containing instance user valuesnstance user
 * @param sharedPreferences,
 */
@Composable
fun AccountInformation(
    paddingValues: Modifier,
    username: String,
    userId: Int,
    sharedPreferences: SharedPreferences
) {
    var context = LocalContext.current
    var activity = (context as? Activity)

    Column(
        paddingValues
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "User Id: $userId",
            Modifier
                .fillMaxWidth(0.75f)
                .padding(horizontal = 20.dp),
            fontSize = 20.sp
        )

        Text(
            text = "Username: $username",
            Modifier
                .fillMaxWidth(0.75f)
                .padding(horizontal = 20.dp),
            fontSize = 20.sp
        )

        Button(modifier = Modifier
            .fillMaxWidth(0.40f)
            .padding(horizontal = 20.dp),
            onClick = {
                // on below line we are creating a variable for our editor.
                val editor: SharedPreferences.Editor = sharedPreferences.edit()

                // on below line we are passing email and pwd with empty values.
                editor.putString("email", "")
                editor.putString("password", "")
                editor.putInt("userId", 0)

                // on below line we are applying changes which are updated.
                editor.apply()

                // on below line we are opening our main activity.
                val navigate = Intent(context, MainActivity::class.java)
                context.startActivity(navigate)
                activity?.finish()
            }) {
            Text(text = "Logout")
        }
    }
}