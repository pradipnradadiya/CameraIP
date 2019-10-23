package com.packetalk.room_db

import androidx.room.Entity
import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo


@Entity(tableName = "notes")
class Note(
    @field:PrimaryKey
    val id: String,
    @field:ColumnInfo(name = "note")
    val note: String
)