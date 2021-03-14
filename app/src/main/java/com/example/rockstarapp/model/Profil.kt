package com.example.rockstarapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profil")
data class Profil (
    @PrimaryKey(autoGenerate = true) var id:Int,
    @ColumnInfo(name = "image_url") var image:String,
    @ColumnInfo(name = "full_name") var fullName:String) {

    constructor(image: String,fullName: String):this(0,image, fullName)
}