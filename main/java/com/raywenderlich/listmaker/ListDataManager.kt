package com.raywenderlich.listmaker

import android.content.Context
import androidx.preference.PreferenceManager

class ListDataManager(private val context: Context) {
    fun saveList(list: TaskList?)  {
        // 1 - References defaultSharedPreferences to allow you to write
        //      key-value pairs to SharedPreferences.
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context).edit()

        // 2 - Writes TaskList as set of Strings and converts the tasks to a HashSet
        //      which SharedPreferences uses as a value to save.
        //      This ensures unique values in the list.
        sharedPreferences.putStringSet(list?.name, list?.tasks!!.toHashSet())

        // 3 - Instructs to apply the changes in file.
        sharedPreferences.apply()
    }

    fun readLists(): ArrayList<TaskList>  {
        // 1 - References to default SharedPreferences file.
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        // 2 - Calling sharedPreferences to get contents of file as Map.
        val sharedPreferenceContents = sharedPreferences.all
        // 3 - Create empty ArrayList of TaskList to store lists retrieved from SharedPreferences.
        val taskLists = ArrayList<TaskList>()

        // 4 - For loop to iterate over items in the Map.
        for (taskList in sharedPreferenceContents)  {
            // Converts list into HashSet.
            val itemsHashSet = ArrayList(taskList.value as HashSet<String>)
            val list = TaskList(taskList.key, itemsHashSet)
            // 5 - Adds TaskList into the ArrayList.
            taskLists.add(list)
        }
        // 6 - Returns contents of taskLists.
        return taskLists
    }
}