package com.example.project

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
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.room.ProjectDB
import com.example.project.ui.theme.ProjectTheme

class SelectHero : ComponentActivity(){

    lateinit var sharedPreferences: SharedPreferences

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            var context = LocalContext.current
            var db = ProjectDB.getInstance(context)

            ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                colors = TopAppBarDefaults.smallTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                title = {
                                    Text(
                                        "Select a Hero",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 30.sp
                                    )
                                },
                                actions = {
                                    IconButton(onClick = { /* do something */ }) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = "Localized description",
                                            tint = MaterialTheme.colorScheme.secondary,
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
                                            var navigate = Intent(context, SelectHero::class.java)
                                            navigate.putExtra("FromPage", "SignUp")
                                            context.startActivity(navigate)
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
                                        IconButton(onClick = { /*TODO*/ }) {
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
                                            var navigate = Intent(context, Account::class.java)
                                            context.startActivity(navigate)
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
                        Column(modifier = Modifier.padding(paddingValues)) {
                            Text(
                                fontSize = 30.sp,
                                text = "Deckbuild Screen",
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}