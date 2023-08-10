package com.example.notes.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Notes::class], version = 1, exportSchema = false)
abstract class myDatabase : RoomDatabase() {

    abstract fun getDao() : NotesDao
}