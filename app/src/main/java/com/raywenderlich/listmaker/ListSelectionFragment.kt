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
    ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener {

    // define a private listener variable to hold a reference to the object
    // that implements the Fragment interface.
    private var listener: OnListItemFragmentInteractionListener? = null

    lateinit var listsRecyclerView: RecyclerView
    // Initiate the shared Preference as soon as the activity is created.
    // In the onAttach method.
    lateinit var listDataManager: ListDataManager


    // A lifecycle method run by a Fragment.
    // onAttach runs when the Fragment is first associated with an activity
    // to setup anything required before it is created.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // assign the context of the Fragment to the listener.
        // which is MainActivity in this situation.
        if (context is OnListItemFragmentInteractionListener) {
            listener = context
            listDataManager = ListDataManager(context)
        } else {
            throw RuntimeException("$context must implement OnListItemFragmentInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // a lifecycle method
    // runs when the Activity to which the Fragment is attached has finished onCreate()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val lists = listDataManager.readLists()
        view?.let {
            listsRecyclerView = it.findViewById(R.id.lists_recycleview)
            listsRecyclerView.layoutManager = LinearLayoutManager(activity)
            listsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(lists, this)
        }
    }
    // A lifecycle method.
    // the Fragment acquires layout it wants to present within the activity.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // a layout inflater is used to inflate the layout and pass it back to the Fragment.
        return inflater.inflate(R.layout.fragment_list_selection, container, false)
    }
    // A lifecycle method.
    // Called when a Fragment is no longer attached to the activity.
    // Either being destroyed or removed.
    // Set listener to null.
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    // An interface to listen for the clicking behavior with an item being clicked.
    // Implemented in the MainActivity.
    interface OnListItemFragmentInteractionListener {
        fun onListItemClicked(list: TaskList)
    }
    // Used by any project that wants to create a new instance of the Fragment.
    companion object {
        fun newInstance(): ListSelectionFragment {
            return ListSelectionFragment()
        }

    }

    override fun listItemClicked(list: TaskList) {
        listener?.onListItemClicked(list)
    }

    fun addList(list: TaskList) {
        listDataManager.saveList(list)
        val recyclerAdapter = listsRecyclerView.adapter as ListSelectionRecyclerViewAdapter
        recyclerAdapter.addList(list)
    }

    fun saveList(list: TaskList) {
        listDataManager.saveList(list)
        updateLists()
    }

    private fun updateLists() {
        val lists = listDataManager.readLists()
        listsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(lists, this)
    }
}