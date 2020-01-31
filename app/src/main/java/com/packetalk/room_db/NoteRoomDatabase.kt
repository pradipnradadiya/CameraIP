package com.packetalk.room_db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Database

@Database(entities = [Note::class], version = 1,exportSchema = false)
abstract class NoteRoomDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private var noteRoomInstance: NoteRoomDatabase? = null

        internal fun getDatabase(context: Context): NoteRoomDatabase? {
            if (noteRoomInstance == null) {
                synchronized(NoteRoomDatabase::class.java) {
                    if (noteRoomInstance == null) {
                        noteRoomInstance = Room.databaseBuilder(
                            context.applicationContext,
                            NoteRoomDatabase::class.java, "note_database"
                        )
                        .build()
                    }
                }
            }
            return noteRoomInstance
        }
    }
}