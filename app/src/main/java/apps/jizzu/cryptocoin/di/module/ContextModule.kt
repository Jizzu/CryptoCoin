package apps.jizzu.cryptocoin.di.module

import android.content.Context
import apps.jizzu.cryptocoin.di.scope.AppScope
import dagger.Module
import dagger.Provides

@Module
class ContextModule(private val context : Context) {

    @Provides
    @AppScope
    fun provideContext() = context
}