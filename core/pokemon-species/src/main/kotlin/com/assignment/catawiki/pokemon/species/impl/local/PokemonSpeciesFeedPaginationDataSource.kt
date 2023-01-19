package com.assignment.catawiki.pokemon.species.impl.local

import com.assignment.catawiki.pokemon.species.impl.local.model.PaginationData

internal interface PokemonSpeciesFeedPaginationDataSource {

    suspend fun getPaginationData(): PaginationData?
    suspend fun savePaginationData(paginationData: PaginationData)
}
