package com.example.tarjimon.withSqlite.adabter

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.DataSetObserver
import android.provider.MediaStore.Images
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.getColumnIndex
import com.example.tarjimon.R
import com.example.tarjimon.withSqlite.db.DBHelper
import com.example.tarjimon.withSqlite.model.WoardData
import com.example.withRoom.data.WordDatabase
import kotlinx.coroutines.withTimeoutOrNull

class DicitionaryAdabter(var cursor: Cursor) :
    RecyclerView.Adapter<DicitionaryAdabter.VIewHolder>() {

    private var lang = "uzbek"
    private var isValid = false

    @SuppressLint("Range")
    inner class VIewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textWord: TextView = view.findViewById(R.id.text_word)
        private val imageLike: ImageView = view.findViewById(R.id.imgLike)

        init {
            imageLike.setOnClickListener {

                cursor.moveToPosition(absoluteAdapterPosition)
                val cursorFav = cursor.getInt(cursor.getColumnIndex("favourite"))
                val cursorId = cursor.getInt(cursor.getColumnIndex("id"))
                clickLikeListener?.invoke(cursorId, cursorFav)
                if (cursorFav == 1) {
                    imageLike.setImageResource(R.drawable.like)
                } else imageLike.setImageResource(R.drawable.no_like)


            }

            textWord.setOnClickListener {
                cursor.moveToPosition(absoluteAdapterPosition)
                val english = cursor.getString(cursor.getColumnIndex("english"))
                val uzbek = cursor.getString(cursor.getColumnIndex("uzbek"))
                val word: String
                val translater: String

                if (lang == "english") {
                    word = english
                    translater = uzbek
                } else {
                    word = uzbek
                    translater = english
                }
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val type = cursor.getString(cursor.getColumnIndex("type"))
                val like = cursor.getInt(cursor.getColumnIndex("favourite"))
                val transcript = cursor.getInt(cursor.getColumnIndex("transcript"))

                clickListener?.invoke(WoardData(id, word, type, translater, like))
            }

        }

        // qachon islayadi  edi ?
        fun bind() {
            cursor.moveToPosition(bindingAdapterPosition)
            val isFavorit = cursor.getInt(cursor.getColumnIndex("favourite"))
            val word = if (lang == "english") cursor.getString(cursor.getColumnIndex("english"))
            else cursor.getString(cursor.getColumnIndex("uzbek"))

            textWord.text = word

            if (isFavorit == 1) {
                imageLike.setImageResource(R.drawable.like)
            } else {
                imageLike.setImageResource(R.drawable.no_like)
            }

        }


    }

    private var clickListener: ((WoardData) -> Unit)? = null
    fun setClickLikeListener(like: (Int, Int) -> Unit) {
        clickLikeListener = like
    }

    private var clickLikeListener: ((Int, Int) -> Unit)? = null
    fun setClickListener(listener: (WoardData) -> Unit) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VIewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_item_word, parent, false)
        return VIewHolder(view)
    }

    override fun getItemCount(): Int = if (isValid) cursor.count else 0

    override fun onBindViewHolder(holder: VIewHolder, position: Int) {
        holder.bind()
    }

    fun setLang(lang: String) {
        this.lang = lang
    }

    // chunarsiz
    private val notifyingDataSetObserver = object : DataSetObserver() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onChanged() {
            isValid = true
            notifyDataSetChanged()
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onInvalidated() {
            isValid = false
            notifyDataSetChanged()
        }
    }

    init {
        cursor.registerDataSetObserver(notifyingDataSetObserver)
        isValid = true
    }

    // tushunarsiz
    @SuppressLint("NotifyDataSetChanged")
    fun updateCursor(newCursor: Cursor) {
        isValid = false
        cursor.unregisterDataSetObserver(notifyingDataSetObserver)
        cursor.close()

        newCursor.registerDataSetObserver(notifyingDataSetObserver)
        cursor = newCursor
        isValid = true
        notifyDataSetChanged()

    }


}
