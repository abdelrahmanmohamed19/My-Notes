package com.example.notes.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {

    @Insert
    fun addNote(note : Notes)

    @Delete
    fun deleteNote(note: Notes)

    @Update
    fun updateNote(notes: Notes)

    @Query("SELECT * FROM Notes_Table")
    fun getAllNotes() : List<Notes>

    @Query("SELECT * FROM Notes_Table WHERE id=:id")
    fun getNote(id : Int) : Notes


}