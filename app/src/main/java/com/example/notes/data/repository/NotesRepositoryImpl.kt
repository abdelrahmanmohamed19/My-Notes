package com.example.notes.data.repository

import com.example.notes.domain.repository.NotesRepository
import com.example.notes.data.local.Notes
import com.example.notes.data.local.NotesDao
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(private val dao : NotesDao) : NotesRepository {

    override suspend fun addNote (note : Notes) {
        dao.addNote(note)
    }

    override suspend fun deleteNote (note: Notes) {
        dao.deleteNote(note)
    }

    override suspend fun getNote (title : String) : List<Notes> {
        return dao.getNote(title)
    }

    override suspend fun getNoteByID (id : Int) : List<Notes> {
        return dao.getNoteByID(id)
    }

   override suspend fun getAllNotes (): List<Notes> {
        return dao.getAllNotes()
    }

    override suspend fun getAllNotesByTitleASC(): List<Notes> {
        return dao.getAllNotesByTitleASC()
    }

    override suspend fun getAllNotesByDateASC(): List<Notes> {
        return dao.getAllNotesByDateASC()
    }

    override suspend fun clear() {
        dao.clear()
    }
}