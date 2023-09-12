package com.example.notes.model.repository

import com.example.notes.model.local.Notes
import com.example.notes.model.local.NotesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(private val dao : NotesDao) : NotesRepository {

    override suspend fun insertNote (note : Notes) {
        dao.addNote(note)
    }

    override suspend fun deleteNote (note: Notes) {
        dao.deleteNote(note)
    }

    override suspend fun updateNote (note: Notes) {
        dao.updateNote(note)
    }

    override suspend fun getNote (title : String) : List<Notes>{
        return dao.getNote(title)
    }

   override suspend fun getAllNotes (): Flow<List<Notes>> {
        return dao.getAllNotes()
    }

    override suspend fun getAllNotesASC(): List<Notes> {
        return dao.getAllNotesASC()
    }

    override suspend fun getAllNotesDateASC(): List<Notes> {
        return dao.getAllNotesDateASC()
    }

    override suspend fun clear() {
        dao.clear()
    }

}