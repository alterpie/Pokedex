package com.yerastov.assignment.pokedex.core.pokemon.species.data.pagination

import com.yerastov.assignment.pokedex.core.pokemon.species.data.pagination.model.PaginationData

internal interface PokemonSpeciesFeedPaginationDataSource {

    suspend fun getPaginationData(): PaginationData?
    suspend fun clearPaginationData()
    suspend fun savePaginationData(paginationData: PaginationData)
}
