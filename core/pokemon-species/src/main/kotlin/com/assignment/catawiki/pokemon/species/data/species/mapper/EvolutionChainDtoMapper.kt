package com.assignment.catawiki.pokemon.species.data.species.mapper

import android.net.Uri
import com.assignment.catawiki.pokemon.species.BuildConfig
import com.assignment.catawiki.pokemon.species.domain.model.PokemonDetails
import com.assignment.catawiki.pokemon.species.data.species.remote.model.EvolutionChainDto
import javax.inject.Inject

internal class EvolutionChainDtoMapper @Inject constructor() {

    fun map(
        from: EvolutionChainDto,
        forSpeciesName: String
    ): PokemonDetails.Evolution = with(from) {
        val species = findEvolutionTargetSpecies(from.chain, forSpeciesName)
            ?: return@with PokemonDetails.Evolution.Final
        val id = Uri.parse(species.url).pathSegments.last().toLong()
        PokemonDetails.Evolution.Next(
            species.name,
            "${BuildConfig.POKEMON_IMAGE_URL}${id}.png"
        )
    }

    private fun findEvolutionTargetSpecies(
        chain: EvolutionChainDto.Chain,
        forSpeciesName: String
    ): EvolutionChainDto.Chain.Species? {
        if (chain.species.name == forSpeciesName) {
            return chain.evolutionTargets.firstOrNull()?.species
        }
        var evolutionTarget = chain.evolutionTargets.firstOrNull()
        while (evolutionTarget?.species?.name != forSpeciesName && evolutionTarget != null) {
            evolutionTarget = evolutionTarget.evolutionTargets.firstOrNull()
        }
        return evolutionTarget?.evolutionTargets?.firstOrNull()?.species
    }
}
