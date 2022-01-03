package com.johnv.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    companion object {
        val KEY_ITEM_TEXT = "item_text"
        val KEY_ITEM_POSITION = "item_position"
        val EDIT_TEXT_CODE = 20
    }

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                listOfTasks.removeAt(position)

                adapter.notifyDataSetChanged()
                saveItems()
            }

        }

        val onClickListener = object : TaskItemAdapter.OnClickListener {
            override fun onItemClicked(position: Int) {
                Log.i("MainActivity", "Single click at position " + position)
                // create the new activity
                val i = Intent(this@MainActivity,EditActivity::class.java)

                // pass the data being edited
                i.putExtra(KEY_ITEM_TEXT,listOfTasks.get(position))
                i.putExtra(KEY_ITEM_POSITION,position)
                // display the activity
                startActivityForResult(i,EDIT_TEXT_CODE)
            }
        }

        loadItems()
        // 1. detect when the user clicks on the add button

//        findViewById<Button>(R.id.button).setOnClickListener {
//            Log.i("John", "User clicked on button")
//        }


        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener, onClickListener)

        // Attach the  adapter to the recyclerView to populate items
        recyclerView.adapter = adapter

        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // set up the button and input field so that the user can enter a task

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //Get a reference to button
        //and then set onclicklistener
        findViewById<Button>(R.id.button).setOnClickListener {
            // 1. Grab the text the user has inputted into @id/editTaskField
            val userInputtedTask = inputTextField.text.toString()

            // 2.  Add the string to our list of tasks: listofTasks
            listOfTasks.add(userInputtedTask)

            //Notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // 3.  Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            // Retrieve the updated text value
            val itemText = data?.getStringExtra(KEY_ITEM_TEXT)
            // extract the original position of the edited item from the position key
            val position = data?.getExtras()?.getInt(KEY_ITEM_POSITION)

            // update the model at the right position with new item text
            if (itemText != null) {
                if (position != null) {
                    listOfTasks.set(position,itemText)
                }
            }
            // notify the adapter
            if (position != null) {
                adapter.notifyItemChanged(position)
            }
            // persist the changes
            saveItems()
            Toast.makeText(applicationContext,"Item updated successfully", Toast.LENGTH_SHORT).show()

        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult")
        }

    }
    // Save the data that the user has inputted
    // data gets saved by writing and reading from a file

    // Create a method to get the file we need
    fun getDataFile() : File {

        // Every line is going to represeent a specific task in our list of tasks
        return File(filesDir, "taskList.txt")
    }
    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }


    // Save items by writing them into our data file
    fun saveItems() {

        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}