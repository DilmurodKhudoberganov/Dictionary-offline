package com.example.tarjimon.withSqlite.model

data class WoardData(
    val id: Int,
    val word: String,
    val type: String,
    val translate: String,
    val favourite: Int
) : java.io.Serializable
