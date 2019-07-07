package apps.jizzu.cryptocoin.di.component

import apps.jizzu.cryptocoin.di.module.AboutActivityModule
import apps.jizzu.cryptocoin.di.module.DetailsActivityModule
import apps.jizzu.cryptocoin.di.module.MainActivityModule
import apps.jizzu.cryptocoin.di.scope.ActivityScope
import apps.jizzu.cryptocoin.screens.about.AboutActivity
import apps.jizzu.cryptocoin.screens.details.DetailsActivity
import apps.jizzu.cryptocoin.screens.main.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [MainActivityModule::class, DetailsActivityModule::class, AboutActivityModule::class])
interface ScreensComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: DetailsActivity)
    fun inject(activity: AboutActivity)
}