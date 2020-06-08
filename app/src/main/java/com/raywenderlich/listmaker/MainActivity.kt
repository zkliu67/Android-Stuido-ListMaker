package com.raywenderlich.listmaker

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    ListSelectionFragment.OnListItemFragmentInteractionListener {

    // Creates a new instance of the fragment when the activity is created.
    private var listSelectionFragment: ListSelectionFragment = ListSelectionFragment.newInstance()
    private var fragmentContainer: FrameLayout? = null

    private var largeScreen = false // check the device screen size
    private var listFragment: ListDetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Attach the activity_main layout to this activity
        setSupportActionBar(findViewById(R.id.toolbar))

        listSelectionFragment =
            supportFragmentManager.findFragmentById(R.id.list_selection_fragment)
                    as ListSelectionFragment

        // Grabs the reference to the FrameLayout via the id.
        fragmentContainer = findViewById(R.id.fragment_container)

        // use a null check to see if using a larger screen.
        largeScreen = fragmentContainer != null
        //val fab = findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener {
            showCreateListDialog()
        }


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

    // In order to deal with the returned result.
    // Override this method for receiving the result of any activities it starts.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // When backing from the showListDetail activity.
        if (requestCode == LIST_DETAIL_REQUEST_CODE) {
            data?.let {
                listSelectionFragment.saveList(data.getParcelableExtra<TaskList>(INTENT_LIST_KEY))
            }
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
                listSelectionFragment.addList(list)

                dialog.dismiss()
                // list: a list of task.
                showListDetail(list)
        }
        // Construct and display the dialog.
        builder.create().show()
    }

    // Change the FloatingButton behavior when adding tasks to a list.
    private fun showCreateTaskDialog() {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) { dialog, _ ->
                val task = taskEditText.text.toString()
                listFragment?.addTask(task)
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        title = resources.getString(R.string.app_name)
        // Since it is not relied on two different activities.
        // Need to update the list manager to save the list.
        listFragment?.list?.let {
            listSelectionFragment.listDataManager.saveList(it)
        }
        // Remove the detail Fragment from the layout.
        listFragment?.let {
            supportFragmentManager
                .beginTransaction()
                .remove(it)
                .commit()
            listFragment = null
        }
        // Update the fab with creating dialog method.
        fab.setOnClickListener {
            showCreateListDialog()
        }
    }

    private fun showListDetail(list: TaskList) {
        // Display differently depends on the screen size.
        if (!largeScreen) {
            // Create an Intent and pass in the current activity and
            // the class of the activity to be shown
            val listDetailIntent = Intent(this, ListDetailActivity::class.java)
            // Extras are keys associated with values that provides intents to give
            // more information to the receiver about the action to be done.
            // INTENT_LIST_KEY: a string of the receiver of the intent to reference the list.
            // a list is to be displayed.
            listDetailIntent.putExtra(INTENT_LIST_KEY, list)

            // Starts the activity
            // and it may hear back from the activity when it finishes and removes itself from the screen.
            // In this case, we want to hear back the list passing to the activity.
            startActivityForResult(listDetailIntent, LIST_DETAIL_REQUEST_CODE)
        } else {
            // If using a large screen, display detailed fragment also.
            // Create a new instance of detail fragment.
            title = list.name
            listFragment = ListDetailFragment.newInstance(list)
            listFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it, getString(R.string.list_fragment_tag))
                    .addToBackStack(null)
                    .commit()
            }

            fab.setOnClickListener {
                showCreateTaskDialog()
            }
        }
    }

    override fun onListItemClicked(list: TaskList) {
        showListDetail(list)
    }

    // Define the key value for intent list.
    companion object {
        const val INTENT_LIST_KEY = "list"
        const val LIST_DETAIL_REQUEST_CODE = 123
    }
}