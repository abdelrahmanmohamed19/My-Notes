package com.example.notes.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert
    suspend fun addNote(note : Notes)

    @Delete
    suspend fun deleteNote(note: Notes)

    @Update
    suspend fun updateNote(notes: Notes)

    @Query("SELECT * FROM Notes_Table")
    fun getAllNotes() : Flow<List<Notes>>

    @Query("SELECT * FROM Notes_Table ORDER BY title ASC")
    suspend fun getAllNotesASC() : List<Notes>

    @Query("SELECT * FROM Notes_Table ORDER BY date ASC")
    suspend fun getAllNotesDateASC() : List<Notes>

    @Query("SELECT * FROM Notes_Table WHERE title Like :title ")
    suspend fun getNote(title : String ) : List<Notes>

    @Query("DELETE FROM NOTES_TABLE")
    suspend fun clear()

}