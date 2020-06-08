package com.raywenderlich.listmaker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListDetailActivity : AppCompatActivity() {

    lateinit var addTaskButton: FloatingActionButton
    lateinit var list: TaskList
    lateinit var listItemsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail)

        // get the list value from Parcel when entering this activity.
        list = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY)
        title = list.name
        // Initiate the RecyclerView for items list.
        listItemsRecyclerView = findViewById(R.id.list_items_recyclerView)
        listItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        listItemsRecyclerView.adapter = ListItemsRecyclerViewAdapter(list)

        // Add the reference to the button
        addTaskButton = findViewById(R.id.add_task_button)
        addTaskButton.setOnClickListener {
            showCreateTaskDialog()
        }
    }

    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_LIST_KEY, list)

        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)

        super.onBackPressed()
    }

    private fun showCreateTaskDialog() {
        // Create input field
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        // Create dialog for adding task.
        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) {
                // if add button is clicked.
                dialog, _ ->
                    // Add task to the shared Preference.
                    val task = taskEditText.text.toString()
                    list.hashedTasks.add(task)
                    // Update recyclerView for fast retrieve.
                    val recyclerAdapter = listItemsRecyclerView.adapter as ListItemsRecyclerViewAdapter
                    recyclerAdapter.notifyItemInserted(list.hashedTasks.size - 1)
                    dialog.dismiss()

            }
            .create()
            .show()
    }
}