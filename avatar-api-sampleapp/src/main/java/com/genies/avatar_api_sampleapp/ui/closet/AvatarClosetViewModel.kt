package com.genies.avatar_api_sampleapp.ui.closet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genies.avatar_api_sampleapp.domain.AvatarRepository
import com.genies.avatar_api_sampleapp.model.Error
import com.genies.avatar_api_sampleapp.model.Resource
import com.genies.avatar_api_sampleapp.model.Success
import com.genies.avatar_api_sampleapp.model.AssetDisplayModel
import com.genies.avatar_api_sampleapp.util.Event

import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.launch

class AvatarClosetViewModel : ViewModel() {

    private val avatarRepository = AvatarRepository

    private val _closetLiveData: MutableLiveData<Resource<List<AssetDisplayModel>>> by lazy { MutableLiveData<Resource<List<AssetDisplayModel>>>() }
    val closetLiveData: LiveData<Resource<List<AssetDisplayModel>>> = _closetLiveData

    private val _assetActionLiveData: MutableLiveData<Event<String>> by lazy { MutableLiveData<Event<String>>() }
    val assetActionLiveData: LiveData<Event<String>> = _assetActionLiveData

    fun loadCloset() = viewModelScope.launch {
        _closetLiveData.value = Resource.Loading
        avatarRepository.getLatestAssets()
            .andThen { composerAssets ->
                avatarRepository.getClosetItems()
                    .onSuccess { closetItems ->

                        // Get the asset ids for the closet items that are in user's possession
                        val assetsInPossesion =
                            closetItems.filter { it.inPossession }.map { it.assetId }

                        // Filter only composer assets that are in user's possession
                        val closetList =
                            composerAssets.filter { composerAsset ->
                                composerAsset.assetId in assetsInPossesion
                            }.map { composerAsset ->
                                AssetDisplayModel(
                                    composerAsset,
                                    closetItems.firstOrNull { closetItem -> closetItem.inPossession && closetItem.assetId == composerAsset.assetId })
                            }

                        _closetLiveData.value = Success(closetList)
                    }
            }
            .onFailure {
                _closetLiveData.value = Error(it.message)
            }

    }

    fun withdrawAsset(asset: AssetDisplayModel) = viewModelScope.launch {
        asset.closetItem?.let {
            avatarRepository.withdrawClosetItem(asset.closetItem.assetId, asset.closetItem.instanceId)
                .onSuccess {
                    loadCloset()
                    _assetActionLiveData.value = Event("Successfully withdrawn item")
                }
                .onFailure {
                    loadCloset()
                    _assetActionLiveData.value = Event("Error while withdrawing item")
                }
        }

    }

}