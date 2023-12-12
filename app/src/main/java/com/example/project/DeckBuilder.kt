package com.example.project

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.room.CardEntity
import com.example.project.room.DeckCardEntity
import com.example.project.room.DeckEntity
import com.example.project.room.ProjectDB
import com.example.project.ui.theme.ProjectTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DeckBuilder : ComponentActivity() {

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
                    val activity = (context as? Activity)
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
                                            "Deck Builder",
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
                                            IconButton(onClick = { /*NO CODE HERE*/ }) {
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
                                                var navigate = Intent(context, Account::class.java)
                                                context.startActivity(navigate)
                                                activity?.finish()
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

                                DeckBuilding(db, userId)
                            }
                        }
                    }
                }
            }
        }
    }

    /*
 * Provides an interface for users to build a new deck
 *
 * @param db, an instance of the ProjectDB
 * @param user, the userId of the current user
 */
    @OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
    @Composable
    fun DeckBuilding(db: ProjectDB, user: Int) {

        var context = LocalContext.current
        val activity = (context as? Activity)

        var deckNameError by remember {
            mutableStateOf(false)
        }
        var deckNameInput by remember {
            mutableStateOf("")
        }
        var heroNameError by remember {
            mutableStateOf(false)
        }
        var heroSelection by remember {
            mutableStateOf("")
        }
        var expanded by remember {
            mutableStateOf(false)
        }
        var cardSelectionError by remember {
            mutableStateOf(false)
        }
        var selectedCards = remember {
            mutableStateListOf<CardEntity>()
        }

        Column(
            Modifier
                .padding(vertical = 5.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = deckNameInput,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { et -> deckNameInput = et },
                label = { Text(text = "Enter a deck name") },
                placeholder = { Text(text = "Dragons...") },
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(vertical = 10.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary
                ),
            )
            if (deckNameError) {
                Text(text = "*Every deck needs a name!")
            }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded })
            {

                TextField(
                    value = heroSelection,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text(text = "Choose your hero") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .padding(vertical = 10.dp)
                        .menuAnchor(),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text(text = "Alduin") },
                        onClick = {
                            heroSelection = "Alduin"
                            expanded = false
                        })
                    Divider()
                    DropdownMenuItem(
                        text = { Text(text = "Merlin") },
                        onClick = {
                            heroSelection = "Merlin"
                            expanded = false
                        })
                }
            }
            if (heroNameError) {
                Text(text = "*Every deck needs a hero!")
            }

            val cardDB = db.cardDAO()
            val cards by cardDB.getCards().collectAsState(initial = emptyList())

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(vertical = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(items = cards) { item ->
                    var selected by remember {
                        mutableStateOf(false)
                    }

                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(maxOf(0.90f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(text = item.name, Modifier.padding(horizontal = 5.dp))
                            Box(modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                selected = !selected
                                if (selected) {
                                    selectedCards.add(item)
                                    Log.d("cards", selectedCards.count().toString())
                                } else if (!selected) {
                                    selectedCards.remove(item)
                                    Log.d("cards", selectedCards.count().toString())
                                }
                            }) {
                                if (selected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "View"
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircleOutline,
                                        contentDescription = "View"
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (cardSelectionError) {
                Text(text = "*Every deck needs a card or two!")
            }

            Box(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    heroNameError = false
                    deckNameError = false
                    cardSelectionError = false

                    if (heroSelection == "") {
                        heroNameError = true
                    }
                    if (deckNameInput == "") {
                        deckNameError = true
                    }
                    if (selectedCards.count() == 0) {
                        cardSelectionError = true
                    }

                    if (heroNameError || deckNameError || cardSelectionError) {

                    }
                    else{
                        GlobalScope.launch {
                            val newDeck = db.deckDAO()
                                .addDeck(DeckEntity(0, deckNameInput, heroSelection, user))

                            val successCheck: Long = -1

                            if (newDeck != successCheck) {
                                for (card in selectedCards) {
                                    var cardEntry = db.deckCardDAO().addDeckCard(
                                        DeckCardEntity(
                                            0,
                                            card.cardId,
                                            newDeck.toInt()
                                        )
                                    )
                                }

                                var navigate = Intent(context, Home::class.java)
                                context.startActivity(navigate)
                                activity?.finish()
                            }
                        }
                    }

                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth(0.50f)
                    .padding(vertical = 5.dp),
            ) {
                Text(text = "Create Deck")
            }
        }
    }
}