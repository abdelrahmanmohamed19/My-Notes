package com.example.notes.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Notes_Table")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title : String,
    val body : String,
    val date : String
)
