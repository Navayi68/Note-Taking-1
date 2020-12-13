package com.example.notetaking.Recycler.DataHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_viewholder_uncategorized.view.*


class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val textTitle:TextView=view.textTitleholder

    val textMessage:TextView=view.textMessageHolder

    val rootViewItem:ConstraintLayout=view.rootViewItem

    val categorizedView:TextView=view.textCategorizedView

    val currentDateItem:TextView=view.textCurrentDate

    val tickItem:ImageView=view.imageTick



}