package com.example.notes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Notes::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun getDao() : NotesDao
}