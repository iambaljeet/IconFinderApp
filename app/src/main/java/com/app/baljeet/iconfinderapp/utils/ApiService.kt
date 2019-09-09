package com.app.baljeet.iconfinderapp.utils

import com.app.baljeet.iconfinderapp.models.IconsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {
    @GET(NetworkConstants.URL_ICONS)
    suspend fun getIcons(@QueryMap params: LinkedHashMap<String, String>): Response<IconsModel>
}