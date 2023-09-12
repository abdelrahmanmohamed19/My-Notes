package com.example.notes.model.repository

import com.example.notes.model.local.Notes
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun insertNote (note : Notes)

    suspend fun deleteNote (note: Notes)

    suspend fun updateNote (note: Notes)

    suspend fun getNote (title : String) : List<Notes>

    suspend fun getAllNotes (): Flow<List<Notes>>

    suspend fun getAllNotesASC() : List<Notes>

    suspend fun getAllNotesDateASC() : List<Notes>


    suspend fun clear()

}