package com.app.baljeet.iconfinderapp.ui.main.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.app.baljeet.iconfinderapp.BuildConfig
import com.app.baljeet.iconfinderapp.MyApp
import com.app.baljeet.iconfinderapp.data.IconsRepository
import com.app.baljeet.iconfinderapp.models.IconsModel
import com.app.baljeet.iconfinderapp.models.WrapperData
import com.app.baljeet.iconfinderapp.ui.main.di.components.DaggerDataComponent
import com.app.baljeet.iconfinderapp.ui.main.di.components.DataComponent
import com.app.baljeet.iconfinderapp.ui.main.di.modules.DataModule
import com.app.baljeet.iconfinderapp.utils.Constants
import com.app.baljeet.iconfinderapp.utils.DirectoryHelper
import com.app.baljeet.iconfinderapp.utils.DownloadIconService
import javax.inject.Inject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dataComponent: DataComponent by lazy {
        DaggerDataComponent.builder()
            .dataModule(DataModule())
            .build()
    }

    init {
        dataComponent.inject(this)
    }

    @Inject
    lateinit var iconsRepository: IconsRepository

    fun getIcons(searchQuery: String, count: Int, offset: Int): MediatorLiveData<WrapperData<IconsModel>> {
        return iconsRepository.getIcons(getParams(searchQuery, count, offset))
    }

    fun downloadIcon(downloadUrl: String?) {
        val application = getApplication<MyApp>()
        val applicationContext = application.applicationContext

        if (downloadUrl != null) {
            createDirectoryAndDownloadIcon(applicationContext, downloadUrl)
        }
    }

    private fun getParams(searchQuery: String, count: Int, offset: Int): LinkedHashMap<String, String> {
        val data: LinkedHashMap<String, String> = LinkedHashMap()

        data[Constants.QUERY] = searchQuery
        data[Constants.COUNT] = count.toString()
        data[Constants.OFFSET] = offset.toString()
        data[Constants.CLIENT_ID] = BuildConfig.client_id
        data[Constants.CLIENT_SECRET] = BuildConfig.client_secret

        return data
    }

    private fun createDirectoryAndDownloadIcon(context: Context, downloadUrl: String) {
        DirectoryHelper.createDirectory(context)
        val downloadService = DownloadIconService.getDownloadService(
            context,
            downloadUrl,
            Constants.ROOT_DIRECTORY_NAME.plus("/")
        )
        context.startService(downloadService)
    }
}