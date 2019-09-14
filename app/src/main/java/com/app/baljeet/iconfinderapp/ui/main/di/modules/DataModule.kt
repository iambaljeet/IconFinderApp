package com.app.baljeet.iconfinderapp.ui.main.di.modules

import com.app.baljeet.iconfinderapp.data.IconsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideIconRepository(): IconsRepository {
        return IconsRepository()
    }
}