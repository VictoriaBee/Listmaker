package com.raywenderlich.listmaker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListItemViewHolder(itemView: View) :
RecyclerView.ViewHolder(itemView) {
    // When ViewHolder is instantiated, it knows to reference the TextView.
    val taskTextView = itemView.findViewById(R.id.textview_task) as TextView

}