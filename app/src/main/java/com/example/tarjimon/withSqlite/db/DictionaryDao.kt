package com.example.tarjimon.withSqlite.db

import android.database.Cursor

interface DictionaryDao {
    fun getAll(): Cursor
    fun    search(word: String, lang: String): Cursor
    fun addFavourite(id: Int)
    fun removeFavourite(id: Int)
    fun getAllFavourite(): Cursor


}