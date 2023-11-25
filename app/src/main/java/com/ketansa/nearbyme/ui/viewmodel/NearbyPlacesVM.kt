package com.ketansa.nearbyme.ui.viewmodel

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
        viewModelScope.launch {
            try {
                val response = getVenuesUseCase.execute(request)
                _venueState.value = VenueState.Success(response)
            } catch (e: Exception) {
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