package com.example.tarjimon.withSqlite.ui.main

import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tarjimon.R
import com.example.tarjimon.databinding.ActivtyMainBinding
import com.example.tarjimon.withSqlite.adabter.DicitionaryAdabter
import com.example.tarjimon.withSqlite.db.DBHelper
import com.example.tarjimon.withSqlite.db.DictionaryDao
import com.example.tarjimon.withSqlite.db.SharedPref
import com.example.tarjimon.withSqlite.model.WoardData
import com.example.tarjimon.withSqlite.ui.favorite.FavouriteActivity

class MainActivity : AppCompatActivity() {
    private val database: DictionaryDao by lazy { DBHelper.getInstence(applicationContext) }
    private val sharedPref by lazy { SharedPref.getInstens(applicationContext) }
    private val adapter by lazy { DicitionaryAdabter(database.getAll()) }
    private lateinit var cursor: Cursor
//    private var tts: TextToSpeech? = null

    private lateinit var binding: ActivtyMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivtyMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            adapter.setLang(sharedPref.language)
            resacl.adapter = adapter
            resacl.layoutManager = LinearLayoutManager(this@MainActivity)

            if (sharedPref.language == "english") {
                txtEdit.hint = "Search - Qidiruv"
                language1.text = "Eng-Uzb Dictionary"
            } else {
                txtEdit.hint = "Qidiruv - Search"
            }

            transitionFavorit.setOnClickListener {
                val intent = Intent(this@MainActivity, FavouriteActivity::class.java)
                intent.putExtra("lang", sharedPref.language)
                startActivity(intent)

                // startActivity(Intent(this@MainActivity, FavouriteActivity::class.java))
            }

            txtEdit.doOnTextChanged { text, start, before, count ->
                if (!(text.toString().contains("\""))) {
                    if (text.toString().isNotBlank()) {
                        cursor = database.search(text.toString(), sharedPref.language)
                        if (cursor.count == 0) binding.notFound.visibility = View.VISIBLE
                        else adapter.updateCursor(cursor)
                    } else {
                        adapter.updateCursor(database.getAll())
                    }
                }
            }

            change.setOnClickListener {
                if (sharedPref.language == "english") {
                    sharedPref.language = "uzbek"
                    language1.text = "O'zb"
                    language2.text = "Eng"
                    Toast.makeText(this@MainActivity, "Uzbek-English", Toast.LENGTH_SHORT).show()
                } else {
                    sharedPref.language = "english"
                    language1.text = "Eng"
                    language2.text = "O'zb"
                    Toast.makeText(this@MainActivity, "English-Uzbek", Toast.LENGTH_SHORT).show()
                }
                adapter.setLang(sharedPref.language)
                adapter.updateCursor(database.getAll())
                resacl.adapter = adapter
            }
        }
        adapter.setClickListener {
            Toast.makeText(this, "bosildi ", Toast.LENGTH_SHORT).show()
            showDialog(it)
        }

        adapter.setClickLikeListener { id, like ->
            if (like == 1) database.removeFavourite(id)
            else database.addFavourite(id)

            if(!(::cursor.isInitialized)){
                adapter.updateCursor(database.getAll())
            }else{
                adapter.updateCursor(cursor)
            }
        }
    }

    private fun showDialog(item: WoardData) {
        val dialog = Dialog(this)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        val window = dialog.window
        window!!.attributes = lp

        dialog.setContentView(R.layout.item_1)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val word: AppCompatTextView = dialog.findViewById(R.id.txt_dilaog_Word)
        val type: AppCompatTextView = dialog.findViewById(R.id.txtType)
        val translate: AppCompatTextView = dialog.findViewById(R.id.txtTrans)

        val btnClose: AppCompatButton = dialog.findViewById(R.id.btnClose)

        item.apply {
            word.text = this.word
            type.text = this.type
            translate.text = this.translate
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.create()
        dialog.show()
    }

    override fun onPause() {
        super.onPause()
        binding.txtEdit.text.clear()
    }

    override fun onResume() {
        super.onResume()
        adapter.updateCursor(database.getAll())
    }
}