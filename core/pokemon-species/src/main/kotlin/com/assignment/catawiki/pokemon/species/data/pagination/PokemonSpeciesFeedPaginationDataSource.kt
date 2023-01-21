package com.assignment.catawiki.pokemon.species.data.pagination

import com.assignment.catawiki.pokemon.species.data.pagination.model.PaginationData

internal interface PokemonSpeciesFeedPaginationDataSource {

    suspend fun getPaginationData(): PaginationData?
    suspend fun clearPaginationData()
    suspend fun savePaginationData(paginationData: PaginationData)
}
