package com.example.tarjimon.withSqlite.db

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.database.Cursor
import android.widget.EditText

class SharedPref private constructor() {


    companion object {
        private lateinit var pref: SharedPreferences
        private lateinit var editor: Editor

        private const val Name_Shared_Pref = "Fayl_nomi"
        private  const val LANGUAGE="language"

        private lateinit var instance: SharedPref

        fun getInstens(context: Context): SharedPref {
            pref = context.getSharedPreferences(Name_Shared_Pref, Context.MODE_PRIVATE)
            editor = pref.edit()

            if (!(::instance.isInitialized))
                instance = SharedPref()
            return instance

        }
    }
    var language:String
    set(value) = editor.putString(LANGUAGE,value).apply()
    get()= pref.getString(LANGUAGE,"").toString()



}