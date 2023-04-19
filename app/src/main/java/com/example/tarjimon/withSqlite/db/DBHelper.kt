package com.example.tarjimon.withSqlite.db

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tarjimon.withSqlite.utils.Constants
import java.io.FileOutputStream
import java.io.InputStream

class DBHelper private constructor(private val context: Context) :
    SQLiteOpenHelper(context, Constants.DB_NAME, null, Constants.VERSION), DictionaryDao {

    lateinit var database: SQLiteDatabase


    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: DBHelper

        fun getInstence(context: Context): DBHelper {
            if (!(::instance.isInitialized)) {
                instance = DBHelper(context)
            }
            return instance
        }
    }

    init {
        val file = context.getDatabasePath("localDictionary.db")
        if (!file.exists()) {
            copyToLocal()
        }
        database =
            SQLiteDatabase.openDatabase(file.absolutePath, null, SQLiteDatabase.OPEN_READWRITE)
    }

    private fun copyToLocal() {
        val inputStream: InputStream = context.assets.open(Constants.DBFILE_NAME)
        val file = context.getDatabasePath("localDictionary.db")
        val fileOutputStream = FileOutputStream(file)
        try {
            val byte = ByteArray(1024)
            var length: Int
            while (inputStream.read(byte).also { length = it } > 0) {
                fileOutputStream.write(byte, 0, length)
            }
            fileOutputStream.flush()
        } catch (e: Exception) {
            file.delete()
        } finally {
            inputStream.close()
            fileOutputStream.close()
        }
    }

    // Empty
    override fun onCreate(db: SQLiteDatabase?) {
    }

    // Empty
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


    override fun getAll(): Cursor = database.rawQuery("SELECT *FROM dictionary ", null)

    override fun search(word: String, lang: String): Cursor = database.rawQuery(
        "SELECT * FROM dictionary WHERE $lang LIKE \"%$word%\"", null
    )

    override fun addFavourite(id: Int) {
        database.execSQL("""
            UPDATE dictionary SET favourite=1 WHERE id =$id
        """.trimIndent())
    }

    override fun removeFavourite(id: Int) {
        database.execSQL("""
            UPDATE dictionary SET favourite=0 WHERE id =$id
            """.trimIndent())
    }

    @SuppressLint("Recycle")
    override fun getAllFavourite(): Cursor =
        database.rawQuery("SELECT * FROM dictionary WHERE favourite=1", null)
}