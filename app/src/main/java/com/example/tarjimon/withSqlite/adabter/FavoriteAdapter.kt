package com.example.tarjimon.withSqlite.adabter

import android.annotation.SuppressLint
import android.database.Cursor
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tarjimon.R
import com.example.tarjimon.withSqlite.model.WoardData

class FavoriteAdapter(
    private var cursor: Cursor, val lang: String
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    // Listeners
    private var clickListener: ((WoardData) -> Unit)? = null
    private var clickLikeListener: ((Int, Int) -> Unit)? = null

    @SuppressLint("Range")
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textWord: TextView = view.findViewById(R.id.text_word)
        private val imageLike: ImageView = view.findViewById(R.id.imgLike)

        init {
            imageLike.setOnClickListener {
                cursor.moveToPosition(absoluteAdapterPosition)
                val cursorFav = cursor.getInt(cursor.getColumnIndex("favourite"))
                val cursorId = cursor.getLong(cursor.getColumnIndex("id"))
                if (cursorFav == 1) {
                    imageLike.setImageResource(R.drawable.like)
                } else imageLike.setImageResource(R.drawable.no_like)
                clickLikeListener?.invoke(cursorId.toInt(), cursorFav)
            }

            textWord.setOnClickListener {
                cursor.moveToPosition(absoluteAdapterPosition)
                val english = cursor.getString(cursor.getColumnIndex("english"))
                val uzbek = cursor.getString(cursor.getColumnIndex("uzbek"))
                val word: String
                val translate: String

                if (lang == "english") {
                    word = english
                    translate = uzbek
                } else {
                    word = uzbek
                    translate = english
                }

                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val type = cursor.getString(cursor.getColumnIndex("type"))
                val like = cursor.getInt(cursor.getColumnIndex("favourite"))

                clickListener?.invoke(WoardData(id, word, type, translate, like))
            }
        }

        fun bind() {
            cursor.moveToPosition(absoluteAdapterPosition)

            val isFavorite: Int = cursor.getInt(6)
            val word = cursor.getString(if(lang == "english") 1 else 4)

            textWord.text = word

            if (isFavorite == 1) {
                imageLike.setImageResource(R.drawable.like)
            } else {
                imageLike.setImageResource(R.drawable.no_like)
            }
        }

    }

    fun setClickListener(listener: (WoardData) -> Unit) {
        clickListener = listener
    }

    fun setClickLikeListener(like: (Int, Int) -> Unit) {
        clickLikeListener = like
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCursor(newCursor: Cursor) {
//        isValid = false
//        cursor.unregisterDataSetObserver(notifyingDataSetObserver)
//        cursor.close()

//        newCursor.registerDataSetObserver(notifyingDataSetObserver)
        cursor = newCursor
//        isValid = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_item_word, parent, false))

    override fun getItemCount(): Int = cursor.count

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

}

/*
 {

    private var isValid = false





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



    fun onDestroy() {
        cursor.unregisterDataSetObserver(notifyingDataSetObserver)
        cursor.close()
    }
 */