package apps.jizzu.cryptocoin.di.module

import apps.jizzu.cryptocoin.di.scope.ActivityScope
import apps.jizzu.cryptocoin.network.Api
import apps.jizzu.cryptocoin.screens.main.MainModel
import apps.jizzu.cryptocoin.screens.main.MainPresenter
import apps.jizzu.cryptocoin.screens.main.adapter.CoinsAdapter
import apps.jizzu.cryptocoin.utils.PreferenceHelper
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    @ActivityScope
    fun provideCoinsAdapter() = CoinsAdapter()

    @Provides
    @ActivityScope
    fun provideMainPresenter(mainModel: MainModel) = MainPresenter(mainModel)

    @Provides
    @ActivityScope
    fun provideMainModel(api: Api, preferenceHelper: PreferenceHelper) = MainModel(api, preferenceHelper)
}