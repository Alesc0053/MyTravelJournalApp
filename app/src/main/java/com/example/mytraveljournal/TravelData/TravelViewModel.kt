package com.example.mytraveljournal.TravelData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mytraveljournal.R
import com.mapbox.search.autocomplete.PlaceAutocomplete
import com.mapbox.search.autocomplete.PlaceAutocompleteOptions
import com.mapbox.search.autocomplete.PlaceAutocompleteType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TravelViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Travel>>
    private val repository: TravelRepository
    var readAllUserTravel: LiveData<List<Travel>>
    private val placeAutocomplete = PlaceAutocomplete.create(application.getString(R.string.mapbox_access_token))


    init {
        val travelDao = TravelDatabase.getInstance(application).dao
        repository = TravelRepository(travelDao)
        readAllData = repository.readAllData
        readAllUserTravel = MutableLiveData()
    }

    fun getUserTavel(userId: Int): LiveData<List<Travel>> {
        readAllUserTravel = repository.getUserTavel(userId)
        return readAllUserTravel
    }

    fun insertTravel(travel: Travel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTravel(travel)
        }
    }

    fun updateTravel(travel: Travel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTravel(travel)
        }
    }

    fun deleteTavel(travel: Travel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTravel(travel)
        }
    }

    fun decodeLocation(
        query: String,
        onDecodeComplete: (name: String, fullAddress: String, long: String, lat: String) -> Unit,
        onDecodeFailed: (Exception) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                placeAutocomplete.suggestions(
                    query = query, options = PlaceAutocompleteOptions(
                        limit = 1,
                        types = listOf(
                            PlaceAutocompleteType.AdministrativeUnit.Region,
                            PlaceAutocompleteType.AdministrativeUnit.Place,
                            PlaceAutocompleteType.AdministrativeUnit.Country,
                            PlaceAutocompleteType.AdministrativeUnit.Locality
                        )
                    )
                )
            withContext(Dispatchers.Main) {
            response.onValue {
                val place = it.firstOrNull()
                place?.let { location ->
                    onDecodeComplete.invoke(
                        location.name,
                        location.formattedAddress.toString(),
                        location.coordinate.longitude().toString(),
                        location.coordinate.latitude().toString()
                    )
                }
            }
            }.onError {
                onDecodeFailed.invoke(it)
            }
        }
    }

}