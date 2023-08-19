package com.example.notes

import android.provider.ContactsContract.CommonDataKinds.Note
import com.example.notes.db.Notes
import com.example.notes.db.NotesDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MainRepository @Inject constructor(private val dao : NotesDao) {

    suspend fun insertNote (note : Notes) {
        dao.addNote(note)
    }

    suspend fun deleteNote (note: Notes) {
        dao.deleteNote(note)
    }

    suspend fun updateNote (note: Notes) {
        dao.updateNote(note)
    }

    suspend fun getNote (title : String) : MutableStateFlow<List<Notes>> {
        val notesList = MutableStateFlow(emptyList<Notes>())
        notesList.value = dao.getNote(title)
        return notesList

    }

   fun getAllNotes (): Flow<List<Notes>> {
        return dao.getAllNotes()
    }

    suspend fun clear() {
        dao.clear()
    }

}