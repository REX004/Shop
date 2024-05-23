package com.example.shop.session3.data

import kotlinx.serialization.Serializable


@Serializable
data class Favorite(
    val id: Int?,
    val user_id: String,
    val name: String,
    val price: String,
    val information: String,
    val gender: String,
    val picture: String,
    val category: String
)
