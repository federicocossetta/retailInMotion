package com.fcossetta.retail.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fcossetta.retail.R
import com.fcossetta.retail.dao.Tram

class TramHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)) {
    private var destinationTextView: TextView? = null
    private var forecast: TextView? = null
    private var from: TextView? = null


    init {
        destinationTextView = itemView.findViewById(R.id.destination)
        forecast = itemView.findViewById(R.id.duemins)
        from = itemView.findViewById(R.id.from)
    }

    fun bind(tram: Tram, fromStation: String) {
        destinationTextView?.text = tram.destination
        forecast?.text = tram.duemins
        from?.text = fromStation
    }

}