package com.example.project

import retrofit2.Call
import retrofit2.http.GET
import com.example.project.model.CardItem
import com.example.project.model.CardTypeItem


interface FaBAPIService {
    @GET("card.json")
    fun getCards () : Call <ArrayList<CardItem>>

}
