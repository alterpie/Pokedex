package com.yerastov.assignment.pokedex.core.pokemon.species.data.species.mapper

import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.UpdateSpeciesDetails
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.remote.model.PokemonSpeciesDetailsDto
import java.util.Locale
import javax.inject.Inject

internal class PokemonSpeciesDetailsDtoMapper @Inject constructor() {

    fun map(from: PokemonSpeciesDetailsDto): UpdateSpeciesDetails = with(from) {
        UpdateSpeciesDetails(
            id,
            flavorTextEntries.find { textEntry ->
                textEntry.language.name.equals(Locale.ENGLISH.language, ignoreCase = true)
            }?.text ?: "",
            captureRate,
            evolutionChain?.url,
        )
    }
}
