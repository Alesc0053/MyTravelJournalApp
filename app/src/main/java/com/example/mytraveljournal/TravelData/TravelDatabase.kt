package com.example.mytraveljournal.TravelData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mytraveljournal.TravelData.userData.User
import com.example.mytraveljournal.TravelData.userData.UserDao

@Database(
    entities = [Travel::class, User::class],
    version = 4
)

abstract class TravelDatabase : RoomDatabase(){
    abstract val dao: TravelsDao
    abstract val userDao: UserDao

    companion object{
        @Volatile
        private var INSTANCE: TravelDatabase? = null

        fun getInstance(context: Context): TravelDatabase { //asta e un singleton
            return  INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TravelDatabase::class.java, "travel_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

}
