package apps.jizzu.cryptocoin.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val baseURL = "https://api.coinmarketcap.com/v1/"

    private val retrofit = Retrofit.Builder().baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val client = retrofit.create(Api::class.java)
}
