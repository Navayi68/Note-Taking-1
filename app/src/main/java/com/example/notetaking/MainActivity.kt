package com.example.notetaking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notetaking.DataProcess.InterfaceLoadDataProcess
import com.example.notetaking.DataProcess.NoteInformationProcess
import com.example.notetaking.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PassDataForProcess, InterfaceLoadDataProcess {


    private val noteInformationProcess: NoteInformationProcess = NoteInformationProcess()

    lateinit var noteAdapter: NoteAdapter

    lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)





        activityMainBinding.TextSearch.addTextChangedListener {

            val search =
                noteInformationProcess.searchItem(
                    this,
                    activityMainBinding.TextSearch.text.toString()
                )

            if (search.isEmpty()) {

                noteAdapter.allNoteArrayList.clear()

                noteAdapter.notifyDataSetChanged()

            } else {

                noteAdapter.allNoteArrayList.clear()

                noteAdapter.allNoteArrayList.addAll(
                    noteInformationProcess.searchItem(
                        this,
                        activityMainBinding.TextSearch.text.toString()
                    )
                )

                noteAdapter.notifyDataSetChanged()

                noteAdapter.deleteAndEditArrayList.clear()
                activityMainBinding.deleteNoteTaskBar.visibility=View.INVISIBLE
                activityMainBinding.editNoteTaskBar.visibility=View.INVISIBLE

            }

        }

        activityMainBinding.addNote.setOnClickListener {

            startActivity(Intent(applicationContext, CreateNote::class.java))

        }

        noteAdapter = NoteAdapter(this@MainActivity, this@MainActivity)

        activityMainBinding.recyclerView.layoutManager = LinearLayoutManager(
            applicationContext, RecyclerView.VERTICAL, false
        )

        activityMainBinding.recyclerView.adapter = noteAdapter

    }

    override fun onResume() {
        super.onResume()




        noteInformationProcess.setupAdapter(this@MainActivity, this@MainActivity)

        noteAdapter.allNoteArrayList.clear()
        activityMainBinding.addNote.visibility=View.VISIBLE
        activityMainBinding.editNoteTaskBar.visibility=View.INVISIBLE
        activityMainBinding.deleteNoteTaskBar.visibility=View.INVISIBLE
        noteAdapter.deleteAndEditArrayList.clear()
        activityMainBinding.TextSearch.setText("")

        noteAdapter.notifyDataSetChanged()

    }


    override fun deleteItem(key: String, position: Int,constraintLayout: ConstraintLayout, tickItem: ImageView) {

        activityMainBinding.addNote.visibility=View.INVISIBLE

        Toast.makeText(this, position.toString(), Toast.LENGTH_SHORT).show()

        activityMainBinding.deleteNoteTaskBar.visibility = View.VISIBLE

        activityMainBinding.editNoteTaskBar.visibility = View.VISIBLE

        activityMainBinding.deleteNoteTaskBar.setOnClickListener {



            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)

            alertDialog.apply {

                setTitle("Delete")

                setMessage("Ae sure Delete")

                setIcon(android.R.drawable.ic_delete)

                setCancelable(false)

                setPositiveButton("Yes") { _, _ ->

                    noteAdapter.deleteAndEditArrayList.sortByDescending {
                        it.position
                    }
                    activityMainBinding.addNote.visibility=View.VISIBLE

                    activityMainBinding.editNoteTaskBar.visibility = View.INVISIBLE

                    activityMainBinding.deleteNoteTaskBar.visibility = View.INVISIBLE

                    //constraintLayout.setBackgroundResource(R.color.white)

                    tickItem.visibility=View.INVISIBLE

                    for (i in 0 until noteAdapter.deleteAndEditArrayList.size) {

                        val keys = noteAdapter.deleteAndEditArrayList[i].key

                        var position = noteAdapter.deleteAndEditArrayList[i].position

                        if (noteAdapter.allNoteArrayList.size == 1)
                            when {
                                position == 0 -> noteAdapter.allNoteArrayList.removeAt(position)
                                position>=1 -> {
                                    position=1
                                    noteAdapter.allNoteArrayList.removeAt(position - 1)


                                }
                            }
                                else  noteAdapter.allNoteArrayList.removeAt(position)


                        noteInformationProcess.deleteNote(context, keys)
                    }

                    noteAdapter.deleteAndEditArrayList.clear()

                    //noteInformationProcess.deleteNote(this@MainActivity, key)
                    activityMainBinding.recyclerView.clearFocus()

                    noteAdapter.notifyDataSetChanged()

                }

                setNegativeButton("No") { _, _ -> }

                show()

            }
        }


    }

    override fun editItem(key: Int, constraintLayout: ConstraintLayout, tickItem: ImageView) {

        activityMainBinding.addNote.visibility=View.INVISIBLE

        activityMainBinding.editNoteTaskBar.setOnClickListener {

            activityMainBinding.addNote.visibility=View.VISIBLE

            activityMainBinding.editNoteTaskBar.visibility = View.INVISIBLE

            activityMainBinding.deleteNoteTaskBar.visibility = View.INVISIBLE

            val intent: Intent = Intent(this, CreateNote::class.java)

            intent.putExtra("idExtra", noteAdapter.deleteAndEditArrayList[0].key)

            tickItem.visibility = View.INVISIBLE

            //constraintLayout.setBackgroundResource(R.color.white)

            noteAdapter.deleteAndEditArrayList.clear()

            startActivity(intent)

        }
    }


    override fun notifyDataForReady() {

        runOnUiThread {

            noteAdapter.notifyDataSetChanged()

        }
    }
}