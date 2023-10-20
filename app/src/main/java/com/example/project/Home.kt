package com.example.project

import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.project.room.DeckEntity
import com.example.project.room.ProjectDB
import com.example.project.ui.theme.ProjectTheme

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var username = intent.getStringExtra("username")
                    var userId = intent.getIntExtra("userId", 0)

                    var context = LocalContext.current
                    var db = ProjectDB.getInstance(context)

                    if (username != null) {
                        HomeScreen(db, username, userId)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(db: ProjectDB,username: String, userId: Int){
    
    Column {
        Text(text = "Welcome, $username")
        UserDecks(db, userId)
    }
}

@Composable
fun UserDecks(db: ProjectDB, userId: Int){
    
    val deckDB = db.deckDAO()
    val decks by deckDB.getDecks(userId).collectAsState(initial = emptyList())
    
    LazyColumn{
        items(items = decks){
            item -> DeckCard(deckEntity = item)
        }
    }
}

@Composable
fun DeckCard(deckEntity: DeckEntity){
    Card {
        Row {
            Text(text = deckEntity.deckName)
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "View")
            }
        }
    }
}