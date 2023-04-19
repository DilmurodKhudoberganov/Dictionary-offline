package com.example.withRoom.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.withRoom.WordDao
import com.example.withRoom.entities.BookEntity

@Database(entities = [BookEntity::class], version = 1)
abstract class WordDatabase : RoomDatabase() {

    companion object {

        private lateinit var instance: WordDatabase


        fun init(context: Context) {
            if (!(::instance.isInitialized)) {
                instance = Room.databaseBuilder(
                    context,
                    WordDatabase::class.java, "UserContact.db"
                ).allowMainThreadQueries().build()
            }
        }

        fun getInstance() = instance
    }


}