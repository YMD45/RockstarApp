package com.example.rockstarapp.database.dao

import androidx.room.*
import com.example.rockstarapp.model.Profil

@Dao
interface  ProfilDao {

    @Query("SELECT * FROM profil")
    fun getAll(): List<Profil>

    @Query("SELECT * FROM profil WHERE full_name LIKE :name")
    fun findByName(name: String): Profil

    @Query("SELECT * FROM profil LIMIT 1")
    fun getProfil(): Profil

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(vararg profil: Profil)

    @Delete
    fun delete(profil: Profil)

    @Update
    fun updateProfil(vararg Profil: Profil)
}