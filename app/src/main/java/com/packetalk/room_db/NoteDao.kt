package com.packetalk.room_db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface NoteDao {

    @get:Query("SELECT * FROM notes")
    val allNotes: LiveData<List<Note>>

    @Insert
    fun insert(note: Note)

    @Query("SELECT * FROM notes WHERE id=:noteId")
    fun getNote(noteId: String): LiveData<Note>

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note): Int
}