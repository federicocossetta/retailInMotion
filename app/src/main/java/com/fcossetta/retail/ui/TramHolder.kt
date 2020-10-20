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
    private var dueminsTextView: TextView? = null


    init {
        destinationTextView = itemView.findViewById(R.id.destination)
        dueminsTextView = itemView.findViewById(R.id.duemins)
    }

    fun bind(tram: Tram) {
        destinationTextView?.text = tram.destionation
        dueminsTextView?.text = tram.duemins
    }

}