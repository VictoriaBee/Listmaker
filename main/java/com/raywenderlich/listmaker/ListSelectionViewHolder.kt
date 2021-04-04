package com.raywenderlich.listmaker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListSelectionViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

        // Referencing the TextViews of LinearLayout.
        val listPosition = itemView.findViewById(R.id.itemNumber) as TextView

        var listTitle = itemView.findViewById(R.id.itemString) as TextView
}