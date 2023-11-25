package com.ketansa.nearbyme.data.repository

import com.ketansa.nearbyme.AppContainer
import com.ketansa.nearbyme.domain.GetVenuesRequest
import com.ketansa.nearbyme.domain.VenueResponse
import com.ketansa.nearbyme.util.Constants.CLIENT_ID


class VenueRepositoryImpl : VenueRepository {
    private val venueService = AppContainer.venueService
    override suspend fun getVenues(request: GetVenuesRequest): VenueResponse {
        return venueService.getVenues(
            request.perPage,
            request.page,
            CLIENT_ID,
            request.lat,
            request.lon,
            request.range,
            request.query ?: ""
        )
    }
}
