package com.raywenderlich.listmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListItemsRecyclerViewAdapter(var list: TaskList):
    RecyclerView.Adapter<ListItemsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemsViewHolder {
        val view = LayoutInflater.from(parent.context).
                    inflate(R.layout.task_view_holder, parent, false)
        return ListItemsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.hashedTasks.size
    }

    override fun onBindViewHolder(holder: ListItemsViewHolder, position: Int) {
        holder.taskTextView.text = list.hashedTasks[position]
    }
}