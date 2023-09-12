package com.example.notes.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.model.local.Notes
import com.example.notes.model.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {

    var notesList by mutableStateOf(emptyList<Notes>())

    init {
        viewModelScope.launch {
            getAllNotes()
        }
    }

    suspend fun insertNote (note : Notes) {
        repository.insertNote(note)
    }

    suspend fun deleteNote (note: Notes) {
        repository.deleteNote(note)
    }

    suspend fun updateNote (note: Notes) {
        repository.updateNote(note)
    }

    suspend fun getNote (title : String){
        notesList = repository.getNote(title)
    }

    suspend fun getAllNotes ()  {
        repository.getAllNotes().collect{
            notesList = it
        }
    }

    suspend fun getAllNotesASC (){
        notesList = repository.getAllNotesASC()

    }

    suspend fun getAllNotesDateASC (){
        notesList = repository.getAllNotesDateASC()
    }

   suspend fun clear() {
       repository.clear()
   }


}