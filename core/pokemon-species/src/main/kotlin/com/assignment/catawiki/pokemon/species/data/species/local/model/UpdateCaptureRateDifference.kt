package com.assignment.catawiki.pokemon.species.data.species.local.model

import androidx.room.ColumnInfo

data class UpdateCaptureRateDifference(
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "capture_rate_difference")
    val captureRateDifference: Int,
)
