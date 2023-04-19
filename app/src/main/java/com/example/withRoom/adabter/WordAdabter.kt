package com.example.withRoom.adabter

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tarjimon.R
import com.example.withRoom.entities.BookEntity

class WordAdabter(private var cursor: Cursor) : RecyclerView.Adapter<WordAdabter.Holder>() {


    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_1, parent, false))
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
    }
}