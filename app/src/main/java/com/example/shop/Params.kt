package com.example.shop

data class Params(
    val query: String,
    val width: Int,
    val height: Int,
    val num_images : Int,
    val type: String = "GENERATE",
)