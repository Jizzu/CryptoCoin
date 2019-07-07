package apps.jizzu.cryptocoin.di.module

import android.content.Context
import android.content.SharedPreferences
import apps.jizzu.cryptocoin.di.scope.AppScope
import apps.jizzu.cryptocoin.utils.PreferenceHelper
import dagger.Module
import dagger.Provides

@Module
class SharedPreferencesModule {

    @Provides
    @AppScope
    fun providePreferenceHelper(sharedPreferences: SharedPreferences) =
            PreferenceHelper(sharedPreferences)

    @Provides
    fun provideSharedPreferences(context: Context) =
            context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
}