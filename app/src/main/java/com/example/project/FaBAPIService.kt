package com.example.project

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import CardItem


interface FaBAPIService {
    @GET("card.json")
    fun getCards () : Call <ArrayList<CardItem>>

}
