package com.genies.avatar_api_sampleapp.ui.assetslist


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genies.avatar_api_sampleapp.domain.AvatarRepository
import com.genies.avatar_api_sampleapp.domain.UserSessionRepository
import com.genies.avatar_api_sampleapp.model.AssetDisplayModel
import com.genies.avatar_api_sampleapp.model.Error
import com.genies.avatar_api_sampleapp.model.Resource
import com.genies.avatar_api_sampleapp.model.Success
import com.genies.avatar_api_sampleapp.util.Event
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.launch

class AvatarAssetsListViewModel : ViewModel() {
    private val userSessionRepository = UserSessionRepository
    private val avatarRepository = AvatarRepository

    private val _signOutState: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData<Event<Boolean>>() }
    val signOutState: LiveData<Event<Boolean>> = _signOutState

    private val _avatarAssetsLiveData: MutableLiveData<Resource<List<AssetDisplayModel>>> by lazy { MutableLiveData<Resource<List<AssetDisplayModel>>>() }
    val avatarAssetsLiveData: LiveData<Resource<List<AssetDisplayModel>>> = _avatarAssetsLiveData

    private val _assetActionLiveData: MutableLiveData<Event<String>> by lazy { MutableLiveData<Event<String>>() }
    val assetActionLiveData: LiveData<Event<String>> = _assetActionLiveData

    fun loadAssets() = viewModelScope.launch {
        _avatarAssetsLiveData.value = Resource.Loading
        avatarRepository.getClosetItems()
            .andThen { closetItems ->
                avatarRepository.getLatestAssets()
                    .onSuccess { composerAssets ->
                        _avatarAssetsLiveData.value = Success(
                            composerAssets.map { asset ->
                                AssetDisplayModel(
                                    composerAsset = asset,
                                    closetItem = closetItems.firstOrNull { closetItem -> closetItem.assetId == asset.assetId && closetItem.inPossession })
                            }
                        )
                    }
            }
            .onFailure {
                _avatarAssetsLiveData.value = Error(it.message)
            }
    }

    fun depositAsset(asset: AssetDisplayModel) = viewModelScope.launch {
        avatarRepository.depositClosetItem(asset.composerAsset.assetId)
            .onSuccess {
                _assetActionLiveData.value = Event("Successfully deposited item")
                loadAssets()
            }
            .onFailure {
                _assetActionLiveData.value = Event("Error while depositing item")
                loadAssets()
            }
    }

    fun withdrawAsset(asset: AssetDisplayModel) = viewModelScope.launch {
        asset.closetItem?.let {
            avatarRepository.withdrawClosetItem(asset.closetItem.assetId, asset.closetItem.instanceId)
                .onSuccess {
                    _assetActionLiveData.value = Event("Successfully withdrawn item")
                    loadAssets()
                }
                .onFailure {
                    _assetActionLiveData.value = Event("Error while withdrawing item")
                    loadAssets()
                }
        }

    }

    fun signOut() = viewModelScope.launch {
        userSessionRepository.signOut()
        avatarRepository.clear()
        _signOutState.value = Event(true)
    }
}

