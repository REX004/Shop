package com.example.shop.session3.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class Favorite(
    val id: Int? = null,
    val user_id : String,
    val name: String,
    val price: String,
    val information: String,
    val gender: String,
    val picture: String,
    val category: String
) {
    companion object {
        fun fromJson(jsonObject: JsonObject): Favorite {
            return Favorite(
                id = jsonObject["id"]?.jsonPrimitive?.intOrNull,
                user_id = jsonObject["user_id"]?.jsonPrimitive?.contentOrNull ?: "",
                name = jsonObject["name"]?.jsonPrimitive?.contentOrNull ?: "",
                price = jsonObject["price"]?.jsonPrimitive?.contentOrNull ?: "",
                information = jsonObject["information"]?.jsonPrimitive?.contentOrNull ?: "",
                gender = jsonObject["gender"]?.jsonPrimitive?.contentOrNull ?: "",
                picture = jsonObject["picture"]?.jsonPrimitive?.contentOrNull?: "",
                category = jsonObject["category"]?.jsonPrimitive?.contentOrNull?: ""
            )
        }
    }
}
