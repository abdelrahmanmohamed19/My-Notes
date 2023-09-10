package com.example.notes.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Notes_Table")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title : String,
    val content : String,
    val date : String
)
