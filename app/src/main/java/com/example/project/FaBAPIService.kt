package com.example.project

import com.example.project.model.Card
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface FaBAPIService {
    @Headers("Authorization: Bearer 063e7bf561502724f30ef53baf109910ed044b11163c3a9bd7199bac6035091b")
    @GET("cards")
    fun getCards (
        @Query("time") time: String,
        @Query("hash") hash: String
    ) : Call<ArrayList<Card>>

}
