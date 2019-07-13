package apps.jizzu.cryptocoin.di

import android.content.Context
import apps.jizzu.cryptocoin.network.Api
import apps.jizzu.cryptocoin.screens.adapter.CoinsAdapter
import apps.jizzu.cryptocoin.screens.vm.CoinsViewModel
import apps.jizzu.cryptocoin.utils.BASE_URL
import apps.jizzu.cryptocoin.utils.PreferenceHelper
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { CoinsAdapter() }
    single { PreferenceHelper(get()) }
    factory { androidContext().getSharedPreferences("preferences", Context.MODE_PRIVATE) }
    viewModel { CoinsViewModel(get(), get()) }
}

val networkModule = module {
    factory { provideCryptocurrencyApi(get()) }
    single { provideRetrofit(get(), get(), get()) }
    factory { provideOkHttpClient(get()) }
    factory { provideHttpLoggingInterceptor() }
    factory { GsonConverterFactory.create() }
    factory { RxJava2CallAdapterFactory.create() }
}

fun provideCryptocurrencyApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

fun provideRetrofit(okHttpClient: OkHttpClient,
                    gsonConverterFactory: GsonConverterFactory,
                    rxJava2CallAdapterFactory: RxJava2CallAdapterFactory): Retrofit {
    return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .build()
}

fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient()
            .newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
}

fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}