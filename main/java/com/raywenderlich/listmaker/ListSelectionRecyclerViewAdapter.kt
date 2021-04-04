package com.raywenderlich.listmaker

import android.app.LauncherActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Figure out how to fix this.
class ListSelectionRecyclerViewAdapter(
    val lists: ArrayList<TaskList>, val clickListener:
    ListSelectionRecyclerViewClickListener) :
    RecyclerView.Adapter<ListSelectionViewHolder>()  {

    // Listens for tap on existing list in RecyclerView.
    interface ListSelectionRecyclerViewClickListener  {
        fun listItemClicked(list: TaskList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSelectionViewHolder {
        // 1 - Uses LayoutInflater to create layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_selection_view_holder,
            parent,
            false)

        // 2 - Returning ViewHolder
        return ListSelectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListSelectionViewHolder, position: Int) {
        if (holder != null) {
            holder.listPosition.text = (position + 1).toString()
            holder.listTitle.text = lists.get(position).name
            holder.itemView.setOnClickListener(
                {
                    clickListener.listItemClicked(lists.get(position))
                })
        }
    }

    // Return the size of listTitles array.
    override fun getItemCount(): Int {
        return lists.size
    }

    fun addList(list: TaskList)  {
        // 1 - Updates the ArrayList with new TaskList.
        lists.add(list)

        // 2 - Informs Adapter that you updated the data source.
        notifyItemInserted(lists.size - 1)
    }

}