package com.example.kapu

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "volunteering")
data class VolunteeringClass(
    @PrimaryKey(autoGenerate = true)
    var id_volunteering: Int,
    var title: String,
    var startDate: String,
    var endDate: String,
    var startTime: String,
    var endTime: String,
    var id_ong: Int
)