package com.assignment.catawiki.pokemon.species.data.species.local.model

import androidx.room.ColumnInfo

internal data class UpdateSpeciesEvolution(
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "evolution")
    val evolution: SpeciesEntity.Evolution,
)
