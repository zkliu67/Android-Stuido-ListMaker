package com.raywenderlich.listmaker

import android.os.Bundle
import android.text.InputType
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var listsRecyclerView: RecyclerView
    // Initiate the shared Preference as soon as the activity is created.
    val listDataManager: ListDataManager = ListDataManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Attach the activity_main layout to this activity
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            showCreateListDialog()
        }

        // Return the lists stored in the sharedPreference
        val lists = listDataManager.readLists()
        // Initialize the listRecyclerView
        listsRecyclerView = findViewById(R.id.lists_recycleview)
        // Determine the layout for the list view.
        // Passing the activity for the layout manager to access the context.
        listsRecyclerView.layoutManager = LinearLayoutManager(this)
        // Bind an adapter to the RecyclerView, by creating a customized adapter.
        listsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(lists)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showCreateListDialog() {
        // Retrieve the strings defined in String.xml for using in Dialog.
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)

        // Construct the dialog.
        val builder = AlertDialog.Builder(this)
        // Input field for entering list name
        val listTitleEditText = EditText(this)
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT

        // Set the title of the dialog, and pass the context view to the dialog.
        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)

        // Add a positive button to the dialog, telling the dialog a positive action
        // is occurred and should respond to it.
        // May also have a negative button.
        builder.setPositiveButton(positiveButtonTitle) {
            dialog, _ ->
                val list = TaskList(listTitleEditText.text.toString())
                listDataManager.saveList(list)

                val recyclerAdapter = listsRecyclerView.adapter as
                        ListSelectionRecyclerViewAdapter
                recyclerAdapter.addList(list)
                dialog.dismiss()
        }
        // Construct and display the dialog.
        builder.create().show()
    }
}