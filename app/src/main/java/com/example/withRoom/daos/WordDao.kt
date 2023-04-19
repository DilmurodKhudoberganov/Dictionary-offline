package com.example.withRoom

import android.database.Cursor
import androidx.room.*
import com.example.withRoom.entities.BookEntity

@Dao
interface WordDao {



    @Query("UPDATE dictionary SET favourite=1 WHERE id=:id")
    fun setFavourite(id: Long)

    @Query("UPDATE dictionary SET favourite=0 WHERE id=:id")
    fun removeFavourite(id: Long)

    @Query("SELECT * FROM dictionary WHERE english LIKE :query || '%' ")
    fun getAllEnglish(query: String): Cursor

    @Query("SELECT * FROM dictionary WHERE uzbek LIKE :query || '%'")
    fun getAllUzbek(query: String): Cursor

    @Query("SELECT * FROM dictionary WHERE favourite = 1")
    fun getFavourite(): Cursor


}