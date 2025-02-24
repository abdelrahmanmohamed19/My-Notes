package com.example.notes.domain.repository

import com.example.notes.data.local.Notes

interface NotesRepository {

    suspend fun addNote (note : Notes)

    suspend fun deleteNote (note: Notes)

    suspend fun getNote (title : String) : List<Notes>

    suspend fun getNoteByID (id : Int) : List<Notes>

    suspend fun getAllNotes (): List<Notes>

    suspend fun getAllNotesByTitleASC() : List<Notes>

    suspend fun getAllNotesByDateASC() : List<Notes>

    suspend fun clear()

}