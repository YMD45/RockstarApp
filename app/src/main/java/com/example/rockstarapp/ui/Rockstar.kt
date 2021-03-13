package com.example.rockstarapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rockstars")
data class Rockstar(
    @PrimaryKey(autoGenerate = true) var id:Int,
    @ColumnInfo(name = "picture_url") var picture: String,
    @ColumnInfo(name = "last_name") var lastName:String,
    @ColumnInfo(name = "first_name") var firstName:String,
    @ColumnInfo(name="status") var status:String,
    @ColumnInfo(name="bookmark") var bookmark:Boolean) {

}