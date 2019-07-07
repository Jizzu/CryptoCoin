package apps.jizzu.cryptocoin.di.module

import apps.jizzu.cryptocoin.di.scope.ActivityScope
import apps.jizzu.cryptocoin.screens.details.DetailsPresenter
import dagger.Module
import dagger.Provides

@Module
class DetailsActivityModule {

    @Provides
    @ActivityScope
    fun provideDetailsPresenter() = DetailsPresenter()
}