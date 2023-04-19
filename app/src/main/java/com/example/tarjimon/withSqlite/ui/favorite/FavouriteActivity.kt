package com.example.tarjimon.withSqlite.ui.favorite

import android.app.Dialog
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tarjimon.R
import com.example.tarjimon.databinding.ActivityFavoritBinding
import com.example.tarjimon.withSqlite.adabter.FavoriteAdapter
import com.example.tarjimon.withSqlite.db.DBHelper
import com.example.tarjimon.withSqlite.db.DictionaryDao
import com.example.tarjimon.withSqlite.db.SharedPref
import com.example.tarjimon.withSqlite.model.WoardData

class FavouriteActivity : AppCompatActivity() {
    lateinit var binding: ActivityFavoritBinding

    private val databace: DictionaryDao by lazy { DBHelper.getInstence(this) }
    private val lang_1 by lazy { intent.getStringExtra("lang") }
    private lateinit var adapter: FavoriteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritBinding.inflate(layoutInflater)
        setContentView(binding.root) // Agar R.layout.

        val cursor = databace.getAllFavourite()
        Toast.makeText(this, "${cursor.count}", Toast.LENGTH_SHORT).show()
        adapter = FavoriteAdapter(cursor, lang_1!!)

        binding.apply {
            recyclerFav.layoutManager = LinearLayoutManager(this@FavouriteActivity)
            recyclerFav.adapter = adapter
            Log.d("TTT","count ${adapter.itemCount}")

            btnBack.setOnClickListener {
                finish()
            }

            if (cursor.count == 0) {
                noFav.visibility = View.VISIBLE
            } else noFav.visibility = View.INVISIBLE

            adapter.setClickLikeListener { id, like ->
                if (like == 1) {
                    databace.removeFavourite(id)
                } else databace.addFavourite(id)

                adapter.updateCursor(databace.getAllFavourite())

                if (databace.getAllFavourite().count == 0) {
                    noFav.visibility = View.VISIBLE
                } else noFav.visibility = View.INVISIBLE
            }


            adapter.setClickListener { item ->
                showItemDialog(item)
            }
        }
    }

    private fun showItemDialog(item: WoardData) {

        val dialog = Dialog(this@FavouriteActivity)

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

        val bTnClose: AppCompatButton = dialog.findViewById(R.id.btnClose)

        item.apply {
            word.text = this.word
            type.text = this.type
            translate.text = this.translate

        }
        bTnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.create()
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
//        adapter.onDestroy()
    }


}