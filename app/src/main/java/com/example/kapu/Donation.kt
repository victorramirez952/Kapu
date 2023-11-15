package com.example.kapu

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "donations")
data class Donation(
    @PrimaryKey(autoGenerate = true)
    var id_donation: Int,
    var title: String,
    // var image: String,
    var id_ong: Int
)