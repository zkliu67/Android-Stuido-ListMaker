package com.raywenderlich.listmaker

import android.content.Context
import androidx.preference.PreferenceManager

// To manage the lists
class ListDataManager(private val context: Context) {
    fun saveList(list: TaskList) {
        // Return a editor reference that allows to write key-pair to the sharedPreference.
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
        // Write taskList to sharedPreference as a list.
        sharedPreferences.putStringSet(list.name, list.hashedTasks.toHashSet())
        // apply the change.
        sharedPreferences.apply()
    }

    fun readLists(): ArrayList<TaskList> {

        // Grab the default sharedPreference file, with read only.
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        // .all get all the contents of the sharedPreference file as a Map.
        val sharedPreferenceContents = sharedPreferences.all

        // Create an empty list for storing all the list items.
        val taskLists = ArrayList<TaskList>()
        // Iterate over the Map, store the value of the object into a HashSet,
        // This would be convert back when retrieving them.
        // Add hashed list item to the TaskLists.
        for (taskList in sharedPreferenceContents) {
            val itemsHashSet = ArrayList(taskList.value as HashSet<String>)
            val list = TaskList(taskList.key, itemsHashSet)
            taskLists.add(list)
        }
        return taskLists
    }
}