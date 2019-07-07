package apps.jizzu.cryptocoin.di.module

import apps.jizzu.cryptocoin.di.scope.AppScope
import apps.jizzu.cryptocoin.network.Api
import apps.jizzu.cryptocoin.utils.BASE_URL
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [OkHttpClientModule::class])
class NetworkModule {

    @Provides
    fun provideCryptocurrencyApi(retrofit: Retrofit) = retrofit.create(Api::class.java)

    @Provides
    @AppScope
    fun provideRetrofit(okHttpClient: OkHttpClient,
                        gsonConverterFactory: GsonConverterFactory,
                        rxJava2CallAdapterFactory : RxJava2CallAdapterFactory): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .build()
    }

    @Provides
    fun provideGsonConverterFactory() = GsonConverterFactory.create()

    @Provides
    fun provideRxJava2CallAdapterFactory() = RxJava2CallAdapterFactory.create()
}