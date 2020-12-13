package com.example.notetaking.DataProcess

import android.content.Context
import com.example.notetaking.DataHolder.InformationDataClass
import com.example.notetaking.MainActivity

//use Interface For Background And Foreground In Thread
interface InterfaceLoadDataProcess {

    fun notifyDataForReady()

}

class NoteInformationProcess {

    //Save Information Data in SharedPreferences
    fun saveNote(
        context: Context,
        id: String,
        title: String,
        message: String,
        categorizedData: String,
        currentDate: String,
    ) {

        val sharedPreferences = context.getSharedPreferences("id", Context.MODE_PRIVATE)

        sharedPreferences.edit().let {

            it.putString(
                id.toString(), "${id}|${title}|${message}|${categorizedData}|${currentDate}"
            )

            it.apply()
        }
    }

    //Read Information Data from allDataArrayList For Process
    private fun loadNote(
        context: Context
    ): List<InformationDataClass> {

        val sharedPreferences = context.getSharedPreferences("id", Context.MODE_PRIVATE)

        val allDataArrayList: ArrayList<InformationDataClass> = ArrayList<InformationDataClass>()

        sharedPreferences.all.keys.forEach { key ->

            val data = sharedPreferences.getString(key, null)

            data?.let {

                val splited = it.split("|")

                val id = splited[0]

                val title = splited[1]

                val message = splited[2]

                val categorizedData = splited[3]

                val currentDate = splited[4]

                allDataArrayList.add(
                    InformationDataClass(id, title, message, categorizedData, currentDate)
                )
            }
        }

        return allDataArrayList.sortedByDescending { it.id }

    }

    //Search Item In DataList By Title
    fun searchItem(context: Context, searchTitle: String): List<InformationDataClass> {

        val allData = loadNote(context)

        val searchResult = ArrayList<InformationDataClass>()

        allData.forEach {

            if (it.title.contains(searchTitle))

                searchResult.add(it)

        }

        return searchResult
    }

    ////Search Item In DataList By Id
    fun searchById(context: Context, searchId: String): List<InformationDataClass> {
        val allData = loadNote(context)

        val searchResult = ArrayList<InformationDataClass>()

        allData.forEach {

            if (it.id.equals( searchId))

                searchResult.add(it)

        }

        return searchResult
    }

    //Delete Item From List
    fun deleteNote(context: Context, key: String) {

        val sharedPreferences = context.getSharedPreferences("id", Context.MODE_PRIVATE)

        sharedPreferences.edit().remove(key).apply()

    }

    //Prepare data in Background to add to the list
    fun setupAdapter(
        mainActivity: MainActivity,
        interfaceLoadDataProcess: InterfaceLoadDataProcess
    ) {
        //Background Process
        val loadProcess = Thread(Runnable {

            mainActivity.noteAdapter.allNoteArrayList.clear()

            mainActivity.noteAdapter.allNoteArrayList.addAll(loadNote(mainActivity))

            interfaceLoadDataProcess.notifyDataForReady()
        })

        if (!loadProcess.isAlive)

            loadProcess.start()

    }


}