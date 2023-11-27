package com.example.project

import androidx.activity.ComponentActivity
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.ui.theme.ProjectTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.project.model.CardItem
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import com.example.project.room.ProjectDB
import com.example.project.room.CardEntity
import kotlinx.coroutines.GlobalScope


class DeckbuildStart : ComponentActivity(){


    lateinit var sharedPreferences: SharedPreferences
    var PREFS_KEY = "prefs"

    private val BASE_URL = "https://raw.githubusercontent.com/the-fab-cube/flesh-and-blood-cards/main/json/english/"


    @Composable
    fun getData() : ArrayList<CardItem>{
        var cards by remember { mutableStateOf(ArrayList<CardItem>()) }
        val scope= rememberCoroutineScope()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build().create(FaBAPIService::class.java)

        val cardData = retrofit.getCards()


        DisposableEffect(key1=Unit){
            scope.launch(Dispatchers.IO){
                cardData.enqueue(object: Callback<ArrayList<CardItem>> {
                    override fun onResponse(
                        call: Call<ArrayList<CardItem>>,
                        response: Response<ArrayList<CardItem>>
                    ) {
                        if (response.isSuccessful) {
                            cards = (response.body() ?: emptyList()) as ArrayList<CardItem>
                            Log.i("asdfg", response.toString())
                        } else {
                            Log.i("asdfg", response.toString())
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<CardItem>>, t: Throwable) {
                        // Handle failure of the data object
                    }
                })
            }

            onDispose {
                cardData.cancel()
            }
        }

        return cards
    }

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

                    dbCards(db = db)
                    DeckbuildScreen()
                }
            }
        }
    }


    @Composable
    fun dbCards(db : ProjectDB){

        val cards = getData()
        val cardDB = db.cardDAO()

        LaunchedEffect(cards){
                for (card in cards) {
                    val cardEntry = cardDB.addCard(
                        CardEntity(
                            card.unique_id,
                            card.cost,
                            card.defense,
                            card.functional_text,
                            card.functional_text_plain,
                            card.health,
                            card.intelligence,
                            card.name,
                            card.pitch,
                            card.power,
                            card.type_text,
                            card.printings[0].image_url
                        )
                    )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DeckbuildScreen()
    {
        val cards = getData()

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    title = {
                        Text(
                            "Testing API Results",
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
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Add com.example.project.model.Card values here
                    items(items = cards) { item ->
                        Card(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(maxOf(0.90f))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(text = item.name + " (" + item.pitch + ")", Modifier.padding(horizontal = 5.dp))
                                Box(modifier = Modifier.fillMaxWidth(0.75f))

                            }
                        }
                    }
                }

            }
        }
    }
}