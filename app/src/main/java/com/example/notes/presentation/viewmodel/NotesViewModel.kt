package com.example.notes.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.local.Notes
import com.example.notes.data.utils.Resources
import com.example.notes.domain.usecase.AddNoteUseCase
import com.example.notes.domain.usecase.ClearAllNotesUseCase
import com.example.notes.domain.usecase.DeleteNoteUseCase
import com.example.notes.domain.usecase.GetAllNotesByDateUseCase
import com.example.notes.domain.usecase.GetAllNotesByTitleASCUseCase
import com.example.notes.domain.usecase.GetAllNotesUseCase
import com.example.notes.domain.usecase.GetNoteByIDUseCase
import com.example.notes.domain.usecase.GetNoteUseCase
import com.example.notes.presentation.uiState.AddDeleteUpdateNoteUIState
import com.example.notes.presentation.uiState.GetAllNotesUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val clearAllNotesUseCase: ClearAllNotesUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val getNoteByIDUseCase: GetNoteByIDUseCase,
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val getAllNotesByTitleASCUseCase: GetAllNotesByTitleASCUseCase,
    private val getAllNotesByDateUseCase: GetAllNotesByDateUseCase
): ViewModel() {

    var addNoteUIState by mutableStateOf<AddDeleteUpdateNoteUIState?>(null)
        private set

    var updateNoteUIState by mutableStateOf<AddDeleteUpdateNoteUIState?>(null)
        private set

    var deleteNoteUIState by mutableStateOf<AddDeleteUpdateNoteUIState?>(null)
        private set

    var clearAllNoteUIState by mutableStateOf<AddDeleteUpdateNoteUIState?>(null)
        private set

    var getNoteUIState by mutableStateOf<GetAllNotesUIState?>(null)
        private set

    var getNoteByIDUIState by mutableStateOf<GetAllNotesUIState?>(null)
        private set

    var getAllNotesUIState by mutableStateOf<GetAllNotesUIState?>(null)
        private set

    var getAllNotesByTitleUIState by mutableStateOf<GetAllNotesUIState?>(null)
        private set

    var getAllNotesByDateUIState by mutableStateOf<GetAllNotesUIState?>(null)
        private set


    fun resetUIState() {
        addNoteUIState = null
        updateNoteUIState = null
        deleteNoteUIState = null
    }

    fun addNote (note : Notes) {
        viewModelScope.launch {
            addNoteUseCase(note).collect{
                addNoteUIState = when(it) {
                    is Resources.Success -> AddDeleteUpdateNoteUIState(data = it.data)
                    is Resources.Error -> AddDeleteUpdateNoteUIState(errorMessage = it.errorMessage)
                    is Resources.Loading -> AddDeleteUpdateNoteUIState(isLoading = true)
                }
            }
        }
    }

    fun updateNote (note: Notes) {
        viewModelScope.launch {
            addNoteUseCase(note).collect{
                updateNoteUIState = when(it) {
                    is Resources.Success -> AddDeleteUpdateNoteUIState(data = it.data)
                    is Resources.Error -> AddDeleteUpdateNoteUIState(errorMessage = it.errorMessage)
                    is Resources.Loading -> AddDeleteUpdateNoteUIState(isLoading = true)
                }
            }
        }
    }

    fun deleteNote (note: Notes) {
        viewModelScope.launch {
            deleteNoteUseCase(note).collect{
                deleteNoteUIState = when(it) {
                    is Resources.Success -> AddDeleteUpdateNoteUIState(data = it.data)
                    is Resources.Error -> AddDeleteUpdateNoteUIState(errorMessage = it.errorMessage)
                    is Resources.Loading -> AddDeleteUpdateNoteUIState(isLoading = true)
                }
            }
        }
    }

    suspend fun getNote (title : String) {
        viewModelScope.launch {
            getNoteUseCase(title).collect{
                getNoteUIState = when(it) {
                    is Resources.Success -> {
                        Log.d("dsdsadaswetv", it.data.toString())
                        GetAllNotesUIState(data = it.data)
                    }
                    is Resources.Error -> GetAllNotesUIState(errorMessage = it.errorMessage)
                    is Resources.Loading -> {
                        Log.d("dsdsadaswetv", "loading..")
                        GetAllNotesUIState(isLoading = true)
                    }
                }
            }
        }
    }

    suspend fun getNoteByID (id : Int) {
        viewModelScope.launch {
            getNoteByIDUseCase(id).collect{
                getNoteByIDUIState = when(it) {
                    is Resources.Success -> {
                        Log.d("dsdsadaswetv", it.data.toString())
                        GetAllNotesUIState(data = it.data)
                    }
                    is Resources.Error -> GetAllNotesUIState(errorMessage = it.errorMessage)
                    is Resources.Loading -> {
                        Log.d("dsdsadaswetv", "loading..")
                        GetAllNotesUIState(isLoading = true)
                    }
                }
            }
        }
    }

    fun getAllNotes() {
        viewModelScope.launch {
            getAllNotesUseCase().collect{
                getAllNotesUIState = when(it) {
                    is Resources.Success -> {
                        Log.d("thisismymaintag", it.data.toString())
                        GetAllNotesUIState(data = it.data)
                    }
                    is Resources.Error -> {
                        Log.d("thisismymaintag", it.errorMessage.toString())
                        GetAllNotesUIState(errorMessage = it.errorMessage)
                    }
                    is Resources.Loading -> {
                        Log.d("thisismymaintag", "loading..")
                        GetAllNotesUIState(isLoading = true)
                    }
                }
            }
        }
    }

    fun getAllNotesByTitleASC() {
        viewModelScope.launch {
            getAllNotesByTitleASCUseCase().collect{
                getAllNotesByTitleUIState = when(it) {
                    is Resources.Success -> {
                        Log.d("thisismymaintag", it.data.toString())
                        GetAllNotesUIState(data = it.data)
                    }
                    is Resources.Error -> {
                        Log.d("thisismymaintag", it.errorMessage.toString())
                        GetAllNotesUIState(errorMessage = it.errorMessage)
                    }
                    is Resources.Loading -> {
                        Log.d("thisismymaintag", "loading..")
                        GetAllNotesUIState(isLoading = true)
                    }
                }
            }
        }
    }

    fun getAllNotesByDateASC() {
        viewModelScope.launch {
            getAllNotesByDateUseCase().collect{
                getAllNotesByDateUIState = when(it) {
                    is Resources.Success -> {
                        Log.d("thisismymaintag", it.data.toString())
                        GetAllNotesUIState(data = it.data)
                    }
                    is Resources.Error -> {
                        Log.d("thisismymaintag", it.errorMessage.toString())
                        GetAllNotesUIState(errorMessage = it.errorMessage)
                    }
                    is Resources.Loading -> {
                        Log.d("thisismymaintag", "loading..")
                        GetAllNotesUIState(isLoading = true)
                    }
                }
            }
        }
    }

   fun clear() {
       viewModelScope.launch {
           clearAllNotesUseCase().collect{
               clearAllNoteUIState = when(it) {
                   is Resources.Success -> {
                       getAllNotesUIState = GetAllNotesUIState(data = emptyList())
                       AddDeleteUpdateNoteUIState(data = it.data)
                   }
                   is Resources.Error -> AddDeleteUpdateNoteUIState(errorMessage = it.errorMessage)
                   is Resources.Loading -> AddDeleteUpdateNoteUIState(isLoading = true)
               }
           }
       }
   }
}