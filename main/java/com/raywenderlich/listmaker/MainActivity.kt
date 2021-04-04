package com.raywenderlich.listmaker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity(),
ListSelectionFragment.OnListItemFragmentInteractionListener {
    // Boolean to track if larger Layout is in use.
    private var largeScreen = false
    private var listFragment : ListDetailFragment? = null

    // To hold reference to the FrameLayout.
    private var fragmentContainer: FrameLayout? = null

    // Fragment instance is created when the Activity is created.
    private var listSelectionFragment: ListSelectionFragment =
        ListSelectionFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        listSelectionFragment = supportFragmentManager.findFragmentById(R.id.list_selection_fragment)
                as ListSelectionFragment
        fragmentContainer = findViewById(R.id.fragment_container)
        largeScreen = (fragmentContainer != null)

        fab.setOnClickListener {
            showCreateListDialoag()
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

    private fun showCreateListDialoag() {
        // 1 - Retrieves the string for use in Dialog.
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)

        // 2 - Creates Dialog
        val builder = AlertDialog.Builder(this)
        // Allows user to input text for list.
        val listTitleEditText = EditText(this)
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT

        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)

        // 3 - Positive button tells Dialog that something should be happening.
        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            val list = TaskList(listTitleEditText.text.toString())
            listSelectionFragment.addList(list)

            dialog.dismiss()
            showListDetail(list)  // When create new list, the app passes that list to new Activity.
        }

        // 4 - Creates and displays Dialog.
        builder.create().show()
    }

    private fun showListDetail(list: TaskList)  {
        if (!largeScreen)  {
            val listDetailIntent = Intent(this, ListDetailActivity::class.java)
            listDetailIntent.putExtra(INTENT_LIST_KEY, list)

            startActivityForResult(listDetailIntent, LIST_DETAIL_REQUEST_CODE)
        }   else  {
            title = list.name

            listFragment = ListDetailFragment.newInstance(list)
            listFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it,
                        getString(R.string.list_fragment_tag))
                    .addToBackStack(null)
                    .commit()
            }

            fab.setOnClickListener  {
                showCreateListDialoag()
            }
        }
    }

    // Shows the detail of the TaskList in another Activity.
    override fun onListItemClicked(list: TaskList) {
        showListDetail(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 1 - Checks request code is the same code expecting to get back.
        //      Checks that resultCode is RESULT_OK in case user cancels action.
        if (requestCode == LIST_DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK)  {
            // 2 - If ok, unwraps data Intent passed in.
            data?.let  {
                // 3 - If confirms data is there, saves list to the list data manager
                //     and then call updateLists().
                listSelectionFragment.saveList(
                    data.getParcelableExtra(INTENT_LIST_KEY) as TaskList?)
            }
        }
    }

    // Used by Intent to refer to list whenever it needs to pass one to new Activity.
    companion object {
        const val INTENT_LIST_KEY = "list"
        const val LIST_DETAIL_REQUEST_CODE = 123
    }

    private fun showCreateTaskDialog()  {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task)  { dialog, _ ->
                val task = taskEditText.text.toString()
                listFragment?.addTask(task)
                dialog.dismiss()
            }
            .create()
            .show()
    }

    // So Activity knows how to deal with the back button being used when using Fragments.
    override fun onBackPressed() {
        super.onBackPressed()

        // 1 - Activity will show name of the app when back button is pressed.
        title = resources.getString(R.string.app_name)

        // 2 - Tells ListDataManager to save the list.
        listFragment?.list?.let {
            listSelectionFragment.listDataManager.saveList(it)
        }

        // 3 - Removes the detail Fragment from the Layouts.
        //      ?. let - Resets between the check and the Fragment manager transaction.
        listFragment?.let {
            supportFragmentManager
                .beginTransaction()
                .remove(it)
                .commit()
            listFragment = null
        }

        // 4 - Updates FAB to create lists again.
        fab.setOnClickListener  {
            showCreateListDialoag()
        }
    }
}
