package com.example.mytraveljournal.TravelData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TravelsDao {
    @Query("SELECT * FROM travel")
    fun getAllTravels(): LiveData<List<Travel>>

    @Query("SELECT * FROM travel WHERE userId = :iduser")
    fun getUserTravel(iduser: Int): LiveData<List<Travel>>

    @Insert
    suspend fun insertTravel(travel: Travel)

    @Update
    suspend fun updateTravel(travel: Travel)

    @Delete
    suspend fun deleteTravel(travel: Travel)
}