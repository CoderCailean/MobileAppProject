package com.example.project

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.example.project.model.CardItem
import com.example.project.room.CardEntity
import com.example.project.room.ProjectDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
                    var context = LocalContext.current
                    var db = ProjectDB.getInstance(context)
                    dbCards(db)
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

@Composable
fun getData() : ArrayList<CardItem>{
    val BASE_URL = "https://raw.githubusercontent.com/the-fab-cube/flesh-and-blood-cards/main/json/english/"

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

@Composable
fun dbCards(db : ProjectDB){
    val cards = getData()
    val cardDB = db.cardDAO()

    LaunchedEffect(cards){
        GlobalScope.launch {
            var cardCount = cardDB.getRowCount()
            Log.d("Count", cardCount.toString())

            if(cardCount == 0){
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
    }
}

/*
 * Provides the main activity screen, giving the user the option to sign in or sign up.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var currentContext = LocalContext.current
    var db = ProjectDB.getInstance(currentContext)

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
