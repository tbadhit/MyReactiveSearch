package com.tbadhit.myreactivesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tbadhit.myreactivesearch.network.ApiConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel: ViewModel() {
    private val accessToken = "pk.eyJ1IjoidGJhZGhpdCIsImEiOiJjbDA3eDBud3oybDNuM2RuZnU3cmpjcGxzIn0.lbOrs1Z2BSS8hSjKsO-KTg"
    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    val searchResult = queryChannel.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            ApiConfig.provideApiService().getCountry(it,accessToken).features
        }
        .asLiveData()
}