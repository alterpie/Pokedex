package com.assignment.catawiki.pokemon.species.data.species.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "species")
data class SpeciesEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "capture_rate")
    val captureRate: Int?,
    @ColumnInfo(name = "evolution_species_name")
    val evolutionSpeciesName: String?,
    @ColumnInfo(name = "evolution_species_image_url")
    val evolutionSpeciesImageUrl: String?,
)
