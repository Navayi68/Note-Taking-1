package com.example.notetaking.DataHolder
//Use Data For Process
data class InformationDataClass(
    val id: String ,
    val title: String,
    val message: String,
    val categorizedData:String="Uncategorized",
    val currentDate:String,
    var selectItem:Boolean=false
)
data class InformationDelete(
    val key:String,
    val position:Int
)