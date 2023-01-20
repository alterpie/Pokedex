package com.assignment.catawiki.pokemon.species.impl.local.pagination

import com.assignment.catawiki.pokemon.species.impl.local.pagination.model.PaginationData

internal interface PokemonSpeciesFeedPaginationDataSource {

    suspend fun getPaginationData(): PaginationData?
    suspend fun savePaginationData(paginationData: PaginationData)
}
