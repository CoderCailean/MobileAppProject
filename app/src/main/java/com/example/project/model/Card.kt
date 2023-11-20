package com.example.project.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class Card(
    val id : String,
    @SerialName(value = "name")
    val name : String
)
