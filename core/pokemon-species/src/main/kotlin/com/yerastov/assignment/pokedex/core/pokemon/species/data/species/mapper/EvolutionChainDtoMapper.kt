package com.yerastov.assignment.pokedex.core.pokemon.species.data.species.mapper

import android.net.Uri
import com.yerastov.assignment.pokedex.core.pokemon.species.BuildConfig
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.SpeciesEntity
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.remote.model.EvolutionChainDto
import javax.inject.Inject

internal class EvolutionChainDtoMapper @Inject constructor() {

    fun map(
        from: EvolutionChainDto,
        forSpeciesName: String,
    ): SpeciesEntity.Evolution = with(from) {
        val species = findEvolutionTargetSpecies(from.chain, forSpeciesName)
            ?: return@with SpeciesEntity.Evolution.Final
        val id = Uri.parse(species.url).pathSegments.last().toLong()
        SpeciesEntity.Evolution.EvolvesTo(
            id,
            species.name,
            "${BuildConfig.POKEMON_IMAGE_URL}${id}.png",
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
