package com.assignment.catawiki.pokemon.species.data.species.mapper

import android.net.Uri
import com.assignment.catawiki.pokemon.species.BuildConfig
import com.assignment.catawiki.pokemon.species.data.species.remote.model.EvolutionChainDto
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies
import javax.inject.Inject

internal class EvolutionChainDtoMapper @Inject constructor() {

    fun map(
        from: EvolutionChainDto,
        forSpeciesName: String
    ): PokemonSpecies.Evolution = with(from) {
        val species = findEvolutionTargetSpecies(from.chain, forSpeciesName)
            ?: return@with PokemonSpecies.Evolution.Final
        val id = Uri.parse(species.url).pathSegments.last().toLong()
        PokemonSpecies.Evolution.Next(
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
