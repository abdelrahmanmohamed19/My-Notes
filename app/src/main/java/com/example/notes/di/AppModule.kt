package com.example.notes.di

import android.content.Context
import androidx.room.Room
import com.example.notes.db.NotesDao
import com.example.notes.db.NotesDatabase
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
        Room.databaseBuilder(context,NotesDatabase::class.java,"mydatabase")
            .allowMainThreadQueries().fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideDatabaseDao(database : NotesDatabase) : NotesDao = database.getDao()
}