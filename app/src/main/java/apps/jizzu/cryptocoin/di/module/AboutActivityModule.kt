package apps.jizzu.cryptocoin.di.module

import apps.jizzu.cryptocoin.di.scope.ActivityScope
import apps.jizzu.cryptocoin.screens.about.AboutPresenter
import dagger.Module
import dagger.Provides

@Module
class AboutActivityModule {

    @Provides
    @ActivityScope
    fun provideAboutPresenter() = AboutPresenter()
}