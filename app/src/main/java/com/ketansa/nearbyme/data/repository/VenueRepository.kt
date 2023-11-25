package com.ketansa.nearbyme.data.repository

import com.ketansa.nearbyme.domain.GetVenuesRequest
import com.ketansa.nearbyme.domain.VenueResponse

interface VenueRepository {
    suspend fun getVenues(request: GetVenuesRequest): VenueResponse
}