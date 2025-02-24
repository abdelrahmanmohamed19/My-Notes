package com.example.notes.di

import android.content.Context
import androidx.room.Room
import com.example.notes.domain.repository.NotesRepository
import com.example.notes.data.repository.NotesRepositoryImpl
import com.example.notes.data.local.NotesDao
import com.example.notes.data.local.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context : Context) : NotesDatabase =
        Room.databaseBuilder(context, NotesDatabase::class.java,"Notes Database").build()

    @Provides
    @Singleton
    fun provideDatabaseDao(database : NotesDatabase) : NotesDao = database.getDao()

    @Provides
    @Singleton
    fun provideRepository (dao: NotesDao) : NotesRepository = NotesRepositoryImpl(dao)
}