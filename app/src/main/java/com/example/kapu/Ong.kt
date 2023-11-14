package com.example.kapu

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ongs")
data class Ong(
    @PrimaryKey(autoGenerate = true)
    var id_ong: Int,
    var name: String,
    var description: String?,
    var address: String,
    var phone: String?,
    var email: String,
    var id_user: Int
    // val img_profile: String?,
)