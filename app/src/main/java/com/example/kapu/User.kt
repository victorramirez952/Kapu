package com.example.kapu

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id_user: Int,
    val email: String,
    val password: String,
    val first_name: String,
    val last_name: String,
    val phone: String?,
    val collaborator: Boolean
    // val img_profile: String?,
)