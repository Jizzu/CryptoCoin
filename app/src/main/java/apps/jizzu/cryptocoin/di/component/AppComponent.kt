package apps.jizzu.cryptocoin.di.component

import apps.jizzu.cryptocoin.di.module.ContextModule
import apps.jizzu.cryptocoin.di.module.NetworkModule
import apps.jizzu.cryptocoin.di.module.SharedPreferencesModule
import apps.jizzu.cryptocoin.di.scope.AppScope
import dagger.Component

@AppScope
@Component(modules = [ContextModule::class, SharedPreferencesModule::class, NetworkModule::class])
interface AppComponent {
    fun createScreensComponent(): ScreensComponent
}