package com.ketansa.nearbyme.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketansa.nearbyme.domain.GetVenuesRequest
import com.ketansa.nearbyme.domain.VenueResponse
import com.ketansa.nearbyme.domain.usecases.GetVenuesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NearbyPlacesVM(private val getVenuesUseCase: GetVenuesUseCase) : ViewModel() {

    private val _venueState = MutableStateFlow<VenueState>(VenueState.Loading)
    val venueState: StateFlow<VenueState> = _venueState

    fun getVenues(request: GetVenuesRequest) {
        Log.d("TAG-imptan", "getVenues: getVenues is called, range is ${request.range}, ${request.page}")
        viewModelScope.launch {
            try {
                val response = getVenuesUseCase.execute(request)
                Log.d("TAG-imptan", "getVenues: getVenues success response $response")
                _venueState.value = VenueState.Success(response)
            } catch (e: Exception) {
                Log.d("TAG-imptan", "getVenues: getVenues error response ${e.localizedMessage}")
                _venueState.value = VenueState.Error("Error fetching venues")
            }
        }
    }
}

sealed class VenueState {
    object Loading : VenueState()
    data class Success(val response: VenueResponse) : VenueState()
    data class Error(val message: String) : VenueState()
}