package com.assignment.catawiki.pokemon.species.impl.local.species.model

import androidx.room.ColumnInfo


data class UpdateSpeciesDetails(
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "capture_rate")
    val captureRate: Int?,
    @ColumnInfo(name = "evolution_species_name")
    val evolutionSpeciesName: String?,
    @ColumnInfo(name = "evolution_species_image_url")
    val evolutionSpeciesImageUrl: String?,
)
