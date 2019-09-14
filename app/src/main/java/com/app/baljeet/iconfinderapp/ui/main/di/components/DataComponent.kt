package com.app.baljeet.iconfinderapp.ui.main.di.components

import com.app.baljeet.iconfinderapp.ui.main.di.modules.DataModule
import com.app.baljeet.iconfinderapp.ui.main.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface DataComponent {
    fun inject(mainViewModel: MainViewModel)
}