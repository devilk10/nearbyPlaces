package com.ketansa.nearbyme.domain.usecases

import com.ketansa.nearbyme.data.repository.VenueRepository
import com.ketansa.nearbyme.domain.GetVenuesRequest
import com.ketansa.nearbyme.domain.VenueResponse

class GetVenuesUseCase(private val venueRepository: VenueRepository) {
    suspend fun execute(request: GetVenuesRequest): VenueResponse {
        return venueRepository.getVenues(request)
    }
}
