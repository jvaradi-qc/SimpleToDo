package com.johnv.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.johnv.simpletodo.R

class EditActivity : AppCompatActivity() {
    lateinit var etItem: EditText
    lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        etItem = findViewById(R.id.etItem)
        btnSave = findViewById(R.id.btnSave)
        supportActionBar!!.title = "Edit item"

        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT))
        btnSave.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // create an intent which will contain results
                val intent = Intent()

                // pass the data (results of editing)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT,etItem.getText().toString())
                intent.putExtra(MainActivity.KEY_ITEM_POSITION,
                    getIntent().getExtras()?.getInt(MainActivity.KEY_ITEM_POSITION)
                )

                // set the result of the intent
                setResult(RESULT_OK, intent)

                // finish activity, close the screen and go back
                finish()
            }

        })
    }
}