package com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model

import androidx.room.ColumnInfo


data class UpdateSpeciesDetails(
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "capture_rate")
    val captureRate: Int?,
    @ColumnInfo(name = "evolution_chain_url")
    val evolutionChainUrl: String?,
)
