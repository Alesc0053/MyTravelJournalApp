package com.example.mytraveljournal.TravelData

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
class Travel (
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val userId: Int,
    val name: String,
    val date: String,
    var favorite: Boolean,
    val travelType: String,
    val travelMood: Int,
    val travelNotes: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable