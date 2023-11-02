package com.example.project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.room.DeckEntity
import com.example.project.room.ProjectDB
import com.example.project.room.UserEntity
import com.example.project.ui.theme.ProjectTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Home : ComponentActivity() {

    // on below line we are creating a variable for our shared preferences.
    lateinit var sharedPreferences: SharedPreferences

    // on below line we are creating a variable
    // for prefs key and email key and pwd key.
    var PREFS_KEY = "prefs"
    var USER_KEY = "username"
    var USER_ID_KEY = "userId"

    // on below line we are creating a variable for email
    var username = ""

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
                    var userId = sharedPreferences.getInt(USER_ID_KEY, 0)

                    var context = LocalContext.current
                    var db = ProjectDB.getInstance(context)

                    if (username != null) {
                        Scaffold(
                            topBar = {
                                CenterAlignedTopAppBar(
                                    colors = TopAppBarDefaults.smallTopAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    title = {
                                        Text(
                                            "Home",
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
                                            IconButton(onClick = { /*TODO*/ }) {
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
                                    text = "Welcome, $username",
                                    modifier = Modifier.padding(10.dp)
                                )
                                UserDecks(db, userId)
                            }
                        }
                    }
                }
            }
        }
    }
}


/*
 * Lists all user decks using a LazyColumn, if any decks exist.
 *
 * @param db, an instance of the database being used
 * @param userId, the integer value of the current signed in user
 */
@Composable
fun UserDecks(db: ProjectDB, userId: Int) {

    val deckDB = db.deckDAO()
    val decks by deckDB.getDecks(userId).collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = decks) { item ->
            DeckCard(deckEntity = item)
        }
    }
}

@Composable
fun DeckCard(deckEntity: DeckEntity) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(maxOf(0.90f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = deckEntity.deckName, Modifier.padding(horizontal = 5.dp))
            Box(modifier = Modifier.fillMaxWidth(0.75f))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "View")
            }
        }
    }
}