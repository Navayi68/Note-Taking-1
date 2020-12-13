package com.example.notetaking

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.notetaking.DataHolder.InformationDataClass
import com.example.notetaking.DataHolder.InformationDelete
import com.example.notetaking.Recycler.DataHolder.NoteViewHolder

//use InterFace For Delete And Edit Data
interface PassDataForProcess {

    fun deleteItem(
        key: String,
        position: Int,
        constraintLayout: ConstraintLayout,
        tickItem: ImageView
    )

    fun editItem(key: Int, constraintLayout: ConstraintLayout, tickItem: ImageView)


}

//Categorized Data For Spinner
object ViewType {

    const val TypeUncategorized = 0

    const val TypeOffice = 1

    const val TypePersonal = 2

    const val TypeFamily = 3

    const val TypeStudy = 4

    const val TypeShopping = 5

    const val TypeHealth = 6

    const val TypeWork = 7

}


class NoteAdapter(
    private val context: MainActivity,
    private val passDataForProcess: PassDataForProcess
) :
    RecyclerView.Adapter<NoteViewHolder>() {

    //ArrayList For All Data
    val allNoteArrayList: ArrayList<InformationDataClass> = ArrayList<InformationDataClass>()

    //ArrayList For Edit And Delete
    val deleteAndEditArrayList: ArrayList<InformationDelete> = ArrayList<InformationDelete>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        return when (viewType) {

            ViewType.TypeUncategorized ->

                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.note_viewholder_uncategorized, parent, false)
                )

            ViewType.TypeOffice ->

                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.note_viewholder_office, parent, false)
                )

            ViewType.TypePersonal ->

                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.note_viewholder_personal, parent, false)
                )

            ViewType.TypeFamily ->

                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.note_viewholder_family, parent, false)
                )

            ViewType.TypeStudy ->

                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.note_viewholder_study, parent, false)
                )

            ViewType.TypeShopping ->

                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.note_viewholder_shopping, parent, false)
                )

            ViewType.TypeHealth ->

                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.note_viewholder_health, parent, false)
                )

            else ->

                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.note_viewholder_work, parent, false)
                )

        }
    }

    //Edit ViewType
    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)

        return when (allNoteArrayList[position].categorizedData) {

            "Uncategorized" -> ViewType.TypeUncategorized

            "Office" -> ViewType.TypeOffice

            "Personal" -> ViewType.TypePersonal

            "Family affair" -> ViewType.TypeFamily

            "Study" -> ViewType.TypeStudy

            "Shopping" -> ViewType.TypeShopping

            "Health" -> ViewType.TypeHealth

            else -> ViewType.TypeWork

        }

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        holder.textTitle.text = allNoteArrayList[position].title

        holder.textMessage.text = allNoteArrayList[position].message

        holder.categorizedView.text = allNoteArrayList[position].categorizedData

        holder.currentDateItem.text = allNoteArrayList[position].currentDate

        if (allNoteArrayList[position].selectItem) {
            holder.tickItem.visibility = View.VISIBLE
            holder.rootViewItem.setBackgroundResource(R.color.backgroundDelete)
        } else {
            holder.tickItem.visibility = View.INVISIBLE
            holder.rootViewItem.setBackgroundResource(R.color.white)
        }

        if (deleteAndEditArrayList.size == 0) {

            holder.tickItem.visibility = View.INVISIBLE

            holder.rootViewItem.setBackgroundResource(R.color.white)
        }

        holder.rootViewItem.setOnLongClickListener {

            if (deleteAndEditArrayList.size == 0) {

                passDataForProcess.editItem(position, holder.rootViewItem, holder.tickItem)


                if (holder.tickItem.isShown) {
                    allNoteArrayList[position].selectItem = false
                    holder.tickItem.visibility = View.INVISIBLE
                    holder.rootViewItem.setBackgroundResource(R.color.white)
                } else {
                    allNoteArrayList[position].selectItem = true
                    holder.tickItem.visibility = View.VISIBLE
                    holder.rootViewItem.setBackgroundResource(R.color.backgroundDelete)
                }


                deleteAndEditArrayList.add(
                    InformationDelete(
                        allNoteArrayList[position].id,
                        position
                    )
                )

                passDataForProcess.deleteItem(
                    allNoteArrayList[position].id.toString(),
                    position,
                    holder.rootViewItem,
                    holder.tickItem
                )

                if (deleteAndEditArrayList.size == 1) {
                    passDataForProcess.editItem(position, holder.rootViewItem, holder.tickItem)

                }


            }

            true

        }

        holder.rootViewItem.setOnClickListener {

            passDataForProcess.editItem(position, holder.rootViewItem, holder.tickItem)

            if (holder.tickItem.isShown) {
                allNoteArrayList[position].selectItem = false
                holder.tickItem.visibility = View.INVISIBLE
                holder.rootViewItem.setBackgroundResource(R.color.white)
                deleteAndEditArrayList.remove(
                    InformationDelete(
                        allNoteArrayList[position].id,
                        position
                    )
                )
                if (deleteAndEditArrayList.size == 1) {

                    passDataForProcess.editItem(position, holder.rootViewItem, holder.tickItem)

                    context.activityMainBinding.editNoteTaskBar.visibility = View.VISIBLE

                }

                if (deleteAndEditArrayList.size == 0) {

                    context.activityMainBinding.addNote.visibility = View.VISIBLE

                    context.activityMainBinding.deleteNoteTaskBar.visibility = View.INVISIBLE

                    context.activityMainBinding.editNoteTaskBar.visibility = View.INVISIBLE

                }
            } else {

                if (deleteAndEditArrayList.size == 0) {

                    context.activityMainBinding.addNote.visibility = View.VISIBLE

                    val intent: Intent = Intent(context, CreateNote::class.java)

                    intent.putExtra("idExtra", allNoteArrayList[position].id.toString())

                    intent.putExtra("titleExtra", allNoteArrayList[position].title)

                    intent.putExtra("messageExtra", allNoteArrayList[position].message)

                    intent.putExtra("categorizedExtra", allNoteArrayList[position].categorizedData)

                    context.startActivity(intent)

                } else {
                    passDataForProcess.deleteItem(
                        allNoteArrayList[position].id.toString(),
                        position,
                        holder.rootViewItem,
                        holder.tickItem
                    )

                    allNoteArrayList[position].selectItem = true
                    holder.tickItem.visibility = View.VISIBLE
                    holder.rootViewItem.setBackgroundResource(R.color.backgroundDelete)

                    deleteAndEditArrayList.add(
                        InformationDelete(
                            allNoteArrayList[position].id,
                            position
                        )
                    )

                    if (deleteAndEditArrayList.size > 1) {

                        context.activityMainBinding.editNoteTaskBar.visibility = View.INVISIBLE

                    }
                }
                if (deleteAndEditArrayList.size == 1) {

                    passDataForProcess.editItem(position, holder.rootViewItem, holder.tickItem)
                }
            }
        }
    }

    override fun getItemCount(): Int {

        return allNoteArrayList.size

    }

}