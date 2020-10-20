package com.fcossetta.retail.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fcossetta.retail.dao.Tram
import java.util.ArrayList


class TrainAdapter(private val list: ArrayList<Tram>, private val from: String?) :
    RecyclerView.Adapter<TramHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TramHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TramHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: TramHolder, position: Int) {
        val tram: Tram = list[position]
        if (from != null) {
            holder.bind(tram, from)
        }
    }

    override fun getItemCount(): Int = list.size
}