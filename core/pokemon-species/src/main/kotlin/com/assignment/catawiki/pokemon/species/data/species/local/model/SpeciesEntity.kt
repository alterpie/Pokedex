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
    @ColumnInfo(name = "evolution_chain_url")
    val evolutionChainUrl: String?,
    @ColumnInfo(name = "evolution")
    val evolution: Evolution?,
) {
    sealed interface Evolution {
        data class EvolvesTo(
            val name: String,
            val imageUrl: String,
        ) : Evolution

        object Final : Evolution
    }
}
