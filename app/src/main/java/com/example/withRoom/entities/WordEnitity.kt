package com.example.withRoom.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WordEnitity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val english: String,
    val type: String,
    val transcript: String,
    val uzbek: String,
    val countable: String,
    val favourite: Int

)
