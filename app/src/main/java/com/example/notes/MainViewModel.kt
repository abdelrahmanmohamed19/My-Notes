package com.example.notes

import android.provider.ContactsContract.CommonDataKinds.Note
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.MainRepository
import com.example.notes.db.Notes
import com.example.notes.db.NotesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    var listMain = mutableStateOf(emptyList<Notes>())

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

    suspend fun updateNote (note:Notes) {
        repository.updateNote(note)
    }

    suspend fun getNote (title : String) : MutableStateFlow<List<Notes>> {
        val notesList = MutableStateFlow(emptyList<Notes>())
        notesList.value = repository.getNote(title).value
        return notesList

    }

   suspend fun getAllNotes (){
        repository.getAllNotes().collect{
            listMain.value = it
        }
    }

   suspend fun clear() {
       repository.clear()
   }


}