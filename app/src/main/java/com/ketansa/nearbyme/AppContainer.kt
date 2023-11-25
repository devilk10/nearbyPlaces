package com.ketansa.nearbyme

import com.ketansa.nearbyme.data.api.VenueApiService
import com.ketansa.nearbyme.data.repository.VenueRepositoryImpl
import com.ketansa.nearbyme.domain.usecases.GetVenuesUseCase
import com.ketansa.nearbyme.ui.viewmodel.NearbyPlacesVM
import com.ketansa.nearbyme.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppContainer {
    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
    val venueService = VenueApiService(retrofit)
    private val venueRepo = VenueRepositoryImpl()
    val nearbyPlacesVM = NearbyPlacesVM(GetVenuesUseCase(venueRepo))
}