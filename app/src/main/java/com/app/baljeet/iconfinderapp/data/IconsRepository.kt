package com.app.baljeet.iconfinderapp.data

import androidx.lifecycle.MediatorLiveData
import com.app.baljeet.iconfinderapp.models.IconsModel
import com.app.baljeet.iconfinderapp.models.WrapperData
import com.app.baljeet.iconfinderapp.utils.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IconsRepository {
    private val iconsModelLiveData = MediatorLiveData<WrapperData<IconsModel>>()

    fun getIcons(queryParams: LinkedHashMap<String, String>): MediatorLiveData<WrapperData<IconsModel>> {
        CoroutineScope(Dispatchers.IO).launch {
            val iconsResponse = RetrofitClient.retrofitClient.getIcons(queryParams)

            withContext(Dispatchers.Main) {
                if (iconsResponse.isSuccessful) {
                    iconsModelLiveData.value = WrapperData(false, iconsResponse.body(), iconsResponse.message())
                } else {
                    iconsModelLiveData.value = WrapperData(true, null, iconsResponse.message())
                }
            }
        }

        return iconsModelLiveData
    }
}