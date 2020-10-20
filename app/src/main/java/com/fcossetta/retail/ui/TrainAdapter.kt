package com.fcossetta.retail.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fcossetta.retail.dao.Tram
import java.util.ArrayList


class TrainAdapter(private val list: ArrayList<Tram>) : RecyclerView.Adapter<TramHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TramHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TramHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: TramHolder, position: Int) {
        val tram: Tram = list[position]
        holder.bind(tram)
    }

    override fun getItemCount(): Int = list.size
}