package com.example.project

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.model.Card
import com.example.project.room.CardEntity
import com.example.project.room.ProjectDB
import com.example.project.room.UserEntity
import com.example.project.ui.theme.ProjectTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class DeckbuildStart : ComponentActivity(){


    lateinit var sharedPreferences: SharedPreferences
    var PREFS_KEY = "prefs"

    private val BASE_URL = "https://api.fabdb.net/"
    private val hash = "c5392d43c8f45e6e961ddf66dcfa36770a9964ad9e8feedd3e903d5d905821ae97e7de5578b09eb1bec008ef4731b457c525dc8d24b32ccfb086230c00e590b3"

    @Composable
    fun cardList() : ArrayList<Card>{
        var cards by remember { mutableStateOf(ArrayList<Card>()) }
        val scope= rememberCoroutineScope()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build().create(FaBAPIService::class.java)

        val time = System.currentTimeMillis().toString()

        val cardData = retrofit.getCards(time, hash)

        DisposableEffect(key1=Unit){
            scope.launch(Dispatchers.IO){
                cardData.enqueue(object: Callback<ArrayList<Card>> {
                    override fun onResponse(
                        call: Call<ArrayList<Card>>,
                        response: Response<ArrayList<Card>>
                    ) {
                        if (response.isSuccessful) {
                            cards = (response.body() ?: emptyList()) as ArrayList<Card>
                            Log.i("Response", response.toString())
                        } else {
                            //Handle a Error Response
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<Card>>, t: Throwable) {
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
                    DeckbuildScreen()
                    cardList()
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DeckbuildScreen()
    {

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
                cardList()
            }
        }
    }

//    @Composable
//    fun CardList() {
//
//        LazyColumn(
//            modifier = Modifier.fillMaxWidth(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            //Add Card values here
//            items(items = cards) { item ->
//                CardCard(card = item)
//            }
//        }
//    }

    @Composable
    fun CardCard(card: Card) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(maxOf(0.90f))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = card.name, Modifier.padding(horizontal = 5.dp))
                Box(modifier = Modifier.fillMaxWidth(0.75f))
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "View")
                }
            }
        }
    }
}