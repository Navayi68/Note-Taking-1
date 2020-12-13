package com.example.notetaking

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.notetaking.DataProcess.NoteInformationProcess
import com.example.notetaking.databinding.ActivityCreateNoteBinding
import java.text.SimpleDateFormat
import java.util.*


class CreateNote : AppCompatActivity() {

    private val noteInformationProcess: NoteInformationProcess = NoteInformationProcess()

    lateinit var createNoteBinding: ActivityCreateNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNoteBinding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(createNoteBinding.root)

        //List For Add The Spinner
        val groups = arrayOf(
            "Uncategorized",
            "Office",
            "Personal",
            "Family affair",
            "Study",
            "Shopping",
            "Health",
            "Work"
        )

        //Add The Spinner
        val adapterSpinner = ArrayAdapter(
            this@CreateNote, android.R.layout.simple_spinner_dropdown_item, groups
        )


        createNoteBinding.spinner.adapter = adapterSpinner


        val sharedPreferences: SharedPreferences =
            application.getSharedPreferences("id", Context.MODE_PRIVATE)

        val idExtra = intent.getStringExtra("idExtra")

        if (intent.hasExtra("idExtra")) {


            val readDataForExtra = noteInformationProcess.searchById(this, idExtra!!.toString())
            createNoteBinding.editTextTitle.setText(readDataForExtra[0].title)
            createNoteBinding.editTextMessage.setText(readDataForExtra[0].message)
            val categorizedExtra = readDataForExtra[0].categorizedData

            var groupId: Int = 0

            when (categorizedExtra) {
                "Uncategorized" -> groupId = 0
                "Office" -> groupId = 1
                "Personal" -> groupId = 2
                "Family affair" -> groupId = 3
                "Study" -> groupId = 4
                "Shopping" -> groupId = 5
                "Health" -> groupId = 6
                "Work" -> groupId = 7
            }
            createNoteBinding.spinner.setSelection(groupId)

        }

        createNoteBinding.buttonSave.setOnClickListener {

            //Date
            val dateTime = SimpleDateFormat("dd/M/yyyy  hh:mm:ss")

            val currentDate = dateTime.format(Date())

            val titleNote = createNoteBinding.editTextTitle.text.toString()

            val messageNote = createNoteBinding.editTextMessage.text.toString()

            val categorized = createNoteBinding.spinner.selectedItem.toString()

            if (titleNote.isNotEmpty() || messageNote.isNotEmpty()) {


                val idNote = System.currentTimeMillis().toString()

                if (intent.hasExtra("idExtra")) {

                    noteInformationProcess.saveNote(
                        applicationContext,
                        idExtra!!,
                        titleNote,
                        messageNote,
                        categorized,
                        currentDate
                    )

                } else {

                    noteInformationProcess.saveNote(
                        applicationContext,
                        idNote,
                        titleNote,
                        messageNote,
                        categorized,
                        currentDate
                    )
                }

                Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show()

                finish()

            } else {

                finish()

            }
        }
    }
}
