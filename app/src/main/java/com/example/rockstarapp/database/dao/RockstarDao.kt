package com.example.rockstarapp.database.dao

import androidx.room.*
import com.example.rockstarapp.model.Rockstar

@Dao
interface  RockstarDao {

    @Query("SELECT * FROM rockstars")
    fun getAll(): List<Rockstar>

    @Query("SELECT * FROM rockstars WHERE last_name LIKE :name")
    fun findByLastName(name: String): Rockstar

    @Query("SELECT * FROM rockstars WHERE first_name LIKE :name")
    fun findByFirstName(name: String): Rockstar

    @Query("SELECT * FROM rockstars WHERE status LIKE :status")
    fun findByStatus(status: String):  List<Rockstar>

    @Query("SELECT * FROM rockstars WHERE bookmark LIKE :bookmark")
    fun findByBookmark(bookmark: Boolean):  List<Rockstar>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(vararg rockstar: Rockstar)

    @Delete
    fun delete(rockstar: Rockstar)

    @Update
    fun updateRockstar(vararg rockstars: Rockstar)
}