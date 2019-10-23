package com.packetalk.view_model

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import java.nio.file.Files.delete
import com.packetalk.room_db.NoteDao
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import com.packetalk.room_db.Note
import com.packetalk.room_db.NoteRoomDatabase


class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = this.javaClass.simpleName
    private val noteDao: NoteDao
    private val noteDB: NoteRoomDatabase? = NoteRoomDatabase.getDatabase(application)
    internal val allNotes: LiveData<List<Note>>

    init {
        noteDao = noteDB!!.noteDao()
        allNotes = noteDao.allNotes
    }

    fun insert(note: Note) {
        InsertAsyncTask(noteDao).execute(note)
    }

    fun update(note: Note) {
        UpdateAsyncTask(noteDao).execute(note)
    }

    fun delete(note: Note) {
        DeleteAsyncTask(noteDao).execute(note)
    }

    override fun onCleared() {
        super.onCleared()
        Log.e(TAG, "ViewModel Destroyed")
    }

    @SuppressLint("StaticFieldLeak")
    private open inner class OperationsAsyncTask internal constructor(internal var mAsyncTaskDao: NoteDao) :
        AsyncTask<Note, Void, Void>() {

        override fun doInBackground(vararg notes: Note): Void? {
            return null
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class InsertAsyncTask internal constructor(mNoteDao: NoteDao) :
        OperationsAsyncTask(mNoteDao) {

        protected override fun doInBackground(vararg notes: Note): Void? {
            mAsyncTaskDao.insert(notes[0])
            return null
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class UpdateAsyncTask internal constructor(noteDao: NoteDao) :
        OperationsAsyncTask(noteDao) {

        protected override fun doInBackground(vararg notes: Note): Void? {
            mAsyncTaskDao.update(notes[0])
            return null
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class DeleteAsyncTask(noteDao: NoteDao) : OperationsAsyncTask(noteDao) {

        protected override fun doInBackground(vararg notes: Note): Void? {
            mAsyncTaskDao.delete(notes[0])
            return null
        }
    }
}