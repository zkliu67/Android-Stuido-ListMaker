package com.raywenderlich.listmaker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListDetailFragment : Fragment() {
    lateinit var list: TaskList
    lateinit var listItemsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Grabs the list from the bundle passed in.
            list = it.getParcelable(MainActivity.INTENT_LIST_KEY)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Bind with the layout
        val view = inflater.inflate(R.layout.fragment_list_detail, container, false)
        view?.let {
            // Setup the RecyclerView via id in the layout
            listItemsRecyclerView = it.findViewById(R.id.list_items_recyclerView)
            // Initialize the RecyclerView Adapter and Layout.
            listItemsRecyclerView.adapter = ListItemsRecyclerViewAdapter(list)
            listItemsRecyclerView.layoutManager = LinearLayoutManager(context)
        }
        return view
    }

    companion object {
        private const val ARG_LIST = "list"

        // Instead of a bundle, it expects a list to create a new instance.
        fun newInstance(list: TaskList): ListDetailFragment {
            val fragment = ListDetailFragment()
            // Define ARG_LIST as a key to put into the Bundle object for the Fragment.
            // Also retrieve the TaskList from the Bundle object when the Fragment is created.
            val args = Bundle()
            args.putParcelable(ARG_LIST, list)
            fragment.arguments = args
            return fragment
        }
    }

    // Implement addTask method.
    fun addTask(item: String) {
        list.hashedTasks.add(item)

        val listRecyclerAdapter = listItemsRecyclerView.adapter as ListItemsRecyclerViewAdapter
        listRecyclerAdapter.list = list
        listRecyclerAdapter.notifyDataSetChanged()
    }
}