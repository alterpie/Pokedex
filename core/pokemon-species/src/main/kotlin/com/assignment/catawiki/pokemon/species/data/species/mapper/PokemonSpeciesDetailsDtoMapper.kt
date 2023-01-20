package com.assignment.catawiki.pokemon.species.data.species.mapper

import com.assignment.catawiki.pokemon.species.domain.model.PokemonDetails
import com.assignment.catawiki.pokemon.species.data.species.remote.model.PokemonSpeciesDetailsDto
import javax.inject.Inject

internal class PokemonSpeciesDetailsDtoMapper @Inject constructor() {

    fun map(from: PokemonSpeciesDetailsDto): PokemonDetails = with(from) {
        PokemonDetails(
            id,
            name,
            flavorTextEntries.find { it.language.name.equals("en", ignoreCase = true) }?.text ?: "",
            captureRate,
            null,
        )
    }
}
