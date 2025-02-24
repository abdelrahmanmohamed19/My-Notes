package com.example.notes.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note : Notes)

    @Delete
    suspend fun deleteNote(note: Notes)

    @Query("SELECT * FROM Notes_Table")
    fun getAllNotes() : List<Notes>

    @Query("SELECT * FROM Notes_Table ORDER BY title ASC")
    suspend fun getAllNotesByTitleASC() : List<Notes>

    @Query("SELECT * FROM Notes_Table ORDER BY date ASC")
    suspend fun getAllNotesByDateASC() : List<Notes>

    @Query("SELECT * FROM Notes_Table WHERE title Like :title || '%'")
    suspend fun getNote(title : String ) : List<Notes>

    @Query("SELECT * FROM NOTES_TABLE WHERE id Like :id")
    suspend fun getNoteByID(id: Int): List<Notes>

    @Query("DELETE FROM NOTES_TABLE")
    suspend fun clear()
}