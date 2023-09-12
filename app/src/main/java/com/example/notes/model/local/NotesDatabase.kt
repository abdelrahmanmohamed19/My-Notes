package com.example.notes.model.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Notes::class], version = 2, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun getDao() : NotesDao
}