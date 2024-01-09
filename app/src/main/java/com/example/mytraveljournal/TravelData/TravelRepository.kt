package com.example.mytraveljournal.TravelData

import androidx.lifecycle.LiveData

class TravelRepository(private val travelDao: TravelsDao) {

    val readAllData: LiveData<List<Travel>> = travelDao.getAllTravels()

    suspend fun insertTravel(travel: Travel) {
        travelDao.insertTravel(travel)
    }

    suspend fun updateTravel(travel: Travel){
        travelDao.updateTravel(travel)
    }

    suspend fun deleteTravel(travel: Travel){
        travelDao.deleteTravel(travel)
    }

    fun getUserTavel(userId: Int): LiveData<List<Travel>> {
        return travelDao.getUserTravel(userId)
    }
}