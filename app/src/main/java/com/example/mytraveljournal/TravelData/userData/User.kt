package com.example.mytraveljournal.TravelData.userData

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
class User(
    val name: String,
    val password: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable