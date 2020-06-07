package com.raywenderlich.listmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.recyclerview.widget.RecyclerView

// The customized adapter should extend the built-in abstract class
// With implementing all needed methods.
class ListSelectionRecyclerViewAdapter(private val lists: ArrayList<TaskList>,
    val clickListener: ListSelectionRecyclerViewClickListener):
    RecyclerView.Adapter<ListSelectionViewHolder>() {

    // Create a new interface: listen to the click on a item.
    interface ListSelectionRecyclerViewClickListener {
        fun listItemClicked(list: TaskList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ListSelectionViewHolder {
        // Create a layout programmatically with LayoutInflater object.
        // It uses the parent context of the Adapter to create itself.
        // and attempts to inflate the customized layout by passing the name and parent view group.
        // Boolean value indicates whether the view should attach to the parent.
        // Set the boolean always to false in RecyclerView, it parent here is already attached.
        val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_selection_view_holder, parent, false)
        return ListSelectionViewHolder(view)
    }

    // Determine how many items the RecyclerView has.
    // Make sure the size of title == the size of RecyclerView
    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ListSelectionViewHolder, position: Int) {
        holder.listPosition.text = (position + 1).toString()
        holder.listTitle.text = lists[position].name
        // Add an onClickListener to the view of itemHolder
        holder.itemView.setOnClickListener {
            clickListener.listItemClicked(lists[position])
        }
    }

    fun addList(list: TaskList) {
        lists.add(list)
        notifyItemInserted(lists.size - 1)
    }
}