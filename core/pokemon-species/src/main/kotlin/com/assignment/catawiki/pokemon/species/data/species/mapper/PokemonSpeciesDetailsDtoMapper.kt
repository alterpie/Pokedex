package com.assignment.catawiki.pokemon.species.data.species.mapper

import com.assignment.catawiki.pokemon.species.BuildConfig
import com.assignment.catawiki.pokemon.species.data.species.remote.model.PokemonSpeciesDetailsDto
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies
import javax.inject.Inject

internal class PokemonSpeciesDetailsDtoMapper @Inject constructor() {

    fun map(from: PokemonSpeciesDetailsDto): PokemonSpecies = with(from) {
        PokemonSpecies(
            id,
            name,
            "${BuildConfig.POKEMON_IMAGE_URL}$id.png",
            flavorTextEntries.find { it.language.name.equals("en", ignoreCase = true) }?.text ?: "",
            captureRate,
            null,
        )
    }
}
