package com.raywenderlich.listmaker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListDetailActivity : AppCompatActivity() {
    // This fix made the errors go away!
    var list: TaskList? = null
    // Referencing the RecyclerView.
    lateinit var listItemsRecyclerView : RecyclerView
    // Referencing the FAB.
    lateinit var addTaskButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail)

        // 1 - Using key from MainActivity to reference list.
        list = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY) as TaskList?
        // 2 - Assigning the title of Activity to name of the list
        //     so user knows what they're viewing.
        title = list?.name

        // 1 - Assigning RecyclerView to local variable.
        listItemsRecyclerView = findViewById(R.id.list_items_recyclerview)
        // 2 - Assigning RecyclerView an Adapter and passing in list.
        listItemsRecyclerView.adapter = ListItemsRecyclerViewAdapter(list)
        // 3 - Assigning it to LayoutManager to show linear layout.
        listItemsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Sets up the FAB.
        addTaskButton = findViewById(R.id.add_task_button)
        addTaskButton.setOnClickListener {
            showCreateTaskDialog()
        }
    }

    private fun showCreateTaskDialog() {
        // 1 - To receive text input from the user.
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        // 2 - Creating AlertDialogBuilder.
        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task)  { dialog, _ ->
                // 3 - In Positive Button's click listener - grabs text input to create a task.
                val task = taskEditText.text.toString()
                list?.tasks!!.add(task)

                // 4 - Notifies the LIstItemsRecyclerViewAdapter that a new item was added.
                val recyclerAdapter = listItemsRecyclerView.adapter
                    as ListItemsRecyclerViewAdapter

        recyclerAdapter.notifyItemInserted(list?.tasks!!.size) // - 1
                // 5 - Once RecyclerAdapter updates, closes dialog by dismissing it.
                dialog.dismiss()
              }
            // 6 - Outside click listener, continues to use method chaining to create.
            .create()
            .show()
    }

    // Bundle up the code to put it into an Intent.
    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_LIST_KEY, list)

        val intent = Intent()
        intent.putExtras(bundle)
        // Setting result to RESULT_OK and passing Intent,
        //      informing the Activity that everything went great.
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }
}