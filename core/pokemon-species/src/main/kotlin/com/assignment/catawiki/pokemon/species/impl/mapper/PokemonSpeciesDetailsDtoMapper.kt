package com.assignment.catawiki.pokemon.species.impl.mapper

import com.assignment.catawiki.pokemon.species.api.model.PokemonDetails
import com.assignment.catawiki.pokemon.species.impl.remote.model.PokemonSpeciesDetailsDto
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
