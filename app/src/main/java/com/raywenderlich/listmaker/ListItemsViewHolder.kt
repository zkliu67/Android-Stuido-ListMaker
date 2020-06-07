package com.raywenderlich.listmaker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListItemsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val taskTextView = itemView.findViewById(R.id.textView_task) as TextView
}