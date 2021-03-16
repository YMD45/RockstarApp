package com.example.rockstarapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rockstars")
data class Rockstar(
    @PrimaryKey(autoGenerate = true) var id:Int,
    @ColumnInfo(name="status") var status:String,
    @ColumnInfo(name = "last_name") var lastName:String,
    @ColumnInfo(name = "first_name") var firstName:String,
    @ColumnInfo(name = "picture_url") var picture: String,
    @ColumnInfo(name="bookmark") var bookmark:Boolean) {

    constructor(status: String,lastName: String,firstName: String,picture: String,bookmark: Boolean):
            this(0,status,lastName,firstName,picture,bookmark)

    override fun equals(other: Any?): Boolean {
        var otherRockstar = other as Rockstar

        return otherRockstar.lastName == this.lastName &&
                otherRockstar.firstName == this.firstName &&
                otherRockstar.picture == this.picture &&
                otherRockstar.status == this.status
    }
}