package com.example.kapu

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id_user: Int,
    var email: String,
    var password: String,
    var first_name: String,
    var last_name: String,
    var phone: String?,
    var collaborator: Boolean,
    var img_profile: ByteArray? = null
)