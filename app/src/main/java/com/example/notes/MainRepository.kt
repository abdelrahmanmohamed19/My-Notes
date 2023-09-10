package com.example.notes

import com.example.notes.db.Notes
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun insertNote (note : Notes)

    suspend fun deleteNote (note: Notes)

    suspend fun updateNote (note: Notes)

    suspend fun getNote (title : String) : List<Notes>

    suspend fun getAllNotes (): Flow<List<Notes>>

    suspend fun getAllNotesASC() : List<Notes>

    suspend fun getAllNotesDateASC() : List<Notes>


    suspend fun clear()

}