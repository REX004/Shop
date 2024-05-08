package com.example.shop.session3.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class Favorite(
    val id: Int,
    val user_id: String,
    val name: String,
    val price: String,
    val information: String,
    val gender: String,
    val picture: String,
    val category: String
)
