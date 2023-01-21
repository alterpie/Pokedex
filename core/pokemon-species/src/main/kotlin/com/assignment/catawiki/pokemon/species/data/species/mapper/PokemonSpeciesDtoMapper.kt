package com.assignment.catawiki.pokemon.species.data.species.mapper

import android.net.Uri
import com.assignment.catawiki.pokemon.species.BuildConfig
import com.assignment.catawiki.pokemon.species.data.species.local.model.SpeciesEntity
import com.assignment.catawiki.pokemon.species.data.species.remote.model.PokemonSpeciesDto
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies
import javax.inject.Inject

internal class PokemonSpeciesDtoMapper @Inject constructor() {

    fun map(from: PokemonSpeciesDto): SpeciesEntity {
        return with(from) {
            val id = Uri.parse(url).pathSegments.last().toLong()
            SpeciesEntity(
                id,
                name,
                "${BuildConfig.POKEMON_IMAGE_URL}$id.png",
                null,
                null,
                null,
                null,
            )
        }
    }
}
