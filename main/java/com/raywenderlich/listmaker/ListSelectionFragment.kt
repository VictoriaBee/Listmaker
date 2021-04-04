package com.raywenderlich.listmaker

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.RuntimeException

class ListSelectionFragment : Fragment(),
ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener  {
    // Creates a new ListDataManager when Activity is created.
    lateinit var listDataManager: ListDataManager
    lateinit var listsRecyclerView: RecyclerView

    // 1 - To hold reference to object implementing the Fragment interface.
    private var listener: OnListItemFragmentInteractionListener? = null

    // 2 - A lifecycle method run by a Fragment.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Assigning context of Fragment to listener if it implements the interface.
        if (context is OnListItemFragmentInteractionListener) {
            listener = context
            listDataManager = ListDataManager(context)
        }   else  {
            throw RuntimeException("$context must implement" +
                    "OnListItemFragmentInteractionListener")
        }
    }

    // 3 - Lifestyle method; used when  a Fragment is in the process of being created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // 4 - Lifestyle method; Fragment acquires layout to present within Activity.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_selection, container, false)
    }

    // 5 - Final lifestyle method; called when Fragement is no longer attached to an Activity
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListItemFragmentInteractionListener {
        fun onListItemClicked(list: TaskList)
    }

    // 6 - Used by any object wanting to create a new instance of the Fragment.
    companion object {
        fun newInstance(): ListSelectionFragment  {
            return ListSelectionFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 1 - Gets list of TaskLists from listDataManager to use.
        val lists = listDataManager.readLists()
        view?.let  {
            listsRecyclerView = it.findViewById<RecyclerView>(R.id.lists_recyclerview)
            // 2 - Arranging item in linear format.
            listsRecyclerView.layoutManager = LinearLayoutManager(activity)
            // 3
            listsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(lists, this)
        }
    }

    // Uses listener to inform the Activity that it's received the list
    // and to start new Activity to show list.
    override fun listItemClicked(list: TaskList) {
        listener?.onListItemClicked(list)
    }

    fun addList(list : TaskList)  {
        listDataManager.saveList(list)

        val recyclerAdapter = listsRecyclerView.adapter as
                ListSelectionRecyclerViewAdapter
        recyclerAdapter.addList(list)
    }

    fun saveList(list: TaskList?)  {
        listDataManager.saveList(list)
        updateLists()
    }

    private fun updateLists() {
        val lists = listDataManager.readLists()
        listsRecyclerView.adapter =
            ListSelectionRecyclerViewAdapter(lists, this)
    }
}